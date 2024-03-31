package com.strategygame.frontlines1950.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Regiment {
    private static final Skin skin = new Skin(Gdx.files.internal("images/mapitems/skin/mapitemskin.json"));

    public void draw(SpriteBatch batch, float x, float y, float scale) {
        float width = skin.getRegion("infantry_regiment").getRegionWidth() * scale;
        float height = skin.getRegion("infantry_regiment").getRegionHeight() * scale;
        batch.draw(skin.getRegion("infantry_regiment"), x - width, y - height, width, height);
    }
}
