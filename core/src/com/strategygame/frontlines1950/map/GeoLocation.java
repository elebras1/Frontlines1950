package com.strategygame.frontlines1950.map;

public interface GeoLocation {
    void setOrigin();
    int getOriginX();
    int getOriginY();
    int[] getDimensions();
    void setDimensions();
}
