package com.strategygame.frontlines1950;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import java.util.concurrent.CompletableFuture;

public class LoadScreen implements Screen {
    private SpriteBatch batch;
    private List<String> loadImages;
    private Image currentImage;
    private Game game;

    public LoadScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.loadImages = new ArrayList<>();
        this.loadImages.addAll(Arrays.asList("load_1.png", "load_2.png", "load_3.png", "load_4.png", "load_5.png", "load_6.png", "load_7.png", "load_8.png", "load_9.png"));

        CompletableFuture.runAsync(() -> {

            Gdx.app.postRunnable(() -> {
                World world = new World();
                this.dispose();
                game.setScreen(new NewGameScreen(world, game));
            });
        });
    }

    @Override
    public void show() {
        showRandomImage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        currentImage.draw(batch, 1);
        batch.end();
    }

    private void showRandomImage() {
        Random rand = new Random();
        String imageName = loadImages.get(rand.nextInt(loadImages.size()));
        this.currentImage = new Image(new Texture(Gdx.files.internal("loadingscreens/" + imageName)));
    }

    @Override
    public void resize(int width, int height) {
        currentImage.setSize(width, height);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
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

