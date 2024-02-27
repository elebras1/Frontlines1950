package com.strategygame.frontlines1950.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;
import static com.strategygame.frontlines1950.utils.PixmapOperations.flipVertically;

public class Country implements GeoLocation {
    private final String id;
    private final String name;
    private final Set<State> states = new HashSet<>();
    private final Color color;
    private Vector2 origin;
    private Dimension dimension;
    private Texture texture;
    private Texture selectedTexture;
    private Set<Province.Pixel> borderPixels;
    private final MapLabel label;

    public Country(String id, String name, int red, int green, int blue) {
        this.id = id;
        this.name = name;
        this.label = new MapLabel("fonts/eurostile_bold_256.fnt", this.name, this);
        this.color = new Color(red / 255f, green / 255f, blue / 255f, 1.0f);
    }

    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public Set<State> getStates() {
        return this.states;
    }

    public Color getColor() {
        return this.color;
    }

    public State addState(int id) {
        State state = new State(id, this);
        this.states.add(state);

        return state;
    }

    public int getNumberStates() {
        return this.states.size();
    }

    public void setOrigin() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (State state : this.states) {
            for (Province province : state.getProvinces()) {
                for (Province.Pixel pixel : province.getPixels()) {
                    minX = Math.min(minX, pixel.getX());
                    minY = Math.min(minY, pixel.getY());
                }
            }
        }

        this.origin = new Vector2(minX, minY);
    }

    public void setDimension() {
        int minWidth = Integer.MAX_VALUE;
        int minHeight = Integer.MAX_VALUE;
        int maxWidth = Integer.MIN_VALUE;
        int maxHeight = Integer.MIN_VALUE;

        for (State state : this.states) {
            for (Province province : state.getProvinces()) {
                for (Province.Pixel pixel : province.getPixels()) {
                    minWidth = Math.min(minWidth, pixel.getX());
                    minHeight = Math.min(minHeight, pixel.getY());
                    maxWidth = Math.max(maxWidth, pixel.getX());
                    maxHeight = Math.max(maxHeight, pixel.getY());
                }
            }
        }
        int actualWidth = maxWidth - minWidth + 1;
        int actualHeight = maxHeight - minHeight + 1;

        this.dimension = new Dimension(actualWidth, actualHeight);
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public boolean isPixelCountry(int x, int y) {
        if (x < this.origin.x || x > this.origin.x + this.dimension.getWidth() || y < this.origin.y || y > this.origin.y + this.dimension.getHeight()) {
            return false;
        }

        for (State state : this.states) {
            if (state.isPixelState(x, y)) {
                return true;
            }
        }
        return false;
    }


    private boolean isPixelBorder(Province.Pixel pixel) {
        int x = pixel.getX();
        int y = pixel.getY();

        return !isPixelCountry(x - 1, y) || !isPixelCountry(x + 1, y) ||
                !isPixelCountry(x, y - 1) || !isPixelCountry(x, y + 1);
    }

    public Set<Province.Pixel> getBorderPixels() {
        return this.borderPixels;
    }

    public void setBorderPixels() {
        this.borderPixels = new HashSet<>();

        for(State state : this.states) {
            for(Province province : state.getProvinces()) {
                for(Province.Pixel pixel : province.getPixels()) {
                    if(isPixelBorder(pixel)) {
                        this.borderPixels.add(pixel);
                    }
                }
            }
        }
    }

    public void createTexture() {
        Pixmap pixmap = new Pixmap(this.dimension.getWidth(), this.dimension.getHeight(), Pixmap.Format.RGBA8888);
        if(color != null) {
            for(State state : this.getStates()) {
                for (Province province : state.getProvinces()) {
                    for (Province.Pixel pixel : province.getPixels()) {
                        int relativeX = (int) (pixel.getX() - this.origin.x);
                        int relativeY = (int) (pixel.getY() - this.origin.y);
                        if(this.borderPixels.contains(pixel)) {
                            pixmap.drawPixel(relativeX, relativeY, Color.rgba8888(new Color(0 ,0 ,0, 1)));
                        }
                        else {
                            pixmap.drawPixel(relativeX, relativeY, Color.rgba8888(this.color));
                        }
                    }
                }
            }
        }

        this.texture = new Texture(flipVertically(pixmap));
        pixmap.dispose();
    }

    public void createSelectedTexture() {
        Pixmap pixmap = new Pixmap(this.dimension.getWidth(), this.dimension.getHeight(), Pixmap.Format.RGBA8888);
        if(color != null) {
            for(Province.Pixel pixel : this.borderPixels) {
                int relativeX = (int) (pixel.getX() - this.origin.x);
                int relativeY = (int) (pixel.getY() - this.origin.y);
                pixmap.drawPixel(relativeX, relativeY, Color.rgba8888(new Color(1 ,1 ,1, 1)));
            }
        }

        this.selectedTexture = new Texture(flipVertically(pixmap));
        pixmap.dispose();
    }

    public void draw(SpriteBatch batch, float zoom) {
        batch.draw(this.texture, this.origin.x, this.origin.y);
        batch.draw(this.texture, this.origin.x - WORLD_WIDTH, this.origin.y);
        batch.draw(this.texture, this.origin.x + WORLD_WIDTH, this.origin.y);
        if(zoom > 0.5) {
            this.label.draw(batch, this.borderPixels);
        } else {
            for(State state : this.states) {
                state.draw(batch);
            }
        }
    }

    public void drawSelected(SpriteBatch batch) {
        batch.draw(this.selectedTexture, this.origin.x, this.origin.y);
        batch.draw(this.selectedTexture, this.origin.x - WORLD_WIDTH, this.origin.y);
        batch.draw(this.selectedTexture, this.origin.x + WORLD_WIDTH, this.origin.y);
        this.label.draw(batch, this.borderPixels);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(id, country.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void dispose() {
        this.texture.dispose();
    }

    @Override
    public String toString() {
        return "Country{" +
                "id='" + this.id + '\'' +
                ", name='" + this.name + '\'' +
                ", color='" + this.color + '\'' +
                ", number_states='" + this.getNumberStates() + '\'' +
                '}';
    }
}
