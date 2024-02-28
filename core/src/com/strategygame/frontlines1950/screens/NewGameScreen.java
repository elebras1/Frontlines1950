package com.strategygame.frontlines1950.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.strategygame.frontlines1950.map.Country;
import com.strategygame.frontlines1950.map.World;
import com.strategygame.frontlines1950.ui.CursorChanger;
import com.strategygame.frontlines1950.ui.NewGameUi;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_HEIGHT;
import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

public class NewGameScreen implements Screen {
    private final World world;
    private final Game game;
    private final OrthographicCamera cam;
    private final SpriteBatch batch;
    private final NewGameInputHandler<NewGameScreen> inputHandler;
    private final NewGameUi newGameUi;
    private Country selectedCountry;
    private Stage stage;
    private CursorChanger cursorChanger;

    public NewGameScreen(World world, Game game) {
        this.world = world;
        this.game = game;
        this.selectedCountry = null;
        this.cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.cam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        this.cam.update();
        this.batch = new SpriteBatch();
        this.inputHandler = new NewGameInputHandler<>(cam, world, this);
        this.newGameUi = new NewGameUi(this);
        this.initializeUi();
    }

    private void initializeUi() {
        this.stage = new Stage(new ScreenViewport());

        this.cursorChanger = new CursorChanger();
        this.cursorChanger.defaultCursor();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.stage);
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        this.stage.addActor(rootTable);

        Table selectedScenarioTable = this.newGameUi.createSelectedScenarioTable();
        rootTable.add(selectedScenarioTable).expand().top().left();

        Image orderScenarioImage = this.newGameUi.createOrderScenarioImage();
        orderScenarioImage.setPosition((stage.getWidth() - orderScenarioImage.getWidth()) / 2, stage.getHeight() - orderScenarioImage.getHeight());
        stage.addActor(orderScenarioImage);
    }

    public World getWorld() {
        return this.world;
    }

    public Game getGame() {
        return this.game;
    }

    public void setSelectedCountry(Country country) {

        this.selectedCountry = country;
        this.newGameUi.setLabel(this.selectedCountry.getId());
        this.newGameUi.setFlag(country.getId().toLowerCase());
        try {
            this.newGameUi.setLeader(country.getId().toLowerCase());
        } catch(GdxRuntimeException e) {
            this.newGameUi.setLeaderDefault();
        }
    }

    public Country getSelectedCountry() {
        return this.selectedCountry;
    }

    @Override
    public void show() {

    }

    @Override
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
        this.world.render(this.batch, this.inputHandler.getCamera().zoom);
        this.batch.end();

        this.inputHandler.setDelta(delta);
        this.inputHandler.handleInput();

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
        this.stage.dispose();
        this.cursorChanger.dispose();
    }
}
