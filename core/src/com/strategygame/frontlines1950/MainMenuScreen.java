package com.strategygame.frontlines1950;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class MainMenuScreen implements Screen {
    private Stage stage;
    private Skin skin = new Skin();
    private Game game;
    private Texture backgroundTexture;
    private Texture menuTexture;

    public MainMenuScreen(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);

        backgroundTexture = new Texture("ui/mainmenu/background.png");
        Image background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(background);

        menuTexture = new Texture("ui/mainmenu/main_menu.png");
        Image menuImage = new Image(menuTexture);
        menuImage.setPosition((float) Gdx.graphics.getWidth() / 2 - menuImage.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - menuImage.getHeight() / 2);
        stage.addActor(menuImage);

        TextButton.TextButtonStyle textButtonStyle = createButtonStyle();
        skin.add("default", textButtonStyle);

        TextButton startButton = createButton("Start Game");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadScreen(game));
            }
        });

        TextButton exitButton = createButton("Exit Game");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Add buttons to a table
        Table table = createButtonTable(startButton, exitButton);
        this.stage.addActor(table);
    }

    private TextButton.TextButtonStyle createButtonStyle() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("ui/mainmenu/button_big.png")));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("ui/mainmenu/button_exit.png")));
        return textButtonStyle;
    }

    private TextButton createButton(String text) {
        return new TextButton(text, skin);
    }

    private Table createButtonTable(TextButton startButton, TextButton exitButton) {
        Table table = new Table();
        table.setFillParent(true);
        table.add(startButton).padTop(187);
        table.row();
        table.add(exitButton).padTop(177);
        return table;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);

        Image background = (Image) stage.getActors().first();
        background.setSize(width, height);

        Image menuImage = (Image) stage.getActors().get(1);
        menuImage.setPosition((float) width / 2 - menuImage.getWidth() / 2, (float) height / 2 - menuImage.getHeight() / 2);
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
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        menuTexture.dispose();
    }
}



