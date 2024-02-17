package com.strategygame.frontlines1950;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;
import static com.strategygame.frontlines1950.utils.PixmapUtils.flipVertically;

public class State implements GeoLocation {
    private final int id;
    private final Set<Province> provinces = new HashSet<>();
    private int originX;
    private int originY;
    private Country country = null;
    private int[] dimensions;
    private Texture texture;
    private List<Province.Pixel> borderPixels;

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

        this.originX = minX;
        this.originY = minY;
    }

    public int getOriginX() {
        return this.originX;
    }

    public int getOriginY() {
        return this.originY;
    }

    public void setDimensions() {
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
        this.dimensions = new int[]{actualWidth, actualHeight};
    }

    public int[] getDimensions() {
        return this.dimensions;
    }

    public boolean isPixelState(int x, int y) {
        if (x < this.originX || x > this.originX + this.dimensions[0] || y < this.originY || y > this.originY + this.dimensions[1]) {
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

    public void createTexture() {
        Pixmap pixmap = new Pixmap(this.getDimensions()[0], this.getDimensions()[1], Pixmap.Format.RGBA8888);
        Color color = this.getCountry().getColor();

        if (color != null) {
            for (Province.Pixel pixel : this.borderPixels) {
                int relativeX = pixel.getX() - this.getOriginX();
                int relativeY = pixel.getY() - this.getOriginY();
                pixmap.drawPixel(relativeX, relativeY, Color.rgba8888(new Color(1f, 1f, 1f, 1f)));
            }
        }

        this.texture = new Texture(flipVertically(pixmap));
        pixmap.dispose();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.texture, this.getOriginX(), this.getOriginY());
        batch.draw(this.texture, this.getOriginX() - WORLD_WIDTH, this.getOriginY());
        batch.draw(this.texture, this.getOriginX() + WORLD_WIDTH, this.getOriginY());
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
        this.texture.dispose();
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + this.id +
                "number_provinces=" + this.provinces.size() +
                '}';
    }
}
