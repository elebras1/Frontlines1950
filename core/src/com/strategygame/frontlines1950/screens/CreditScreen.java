package com.strategygame.frontlines1950.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Arrays;
import java.util.List;

public class CreditScreen implements Screen {
    private static final List<String> credits = Arrays.asList("Game Design, Development, and Artwork by: \n Le Bras Erwan \n\nGitHub repository: \n https://github.com/elebras1/Frontlines1950", "Special Thanks to: \n Paradox Interactive for the assets from the cancelled game East vs West: A Hearts of Iron Game.", "Quote: \n It is, of course, much easier to shout, abuse, and howl than to attempt to relate, to explain.\n -Lenin");
    private final Stage stage;
    private int creditIndex = 0;

    public CreditScreen(Game game) {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);
        Skin skinMessage = new Skin(Gdx.files.internal("ui/message/skin/messageskin.json"));
        Skin skinMainmenu = new Skin(Gdx.files.internal("ui/mainmenu/skin/mainmenuskin.json"));
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(skinMainmenu.getDrawable("frontend_main_bg"));
        Table messageTable = new Table();
        messageTable.padLeft(30).padRight(30).padTop(100).padBottom(7);
        messageTable.setBackground(skinMessage.getDrawable("message_naked_bgl"));
        Label credit = new Label(credits.get(0), skinMessage);
        credit.setWrap(true);
        messageTable.add(credit).expandX().fill().left();

        messageTable.row();
        Table buttonTable = new Table();
        messageTable.add(buttonTable).expand().bottom();
        TextButton exitButton = new TextButton("Exit", skinMessage, "exit");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        buttonTable.add(exitButton);
        TextButton nextButton = new TextButton("Next", skinMessage, "next");
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                creditIndex = (creditIndex + 1) % credits.size(); // Mettez à jour l'index de crédit
                credit.setText(credits.get(creditIndex)); // Mettez à jour le texte du label de crédit
            }
        });
        buttonTable.add(nextButton);
        rootTable.add(messageTable).expand().center();
        this.stage.addActor(rootTable);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act();
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
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

    }
}
