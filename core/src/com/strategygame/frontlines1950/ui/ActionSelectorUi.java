package com.strategygame.frontlines1950.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.strategygame.frontlines1950.screens.GameScreen;

public class ActionSelectorUi {
    private final GameScreen gameScreen;
    private final Skin skinAction;
    private Table table;

    public ActionSelectorUi(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.skinAction = new Skin(Gdx.files.internal("ui/action/skin/actionskin.json"));
    }

    public Table create() {
        this.table = new Table();
        this.table.padLeft(15).padRight(12);
        this.table.setBackground(this.skinAction.getDrawable("action_bar"));
        Button attack = new Button(this.skinAction, "unit_attack");
        attack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.getCursorChanger().animatedCursor("attack");
            }
        });
        this.table.add(attack);
        Button support = new Button(this.skinAction, "unit_support");
        support.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.getCursorChanger().animatedCursor("support");
            }
        });
        this.table.add(support).expandX().left();
        Button returnAction = new Button(this.skinAction, "return");
        returnAction.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.getCursorChanger().defaultCursor();
            }
        });
        this.table.add(returnAction);
        Button confirm = new Button(this.skinAction, "confirm");
        this.table.add(confirm);

        return this.table;
    }

    public Table getTable() {
        return this.table;
    }

    public void dispose() {
        this.skinAction.dispose();
    }
}
