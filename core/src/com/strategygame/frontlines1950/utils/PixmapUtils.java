package com.strategygame.frontlines1950.utils;

import com.badlogic.gdx.graphics.Pixmap;

public class PixmapUtils {

    static public Pixmap flipVertically(Pixmap pixmap) {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();

        Pixmap flippedPixmap = new Pixmap(width, height, pixmap.getFormat());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = pixmap.getPixel(x, height - y - 1);
                flippedPixmap.drawPixel(x, y, color);
            }
        }

        return flippedPixmap;
    }
}
