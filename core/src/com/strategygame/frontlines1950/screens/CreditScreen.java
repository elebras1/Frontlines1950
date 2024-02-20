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

public class CreditScreen implements Screen {
    private final Stage stage;
    private final Skin skinMessage;
    private final Skin skinMainmenu;

    public CreditScreen(Game game) {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);
        this.skinMessage = new Skin(Gdx.files.internal("ui/message/skin/messageskin.json"));
        this.skinMainmenu = new Skin(Gdx.files.internal("ui/mainmenu/skin/mainmenuskin.json"));
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(this.skinMainmenu.getDrawable("frontend_main_bg"));
        Table messageTable = new Table();
        messageTable.padLeft(30).padRight(30).padTop(90).padBottom(7);
        messageTable.setBackground(this.skinMessage.getDrawable("message_naked_bgl"));
        Label credit = new Label("Game creator : Le Bras Erwan - 2024", this.skinMessage);
        messageTable.add(credit).expandX().left();
        messageTable.row();
        Label citation = new Label("Citation of Lenin : The oppressed are allowed once every few years to decide which particular representatives of the oppressing class shall represent and repress them in parliament.", this.skinMessage);
        citation.setWrap(true);
        messageTable.add(citation).expandX().fill().left().spaceTop(5);

        messageTable.row();
        Table buttonTable = new Table();
        messageTable.add(buttonTable).expand().bottom();
        TextButton exitButton = new TextButton("Exit", this.skinMessage, "exit");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        buttonTable.add(exitButton);
        TextButton nextButton = new TextButton("Next", this.skinMessage, "next");
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
