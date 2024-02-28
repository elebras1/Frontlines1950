package com.strategygame.frontlines1950.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.strategygame.frontlines1950.screens.GameScreen;

public class TopbarUi {
    private final GameScreen gameScreen;
    private final Skin skinTopbar;
    private final Skin skinFlag;
    private final Skin skinLeader;
    private Table table;

    public TopbarUi(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.skinTopbar = new Skin(Gdx.files.internal("ui/topbar/skin/topbarskin.json"));
        this.skinFlag = new Skin(Gdx.files.internal("images/flags/skin/flagskin.json"));
        this.skinLeader = new Skin(Gdx.files.internal("images/leaders/skin/leaderskin.json"));
    }

    public Table create() {
        this.table = new Table();
        this.table.setBackground(this.skinTopbar.getDrawable("naked_topbar"));
        Table firstRowTable = this.initializeFirstRow();
        this.table.add(firstRowTable).expandX();
        this.table.row();
        Table secondRowTable = this.createSecondRow();
        this.table.add(secondRowTable).expandX().left();

        return this.table;
    }

    public Table getTable() {
        return this.table;
    }

    private Table initializeFirstRow() {
        // the first row of the topbar table
        Table firstRowTable = new Table();
        //firstRowTable.debug();
        firstRowTable.padLeft(25).padBottom(10);
        // the main data table of th first row
        Image manpowerIcon = new Image(this.skinTopbar.getDrawable("icon_manpower_small_blue"));
        firstRowTable.add(manpowerIcon);
        Label manpowerLabel = new Label("573.63M", this.skinTopbar, "blue");
        firstRowTable.add(manpowerLabel);
        Image manpowerArmyIcon = new Image(this.skinTopbar.getDrawable("icon_manpower_army_small_blue"));
        firstRowTable.add(manpowerArmyIcon);
        Label manpowerArmyLabel = new Label("3,30K", this.skinTopbar, "blue");
        firstRowTable.add(manpowerArmyLabel);
        Image gdpIcon  = new Image(this.skinTopbar.getDrawable("icon_gdp_small_blue"));
        firstRowTable.add(gdpIcon);
        Label dgpLabel = new Label("12.50K", this.skinTopbar, "blue");
        firstRowTable.add(dgpLabel);
        // the ressources data table of the first row
        Image moneyIcon = new Image(this.skinTopbar.getDrawable("icon_money_small_green"));
        firstRowTable.add(moneyIcon);
        Label moneyLabel = new Label("309K", this.skinTopbar, "green");
        firstRowTable.add(moneyLabel);
        Image suppliesIcon = new Image(this.skinTopbar.getDrawable("icon_supplies_small_red"));
        firstRowTable.add(suppliesIcon);
        Label suppliesLabel = new Label("13597", this.skinTopbar, "red");
        firstRowTable.add(suppliesLabel);
        Image fuelIcon = new Image(this.skinTopbar.getDrawable("icon_fuel_small_red"));
        firstRowTable.add(fuelIcon);
        Label fuelLabel = new Label("7775", this.skinTopbar, "red");
        firstRowTable.add(fuelLabel);
        // the prestige data table of the first row
        Image prestigeIcon = new Image(this.skinTopbar.getDrawable("icon_prestige_small_blue"));
        firstRowTable.add(prestigeIcon);
        Label prestigeLabel = new Label("1.5", this.skinTopbar, "blue");
        firstRowTable.add(prestigeLabel);
        // the uranium data table of the first row
        Image uraniumIcon = new Image(this.skinTopbar.getDrawable("icon_uranium_small_blue"));
        firstRowTable.add(uraniumIcon);
        Label uraniumLabel = new Label("0", this.skinTopbar, "blue");
        firstRowTable.add(uraniumLabel);
        // the stability data table of the first row
        Image dissentIcon = new Image(this.skinTopbar.getDrawable("icon_dissent_small_green"));
        firstRowTable.add(dissentIcon).expandX().right();
        Label dissentLabel = new Label("0.00", this.skinTopbar, "green");
        firstRowTable.add(dissentLabel);
        Image unityIcon = new Image(this.skinTopbar.getDrawable("icon_unity_small_green"));
        firstRowTable.add(unityIcon);
        Label unityLabel = new Label("100%", this.skinTopbar, "green");
        firstRowTable.add(unityLabel);

        return firstRowTable;
    }

    private Table createSecondRow() {
        // the second row of the topbar table
        Table secondRowTable = new Table();
        secondRowTable.padLeft(10);
        Table playPauseTable = new Table();
        secondRowTable.add(playPauseTable);
        playPauseTable.setBackground(this.skinTopbar.getDrawable("play_pause"));
        Image flag = new Image(this.skinFlag.getDrawable(this.gameScreen.getPlayerManager().getHumanPlayer().getCountry().getId().toLowerCase()));
        playPauseTable.add(flag).padRight(2);
        Image menu = new Image(this.skinTopbar.getDrawable("menu_topbar"));
        playPauseTable.add(menu).padRight(2);
        Image stats = new Image(this.skinTopbar.getDrawable("stats_topbar"));
        playPauseTable.add(stats).padRight(2);
        Image speed = new Image(this.skinTopbar.getDrawable("defcon_buttons_" + gameScreen.getTimeSpeed()));
        playPauseTable.add(speed).padRight(5);
        Table speedTable = new Table();
        playPauseTable.add(speedTable);
        Button plus = new Button(this.skinTopbar,"plus_speed");
        plus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.upTimeSpeed();
                speed.setDrawable(skinTopbar.getDrawable("defcon_buttons_" + gameScreen.getTimeSpeed()));
            }
        });
        speedTable.add(plus);
        speedTable.row();
        Button minus = new Button(this.skinTopbar,"minus_speed");
        minus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameScreen.getTimeSpeed() > 0) {
                    gameScreen.downTimeSpeed();
                    speed.setDrawable(skinTopbar.getDrawable("defcon_buttons_" + gameScreen.getTimeSpeed()));
                }
            }
        });
        speedTable.add(minus);
        CheckBox speedToggle = new CheckBox("", this.skinTopbar);
        playPauseTable.add(speedToggle);
        Label date = new Label("1950.01.01", this.skinTopbar, "default");
        playPauseTable.add(date).expandX().center();
        Image leader;
        try {
            leader = new Image(this.skinLeader.getDrawable(this.gameScreen.getPlayerManager().getHumanPlayer().getCountry().getId().toLowerCase()));
        } catch(GdxRuntimeException e) {
            leader = new Image(this.skinLeader.getDrawable("adm"));
        }
        secondRowTable.add(leader);
        Image prestige = new Image(this.skinTopbar.getDrawable("topbar_prestige"));
        secondRowTable.add(prestige);

        return secondRowTable;
    }

    public void dispose() {
        this.skinTopbar.dispose();
        this.skinFlag.dispose();
        this.skinLeader.dispose();
    }
}
