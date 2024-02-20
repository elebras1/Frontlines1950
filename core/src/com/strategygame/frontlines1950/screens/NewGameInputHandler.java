package com.strategygame.frontlines1950;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class NewGameInputHandler<T extends Screen> extends InputHandler<T> {
    public NewGameInputHandler(OrthographicCamera cam, World world, T screen) {
        super(cam, world, screen);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            super.cam.unproject(worldCoordinates);
            int x = Math.round(worldCoordinates.x);
            int y = Math.round(worldCoordinates.y);
            Country country = super.world.selectCountry(x, y);
            if(country != null) {
                NewGameScreen newGameScreen = (NewGameScreen) super.screen;
                newGameScreen.setSelectedCountry(country);
            }

            return true;
        }

        return false;
    }
}
