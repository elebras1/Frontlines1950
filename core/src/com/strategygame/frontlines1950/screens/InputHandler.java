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
    /**
     * The camera used to move around the world.
     */
    final OrthographicCamera cam;
    /**
     * The world to interact with.
     */
    final World world;
    /**
     * The delta time.
     */
    private float delta = 0;
    /**
     * The screen to interact with.
     */
    final T screen;
    /**
     * The edge size of the screen.
     */
    private final int edgeSize = 50;

    public InputHandler(OrthographicCamera cam, World world, T screen) {

        this.cam = cam;
        this.world = world;
        this.screen = screen;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public OrthographicCamera getCamera() {
        return this.cam;
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

        if (screenX < this.edgeSize) {
            this.cam.translate(-speed * this.cam.zoom, 0, 0);
        } else if (screenX > screenWidth - this.edgeSize) {
            this.cam.translate(speed * this.cam.zoom, 0, 0);
        }

        if (screenY < this.edgeSize) {
            cam.translate(0, speed * this.cam.zoom, 0);
        } else if (screenY > screenHeight - this.edgeSize) {
            cam.translate(0, -speed * this.cam.zoom, 0);
        }

        this.cam.zoom = MathUtils.clamp(this.cam.zoom, 0f, WORLD_WIDTH / this.cam.viewportWidth);
    }

    public void handleInput() {
        float speed = 1000f * this.delta;
        int screenX = Gdx.input.getX();
        int screenY = Gdx.input.getY();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if (screenX < this.edgeSize) {
            this.cam.translate(-speed * this.cam.zoom, 0, 0);
        } else if (screenX > screenWidth - this.edgeSize) {
            this.cam.translate(speed * this.cam.zoom, 0, 0);
        }

        if (screenY < this.edgeSize) {
            this.cam.translate(0, speed * this.cam.zoom, 0);
        } else if (screenY > screenHeight - this.edgeSize) {
            this.cam.translate(0, -speed * this.cam.zoom, 0);
        }

        this.cam.zoom = MathUtils.clamp(this.cam.zoom, 0f, WORLD_WIDTH / this.cam.viewportWidth);
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
            this.cam.unproject(worldCoordinates);
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
        float speed2 = 0.05f;
        float zoom2 = amountY * speed2;

        if((this.cam.zoom + zoom2 > 0f) && (this.cam.zoom < 1f)) {
            this.cam.zoom += zoom2;
        }
        else if ((this.cam.zoom + zoom) <= WORLD_WIDTH / this.cam.viewportWidth && (this.cam.zoom + zoom > 0f)) {
            this.cam.zoom += zoom;
        }

        return true;
    }

}
