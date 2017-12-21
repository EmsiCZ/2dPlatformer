package com.olszar.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Scenes.Controller;
import com.olszar.platformer.Scenes.Hud;
import com.olszar.platformer.Sprites.Enemy;
import com.olszar.platformer.Sprites.Player;
import com.olszar.platformer.Sprites.Slime;
import com.olszar.platformer.Tools.B2WorldCreator;
import com.olszar.platformer.Tools.WorldContactListener;

import sun.rmi.runtime.Log;

/**
 * Created by lubos on 15.11.2016.
 */
public class PlayScreen implements Screen{

    private PlatformerGame game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Texture background;
    private Hud hud;
    private Controller controller;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Player player;

    private Music music;

    public PlayScreen(PlatformerGame game) {
        atlas = new TextureAtlas("animations2.pack");

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(PlatformerGame.V_WIDTH / PlatformerGame.PPM, PlatformerGame.V_HEIGHT / PlatformerGame.PPM, gamecam);
        hud = new Hud(game.batch);
        controller = new Controller(game.batch, hud.stage);

        background = new Texture("Backgrounds/blue_grass.png");

        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PlatformerGame.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Player(this);

        //cervene okraje
        b2dr.SHAPE_STATIC.set(1,0,0,1);

        world.setContactListener(new WorldContactListener());

        music = PlatformerGame.manager.get("audio/music/Grasslands Theme.mp3", Music.class);
        music.setLooping(true);
        music.play();

        //slime = new Slime(this, 21f, 6f);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){

        //player.b2body.applyForceToCenter(1.0f, 0.0f, true);
        if(player.currentState != Player.State.DEAD){
            if(controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 4)
                player.b2body.applyLinearImpulse(new Vector2(0.15f, 0), player.b2body.getWorldCenter(), true);
            else if(controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -4)
                player.b2body.applyLinearImpulse(new Vector2(-0.15f, 0), player.b2body.getWorldCenter(), true);
            else
                player.b2body.applyLinearImpulse(new Vector2(0, 0), player.b2body.getWorldCenter(), false);
            if(controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0){
                player.b2body.applyLinearImpulse(new Vector2(0, 9f), player.b2body.getWorldCenter(), true);
                PlatformerGame.manager.get("audio/sounds/jump.wav", Sound.class).play();
            }
        }


    }

    public void update(float dt){
        //user input
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);
        for (Enemy enemy: creator.getSlimes())
            enemy.update(dt);

        hud.setLifes(player.getLifes());
        hud.update(dt);

        if(player.currentState != Player.State.DEAD){
            gamecam.position.x = player.b2body.getPosition().x;
        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        //Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0, PlatformerGame.V_WIDTH, PlatformerGame.V_HEIGHT);
        game.batch.end();

        //Render game map
        renderer.render();

        //Render Box2DDebugLines
        //b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy: creator.getSlimes()){
            enemy.draw(game.batch);
            if(enemy.getX() < player.getX() + 900 / PlatformerGame.PPM)
                enemy.b2body.setActive(true);
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver() || hud.timeEnd()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        else if(winner()){
            game.setScreen(new WinnerScreen(game, hud.getFinalScore()));
            dispose();
        }

    }

    public boolean gameOver(){
        if(player.currentState == Player.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    public boolean winner(){
        if(player.currentState == Player.State.WON){
            return true;
        }
        return false;
    }


    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
