package com.strategygame.frontlines1950.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.strategygame.frontlines1950.map.World;
import com.strategygame.frontlines1950.ui.AnimatedCursor;

import java.util.concurrent.CompletableFuture;

public class LoadScreen implements Screen {
    private final SpriteBatch batch;
    private Image image;
    private final AnimatedCursor animatedCursor;

    public LoadScreen(Game game) {
        this.batch = new SpriteBatch();
        this.animatedCursor = new AnimatedCursor("busy");
        this.showImage();
        CompletableFuture.runAsync(() -> {
            Gdx.app.postRunnable(() -> {
                World world = new World();
                game.setScreen(new NewGameScreen(world, game));
                this.dispose();
            });
        });
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.begin();
        this.image.draw(batch, 1);
        this.batch.end();

        this.animatedCursor.update(Gdx.graphics.getDeltaTime());
    }

    private void showImage() {
        this.image = new Image(new Texture(Gdx.files.internal("loadingscreens/load_1.png")));
    }

    @Override
    public void resize(int width, int height) {
        this.image.setSize(width, height);
        this.batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

