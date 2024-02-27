package com.strategygame.frontlines1950.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.strategygame.frontlines1950.map.Country;
import com.strategygame.frontlines1950.map.World;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_HEIGHT;
import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

public class NewGameScreen implements Screen {
    private final World world;
    private final Game game;
    private final OrthographicCamera cam;
    private final SpriteBatch batch;
    private final NewGameInputHandler<NewGameScreen> inputHandler;
    private Country selectedCountry;
    private Stage stage;
    private Skin skinUi;
    private Skin skinFlag;
    private Skin skinLeader;
    private Label label;
    private Image leader;
    private Image flag;

    public NewGameScreen(World world, Game game) {
        this.world = world;
        this.game = game;
        this.selectedCountry = null;
        this.cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.cam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        this.cam.update();
        this.batch = new SpriteBatch();
        this.inputHandler = new NewGameInputHandler<>(cam, world, this);
        this.initializeUi();
    }

    private void initializeUi() {
        this.stage = new Stage(new ScreenViewport());
        this.skinUi = new Skin(Gdx.files.internal("ui/newgame/skin/uiskin.json"));
        this.skinFlag = new Skin(Gdx.files.internal("images/flags/skin/flagskin.json"));
        this.skinLeader = new Skin(Gdx.files.internal("images/leaders/skin/leaderskin.json"));

        this.changeCursor();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.stage);
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

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
                world.deselectCountry();
                game.setScreen(new GameScreen(world, game, selectedCountry));
                dispose();
            }
        });
        choiceTable.add().spaceLeft(8);

        table.row();
        table.add(choiceTable);
        rootTable.add(table).expand().top().left();

        Image order = new Image(this.skinUi.getDrawable("frontend_selected_scenario"));
        order.setPosition((stage.getWidth() - order.getWidth()) / 2, stage.getHeight() - order.getHeight());
        stage.addActor(order);
    }


    public void setSelectedCountry(Country country) {

        this.selectedCountry = country;
        this.label.setText(this.selectedCountry.getId());
        this.flag.setDrawable(this.skinFlag.getDrawable(country.getId().toLowerCase()));
        try {
            this.leader.setDrawable(this.skinLeader.getDrawable(country.getId().toLowerCase()));
        } catch(GdxRuntimeException e) {
            this.leader.setDrawable(this.skinLeader.getDrawable("adm"));
        }
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
        this.world.render(this.batch);
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

    public void changeCursor() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("ui/cursor/normal.png"));
        int xHotspot = 0, yHotspot = 0;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose();
        Gdx.graphics.setCursor(cursor);
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
        this.skinUi.dispose();
        this.skinLeader.dispose();
        this.skinFlag.dispose();
        this.stage.dispose();
    }
}
