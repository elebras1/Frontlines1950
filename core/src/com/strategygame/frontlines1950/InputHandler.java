package com.strategygame.frontlines1950;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

public class InputHandler<T extends Screen> implements InputProcessor {
    final OrthographicCamera cam;
    final World world;
    final T screen;

    public InputHandler(OrthographicCamera cam, World world, T screen) {

        this.cam = cam;
        this.world = world;
        this.screen = screen;
    }
    public void handleInput() {
        float speed = 5f;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-speed * cam.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(speed * cam.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -speed * cam.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, speed * cam.zoom, 0);
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, WORLD_WIDTH / cam.viewportWidth);

        //float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        //float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        //cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, WORLD_HEIGHT - effectiveViewportHeight / 2f);
        //cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
    }

    @Override
    public boolean keyDown (int keycode) {
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped (char character) {
        return false;
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)  {
        return false;
    }


    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            cam.unproject(worldCoordinates);
            int x = Math.round(worldCoordinates.x);
            int y = Math.round(worldCoordinates.y);
            this.world.selectState(x, y);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchCancelled (int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float speed = 0.25f;
        float zoom = amountY * speed;

        if ((cam.zoom + zoom) >= 0.1f && (cam.zoom + zoom) <= WORLD_WIDTH / cam.viewportWidth) {
            cam.zoom += zoom;
        }

        return true;
    }

}
