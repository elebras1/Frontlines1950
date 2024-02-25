package com.strategygame.frontlines1950.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PixmapOperations {
    /**
     * Flip a pixmap vertically
     *
     * @param pixmap the pixmap
     * @return the flipped pixmap
     */
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

    /**
     * Extract a pixmap from a texture region
     *
     * @param textureRegion the texture region
     * @return the pixmap
     */
    public static Pixmap extractPixmapFromTextureRegion(TextureRegion textureRegion) {
        TextureData textureData = textureRegion.getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = new Pixmap(
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                textureData.getFormat()
        );
        pixmap.drawPixmap(
                textureData.consumePixmap(), // The other Pixmap
                0, // The target x-coordinate (top left corner)
                0, // The target y-coordinate (top left corner)
                textureRegion.getRegionX(), // The source x-coordinate (top left corner)
                textureRegion.getRegionY(), // The source y-coordinate (top left corner)
                textureRegion.getRegionWidth(), // The width of the area from the other Pixmap in pixels
                textureRegion.getRegionHeight() // The height of the area from the other Pixmap in pixels
        );
        return pixmap;
    }

}
