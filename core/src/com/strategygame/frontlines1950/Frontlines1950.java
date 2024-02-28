package com.strategygame.frontlines1950;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.strategygame.frontlines1950.ui.CursorChanger;
import com.strategygame.frontlines1950.screens.MainMenuScreen;

public class Frontlines1950 extends Game {

    private Screen screen;
    public static final int WORLD_WIDTH = 5616;
    public static final int WORLD_HEIGHT = 2160;

    @Override
    public void create() {
        CursorChanger cursorChanger = new CursorChanger();
        cursorChanger.defaultCursor();

        this.screen = new MainMenuScreen(this);
        this.setScreen(this.screen);
    }

    @Override
    public void dispose() {
        this.screen.dispose();
        super.dispose();
    }
}



