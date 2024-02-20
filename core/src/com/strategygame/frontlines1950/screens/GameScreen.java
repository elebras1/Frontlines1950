package com.strategygame.frontlines1950;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_HEIGHT;
import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

class GameScreen implements Screen {

    private final World world;
    private final Game game;
    private final OrthographicCamera cam;
    private final SpriteBatch batch;
    private final InputHandler<GameScreen> inputHandler;
    private final PlayerManager playerManager;
    private Stage stage;
    private Skin skinTopbar;
    private Skin skinFlag;
    private Skin skinLeader;
    private int timeSpeed = 3;
    private Image speed;

    private Label fpsLabel;

    public GameScreen(World world, Game game, Country playerCountry) {
        this.world = world;
        this.game = game;
        this.cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.cam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        this.cam.update();
        this.batch = new SpriteBatch();
        this.inputHandler = new InputHandler<>(this.cam, world, this);
        Gdx.input.setInputProcessor(this.inputHandler);
        this.playerManager = new PlayerManager();
        Player player = new Player(playerCountry);
        this.playerManager.addPlayer(player);
        for(Country country : this.world.getCountries()) {
            if(!country.equals(playerCountry)) {
                this.playerManager.addPlayer(new PlayerAi(country));
            }
        }
        this.initializedTopbar();
    }

    private void initializedTopbar() {
        this.stage = new Stage(new ScreenViewport());
        this.skinTopbar = new Skin(Gdx.files.internal("ui/topbar/skin/topbarskin.json"));
        this.skinFlag = new Skin(Gdx.files.internal("images/flags/skin/flagskin.json"));
        this.skinLeader = new Skin(Gdx.files.internal("images/leaders/skin/leaderskin.json"));

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.stage);
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
        // UI table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        this.stage.addActor(rootTable);
        // Topbar table
        Table topbarTable = new Table();
        Drawable background = this.skinTopbar.getDrawable("naked_topbar");
        topbarTable.setBackground(background);
        rootTable.add(topbarTable).expand().left().top();

        initializeFirstRow(topbarTable);
        topbarTable.row();
        initializeSecondRow(topbarTable);

        this.fpsLabel = new Label("", this.skinTopbar, "green");
        this.fpsLabel.setPosition(stage.getWidth() - 60, stage.getHeight() - 20);
        this.stage.addActor(this.fpsLabel);
    }

    private void initializeFirstRow(Table topbarTable) {
        // the first row of the topbar table
        Table firstRowTable = new Table();
        topbarTable.add(firstRowTable).expandX();
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
    }

    private void initializeSecondRow(Table topbarTable) {
        // the second row of the topbar table
        Table secondRowTable = new Table();
        topbarTable.add(secondRowTable).expandX().left();
        secondRowTable.padLeft(10);
        Table playPauseTable = new Table();
        secondRowTable.add(playPauseTable);
        playPauseTable.setBackground(this.skinTopbar.getDrawable("play_pause"));
        Image flag = new Image(this.skinFlag.getDrawable(this.playerManager.getHumanPlayer().getCountry().getId().toLowerCase()));
        playPauseTable.add(flag).padRight(2);
        Image menu = new Image(this.skinTopbar.getDrawable("menu_topbar"));
        playPauseTable.add(menu).padRight(2);
        Image stats = new Image(this.skinTopbar.getDrawable("stats_topbar"));
        playPauseTable.add(stats).padRight(2);
        this.speed = new Image(this.skinTopbar.getDrawable("defcon_buttons_" + this.timeSpeed));
        playPauseTable.add(this.speed).padRight(5);
        Table speedTable = new Table();
        playPauseTable.add(speedTable);
        Button plus = new Button(this.skinTopbar,"plus_speed");
        plus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(timeSpeed < 5) {
                    timeSpeed++;
                    speed.setDrawable(skinTopbar.getDrawable("defcon_buttons_" + timeSpeed));
                }
            }
        });
        speedTable.add(plus);
        speedTable.row();
        Button minus = new Button(this.skinTopbar,"minus_speed");
        minus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(timeSpeed > 0) {
                    timeSpeed--;
                    speed.setDrawable(skinTopbar.getDrawable("defcon_buttons_" + timeSpeed));
                }
            }
        });
        speedTable.add(minus);
        CheckBox speedToggle = new CheckBox("", this.skinTopbar);
        playPauseTable.add(speedToggle);
        Label date = new Label("1950.01.01", this.skinTopbar, "default");
        playPauseTable.add(date).expandX().center();
        Image leader = new Image(this.skinLeader.getDrawable(this.playerManager.getHumanPlayer().getCountry().getId().toLowerCase()));
        secondRowTable.add(leader);
        Image prestige = new Image(this.skinTopbar.getDrawable("topbar_prestige"));
        secondRowTable.add(prestige);
    }

    @Override
    public void show() {
    }

    public void render(float delta) {
        this.cam.update();
        this.batch.setProjectionMatrix(this.cam.combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        float camX = this.cam.position.x;
        camX = (camX + WORLD_WIDTH) % WORLD_WIDTH;

        this.cam.position.x = camX;

        this.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        this.batch.begin();
        this.world.render(this.batch);
        this.batch.end();

        this.inputHandler.handleInput();

        this.fpsLabel.setText(Gdx.graphics.getFramesPerSecond());

        this.stage.act();
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.cam.viewportWidth = width / 2f;
        this.cam.viewportHeight = height / 2f;
        this.cam.update();
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
        this.world.dispose();
        this.batch.dispose();

    }
}