package com.strategygame.frontlines1950.screens;

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
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.strategygame.frontlines1950.player.Player;
import com.strategygame.frontlines1950.player.PlayerAi;
import com.strategygame.frontlines1950.player.PlayerManager;
import com.strategygame.frontlines1950.map.Country;
import com.strategygame.frontlines1950.map.World;
import com.strategygame.frontlines1950.ui.ActionSelectorUi;
import com.strategygame.frontlines1950.ui.CursorChanger;
import com.strategygame.frontlines1950.ui.TopbarUi;
import com.strategygame.frontlines1950.utils.Debug;

import java.util.ArrayList;
import java.util.List;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_HEIGHT;
import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

public class GameScreen implements Screen {

    private final World world;
    private final Game game;
    private final OrthographicCamera cam;
    private final SpriteBatch batch;
    private final InputHandler<GameScreen> inputHandler;
    private final PlayerManager playerManager;
    private Stage stage;
    private int timeSpeed = 3;
    private CursorChanger cursorChanger;
    private TopbarUi topbarUi;
    private ActionSelectorUi actionSelectorUi;
    private Debug debug;

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
        this.topbarUi = new TopbarUi(this);
        this.actionSelectorUi = new ActionSelectorUi(this);
        this.debug = new Debug();
        this.initializeUi();
        this.cursorChanger = new CursorChanger();
        this.cursorChanger.defaultCursor();
    }

    private void initializeUi() {
        this.stage = new Stage(new ScreenViewport());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.stage);
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
        // UI table
        Table rootTable = new Table();
        rootTable.padRight(10).padBottom(10);
        rootTable.setFillParent(true);
        this.stage.addActor(rootTable);

        // Topbar table
        Table topbarTable = this.topbarUi.create();
        rootTable.add(topbarTable).expand().left().top();

        Table actionTable = this.actionSelectorUi.create();
        rootTable.add(actionTable).bottom();

        // Debug labels
        for(Label label : this.debug.firstConfiguration().values()) {
            this.stage.addActor(label);
        }
    }

    public int getTimeSpeed() {
        return this.timeSpeed;
    }

    public void upTimeSpeed() {
        if(this.timeSpeed < 5) {
            this.timeSpeed = 5;
        }
    }

    public void downTimeSpeed() {
        if (this.timeSpeed > 0) {
            this.timeSpeed = 0;
        }
    }

    public CursorChanger getCursorChanger() {
        return this.cursorChanger;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public void show() {
    }

    public void render(float delta) {
        this.cam.update();
        this.batch.setProjectionMatrix(this.cam.combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(this.cursorChanger.getAnimatedCursor() != null) {
            this.cursorChanger.getAnimatedCursor().update(delta);
        }

        float camX = this.cam.position.x;
        camX = (camX + WORLD_WIDTH) % WORLD_WIDTH;

        this.cam.position.x = camX;

        this.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        this.batch.begin();
        this.world.render(this.batch, this.inputHandler.getCamera().zoom);
        this.batch.end();

        this.inputHandler.setDelta(delta);
        List<Table> tables = new ArrayList<>();
        tables.add(this.topbarUi.getTable());
        tables.add(this.actionSelectorUi.getTable());
        this.inputHandler.handleInput(tables);

        this.debug.actualize();

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
        this.stage.dispose();
        this.cursorChanger.dispose();
        this.topbarUi.dispose();
        this.actionSelectorUi.dispose();
    }
}
