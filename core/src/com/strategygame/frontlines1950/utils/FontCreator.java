package com.strategygame.frontlines1950.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontUtils {
    /**
     * The default size for fonts
     */
    static int defaultSize = 12;

    /**
     * Create a new font with the specified size
     *
     * @param fontFile the font file
     * @return the font
     */
    public static BitmapFont createFont(String fontFile, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/eurostile.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        font.getData().setScale(size);
        return font;
    }

    /**
     * Create a new font with the default size
     *
     * @param fontFile the font file
     * @return then font
     */
    public static BitmapFont createFont(String fontFile) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFile));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        BitmapFont font = generator.generateFont(parameter);
        parameter.borderColor = Color.WHITE; // Définissez la couleur de la bordure
        parameter.borderWidth = 2;
        generator.dispose();
        return font;
    }
}
