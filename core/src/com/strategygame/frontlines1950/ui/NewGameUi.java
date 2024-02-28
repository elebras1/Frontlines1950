package com.strategygame.frontlines1950.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.strategygame.frontlines1950.screens.GameScreen;
import com.strategygame.frontlines1950.screens.NewGameScreen;

public class NewGameUi {
    private final NewGameScreen newGameScreen;
    private final Skin skinUi;
    private final Skin skinFlag;
    private final Skin skinLeader;
    private Image flag;
    private Label label;
    private Image leader;

    public NewGameUi(NewGameScreen newGameScreen) {
        this.skinUi = new Skin(Gdx.files.internal("ui/newgame/skin/uiskin.json"));
        this.skinFlag = new Skin(Gdx.files.internal("images/flags/skin/flagskin.json"));
        this.skinLeader = new Skin(Gdx.files.internal("images/leaders/skin/leaderskin.json"));
        this.newGameScreen = newGameScreen;
    }

    public Table createSelectedScenarioTable() {
        Table table = new Table();
        Image scenario = new Image(this.skinUi.getDrawable("frontend_scenario_entry"));
        table.add(scenario);

        Table choiceTable = new Table();
        Drawable background = this.skinUi.getDrawable("frontend_selected_bg");
        choiceTable.setBackground(background);
        choiceTable.padLeft(9).padBottom(4);
        this.flag = new Image(this.skinFlag.getDrawable("def"));
        choiceTable.add(this.flag).expandY().bottom().spaceBottom(7);
        this.label = new Label("XXX", this.skinUi);
        choiceTable.add(this.label).expandY().left().bottom().spaceBottom(22).spaceLeft(5);
        choiceTable.row();
        this.leader = new Image(this.skinLeader.getDrawable("def"));
        choiceTable.add(this.leader).expandX().left();
        TextButton playButton = new TextButton("Play", this.skinUi);
        choiceTable.add(playButton);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newGameScreen.getWorld().deselectCountry();
                newGameScreen.getGame().setScreen(new GameScreen(newGameScreen.getWorld(), newGameScreen.getGame(), newGameScreen.getSelectedCountry()));
                newGameScreen.dispose();
                dispose();
            }
        });
        choiceTable.add().spaceLeft(8);

        table.row();
        table.add(choiceTable);

        return table;
    }

    public Image createOrderScenarioImage() {
        return new Image(this.skinUi.getDrawable("frontend_selected_scenario"));
    }

    public void setLabel(String id) {
        this.label.setText(id);
    }

    public void setFlag(String id) {
        System.out.println("Setting flag to " + id);
        this.flag.setDrawable(this.skinFlag.getDrawable(id));
    }

    public void setLeader(String id) {
        this.leader.setDrawable(this.skinLeader.getDrawable(id));
    }

    public void setLeaderDefault() {
        this.leader.setDrawable(this.skinLeader.getDrawable("adm"));
    }

    public void dispose() {
        this.skinUi.dispose();
        this.skinLeader.dispose();
        this.skinFlag.dispose();
    }
}
