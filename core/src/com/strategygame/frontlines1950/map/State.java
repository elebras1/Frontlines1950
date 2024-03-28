package com.strategygame.frontlines1950.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.strategygame.frontlines1950.utils.Bresenham;
import com.strategygame.frontlines1950.utils.PixmapOperations;

import java.util.*;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

public class State implements GeoLocation {
    private final int id;
    private final Set<Province> provinces = new HashSet<>();
    private Vector2 origin;
    private Vector2 center;
    private Country country = null;
    private Dimension dimension;
    private Texture texture;
    private Texture selectedTexture;
    private List<Province.Pixel> borderPixels;
    private List<Regiment> regiments;

    public State(int id, Country country) {

        this.id = id;
        this.country = country;
    }

    public int getId() {
        return this.id;
    }

    public Set<Province> getProvinces() {
        return this.provinces;
    }

    public void addProvinces(Set<Province> provinces) {
        this.provinces.addAll(provinces);
        provinces.forEach(province -> province.setState(this));
    }

    public Country getCountry() {
        return this.country;
    }

    public void setOrigin() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (Province province : this.provinces) {
            for (Province.Pixel pixel : province.getPixels()) {
                minX = Math.min(minX, pixel.getX());
                minY = Math.min(minY, pixel.getY());
            }
        }

        this.origin = new Vector2(minX, minY);
    }

    public void setCenter() {
        List<Province.Pixel> borderPixelsList = new ArrayList<>(borderPixels);
        int step = 5;
        int maxDistance = 0;
        Province.Pixel pixelA = null;
        Province.Pixel pixelB = null;

        for (int i = 0; i < borderPixelsList.size(); i += step) {
            Province.Pixel tempPixelA = borderPixelsList.get(i);
            for (int j = i + step; j < borderPixelsList.size(); j += step) {
                Province.Pixel tempPixelB = borderPixelsList.get(j);
                int distance = (int) Math.sqrt(Math.pow(tempPixelA.getX() - tempPixelB.getX(), 2) + Math.pow(tempPixelA.getY() - tempPixelB.getY(), 2));
                if (distance > maxDistance && Bresenham.isLineInState(tempPixelA, tempPixelB, this) && tempPixelA.getX() < tempPixelB.getX()) {
                    maxDistance = distance;
                    pixelA = tempPixelA;
                    pixelB = tempPixelB;
                }
            }
        }
        if(pixelA == null) {
            return;
        }
        int x = (pixelA.getX() + pixelB.getX()) / 2;
        int y = (pixelA.getY() + pixelB.getY()) / 2;
        this.center = new Vector2(x, y);
    }

    public void setDimension() {
        int minWidth = Integer.MAX_VALUE;
        int minHeight = Integer.MAX_VALUE;
        int maxWidth = Integer.MIN_VALUE;
        int maxHeight = Integer.MIN_VALUE;

        for (Province province : this.provinces) {
            for (Province.Pixel pixel : province.getPixels()) {
                minWidth = Math.min(minWidth, pixel.getX());
                minHeight = Math.min(minHeight, pixel.getY());
                maxWidth = Math.max(maxWidth, pixel.getX());
                maxHeight = Math.max(maxHeight, pixel.getY());
            }
        }
        int actualWidth = maxWidth - minWidth + 1;
        int actualHeight = maxHeight - minHeight + 1;
        this.dimension = new Dimension(actualWidth, actualHeight);
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public boolean isPixelState(int x, int y) {
        if (x < this.origin.x || x > this.origin.x + this.dimension.getWidth() || y < this.origin.y || y > this.origin.y + this.dimension.getHeight()) {
            return false;
        }

        for (Province province : this.provinces) {
            if (province.isPixelProvince(x, y)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPixelBorder(Province.Pixel pixel) {
        int x = pixel.getX();
        int y = pixel.getY();
        return !isPixelState(x - 1, y) || !isPixelState(x + 1, y) ||
                !isPixelState(x, y - 1) || !isPixelState(x, y + 1);
    }

    public void setBorderPixels() {
        this.borderPixels = new ArrayList<>();

        for (Province province : this.provinces) {
            for (Province.Pixel pixel : province.getPixels()) {
                if (isPixelBorder(pixel)) {
                    this.borderPixels.add(pixel);
                }
            }
        }
    }

    public void setRegiment() {
        this.regiments = new ArrayList<>();
        for(int i = 0; i < this.provinces.size(); i++) {
            this.regiments.add(new Regiment());
        }
    }

    public void createTexture() {
        Pixmap pixmap = new Pixmap(this.getDimension().getWidth(), this.getDimension().getHeight(), Pixmap.Format.RGBA8888);
        Pixmap pixmapSelected = new Pixmap(this.getDimension().getWidth(), this.getDimension().getHeight(), Pixmap.Format.RGBA8888);
        Color color = this.getCountry().getColor();

        if (color != null) {
            for (Province.Pixel pixel : this.borderPixels) {
                int relativeX = (int) (pixel.getX() - this.origin.x);
                int relativeY = (int) (pixel.getY() - this.origin.y);
                pixmap.drawPixel(relativeX, relativeY, Color.rgba8888(new Color(0f, 0f, 0f, 1f)));
                pixmapSelected.drawPixel(relativeX, relativeY, Color.rgba8888(new Color(1f, 1f, 1f, 1f)));
            }
        }

        this.texture = new Texture(PixmapOperations.flipVertically(pixmap));
        this.selectedTexture = new Texture(PixmapOperations.flipVertically(pixmapSelected));
        pixmap.dispose();
        pixmapSelected.dispose();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.texture, this.origin.x, this.origin.y);
        batch.draw(this.texture, this.origin.x - WORLD_WIDTH, this.origin.y);
        batch.draw(this.texture, this.origin.x + WORLD_WIDTH, this.origin.y);
        int offset = 0;
        for(Regiment regiment : this.regiments) {
            regiment.draw(batch, this.center.x, this.center.y + offset, this.dimension);
            regiment.draw(batch, this.center.x - WORLD_WIDTH, this.center.y + offset, this.dimension);
            regiment.draw(batch, this.center.x + WORLD_WIDTH, this.center.y + offset, this.dimension);
            offset -= this.dimension.getScale() * 2;
        }
    }

    public void drawSelected(SpriteBatch batch) {
        batch.draw(this.selectedTexture, this.origin.x, this.origin.y);
        batch.draw(this.selectedTexture, this.origin.x - WORLD_WIDTH, this.origin.y);
        batch.draw(this.selectedTexture, this.origin.x + WORLD_WIDTH, this.origin.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return this.id  == state.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id );
    }

    public void dispose() {
        this.selectedTexture.dispose();
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + this.id +
                "number_provinces=" + this.provinces.size() +
                '}';
    }
}
