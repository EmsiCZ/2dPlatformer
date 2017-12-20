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
import com.olszar.platformer.Sprites.Player;
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

    //sprites
    private Player player;

    private Music music;

    public PlayScreen(PlatformerGame game) {
        atlas = new TextureAtlas("Player.pack");

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

        new B2WorldCreator(world, map);

        player = new Player(world, this);

        //cervene okraje
        b2dr.SHAPE_STATIC.set(1,0,0,1);

        world.setContactListener(new WorldContactListener());

        music = PlatformerGame.manager.get("audio/music/Grasslands Theme.mp3", Music.class);
        music.setLooping(true);
        music.play();
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        /*Vector3 touchPoint = new Vector3();
        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            guicam.unproject(touchPoint.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            if(leftControl.contains(touchPoint.x, touchPoint.y) && player.b2body.getLinearVelocity().x >= -2){
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            else if(rightControl.contains(touchPoint.x, touchPoint.y) && player.b2body.getLinearVelocity().x <= 2){
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            else if(upControl.contains(touchPoint.x, touchPoint.y)){
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            }
        }*/
        player.b2body.applyForceToCenter(1.0f, 0.0f, true);
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

    public void update(float dt){
        //user input
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);
        hud.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;

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
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
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
