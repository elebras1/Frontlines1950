package com.strategygame.frontlines1950.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Province implements GeoLocation {
    private final Set<Pixel> pixels = new HashSet<>();
    private Color color;
    private int id;
    private String name;
    private State state = null;
    private Vector2 origin;

    private Dimension dimension;
    static public class Pixel {
        private final int x;
        private final int y;
        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX() {
            return this.x;
        }
        public int getY() {
            return this.y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pixel pixel = (Pixel) o;
            return x == pixel.x && y == pixel.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Pixel{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Province(int id) {
        this.id = id;
    }

    public Set<Pixel> getPixels() {
        return this.pixels;
    }

    public void addPixel(int x, int y) {
        pixels.add(new Pixel(x, y));
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isPixelProvince(int x, int y) {
        return this.pixels.contains(new Pixel(x, y));
    }

    public void setOrigin() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (Province.Pixel pixel : this.getPixels()) {
            minX = Math.min(minX, pixel.getX());
            minY = Math.min(minY, pixel.getY());
        }

        this.origin = new Vector2(minX, minY);
    }

    public void setDimension() {
        int minWidth = Integer.MAX_VALUE;
        int minHeight = Integer.MAX_VALUE;
        int maxWidth = Integer.MIN_VALUE;
        int maxHeight = Integer.MIN_VALUE;

        for (Province.Pixel pixel : this.pixels) {
            minWidth = Math.min(minWidth, pixel.getX());
            minHeight = Math.min(minHeight, pixel.getY());
            maxWidth = Math.max(maxWidth, pixel.getX());
            maxHeight = Math.max(maxHeight, pixel.getY());
        }
        int actualWidth = maxWidth - minWidth + 1;
        int actualHeight = maxHeight - minHeight + 1;
        this.dimension = new Dimension(actualWidth, actualHeight);
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + this.id +
                ", color='" + this.color + '\'' +
                ", name='" + this.name + '\'' +
                ", number_pixels=" + this.pixels.size() +
                '}';
    }
}
