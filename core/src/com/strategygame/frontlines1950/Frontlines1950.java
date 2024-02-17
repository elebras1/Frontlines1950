package com.strategygame.frontlines1950;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;

public class Frontlines1950 extends Game {

    private Screen screen;
    static final int WORLD_WIDTH = 5616;
    static final int WORLD_HEIGHT = 2160;

    @Override
    public void create() {
        this.changeCursor();

        //this.screen = new LoadScreen(this);
        this.screen = new MainMenuScreen(this);
        setScreen(screen);
    }

    public void changeCursor() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("ui/cursor/normal.png"));
        int xHotspot = 0, yHotspot = 0;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose();
        Gdx.graphics.setCursor(cursor);
    }

    @Override
    public void dispose() {
        this.screen.dispose();
        super.dispose();
    }
}



