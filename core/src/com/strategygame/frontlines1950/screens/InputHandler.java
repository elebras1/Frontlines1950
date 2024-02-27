package com.strategygame.frontlines1950.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.strategygame.frontlines1950.map.World;

import java.util.List;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

public class InputHandler<T extends Screen> implements InputProcessor {
    final OrthographicCamera cam;
    final World world;
    private float delta = 0;
    final T screen;
    private final int edgeSize = 50;

    public InputHandler(OrthographicCamera cam, World world, T screen) {

        this.cam = cam;
        this.world = world;
        this.screen = screen;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public void handleInput(List<Table> tables) {
        float speed = 1000f * this.delta;
        int screenX = Gdx.input.getX();
        int screenY = Gdx.input.getY();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        for(Table table : tables) {
            if (table == null) {
                continue;
            }
            Vector2 tablePos = table.localToStageCoordinates(new Vector2(0, 0));
            Vector2 tableDim = new Vector2(table.getWidth(), table.getHeight());
            Vector2 tableOver = new Vector2(screenX, screenHeight - screenY);
            if (tablePos.x < tableOver.x && tableOver.x < tablePos.x + tableDim.x && tablePos.y < tableOver.y && tableOver.y < tablePos.y + tableDim.y) {
                return;
            }
        }

        if (screenX < edgeSize) {
            cam.translate(-speed * cam.zoom, 0, 0);
        } else if (screenX > screenWidth - edgeSize) {
            cam.translate(speed * cam.zoom, 0, 0);
        }

        if (screenY < edgeSize) {
            cam.translate(0, speed * cam.zoom, 0);
        } else if (screenY > screenHeight - edgeSize) {
            cam.translate(0, -speed * cam.zoom, 0);
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, WORLD_WIDTH / cam.viewportWidth);
    }

    public void handleInput() {
        float speed = 1000f * this.delta;
        int screenX = Gdx.input.getX();
        int screenY = Gdx.input.getY();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if (screenX < edgeSize) {
            cam.translate(-speed * cam.zoom, 0, 0);
        } else if (screenX > screenWidth - edgeSize) {
            cam.translate(speed * cam.zoom, 0, 0);
        }

        if (screenY < edgeSize) {
            cam.translate(0, speed * cam.zoom, 0);
        } else if (screenY > screenHeight - edgeSize) {
            cam.translate(0, -speed * cam.zoom, 0);
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, WORLD_WIDTH / cam.viewportWidth);
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

        if ((cam.zoom + zoom) >= 0.00001f && (cam.zoom + zoom) <= WORLD_WIDTH / cam.viewportWidth) {
            cam.zoom += zoom;
        }

        return true;
    }

}
