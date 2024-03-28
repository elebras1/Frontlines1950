package com.strategygame.frontlines1950.map;

public class Dimension {
    /**
     * the width of the dimension
     */
    private int width;
    /**
     * the height of the dimension
     */
    private int height;

    /**
     * Constructor for the Dimension class
     * @param width the width of the dimension
     * @param height the height of the dimension
     */
    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Get the width of the dimension
     * @return the width of the dimension
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the dimension
     * @return the height of the dimension
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Set the width of the dimension
     * @param width the width of the dimension
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Set the height of the dimension
     * @param height the height of the dimension
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the scale of the dimension
     * @return the scale of the dimension
     */
    public int getScale() {
        return this.height / this.width;
    }
}
