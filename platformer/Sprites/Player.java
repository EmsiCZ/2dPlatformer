package com.olszar.platformer.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Screens.PlayScreen;

/**
 * Created by lubos on 17.11.2016.
 */
public class Player extends Sprite {
    public enum State { JUMPING, STANDING, RUNNING, DEAD, WON };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerStand;
    private Animation playerRun;
    private TextureRegion playerJump;
    private TextureRegion playerDead;
    private float stateTimer;
    private boolean runningRight;
    private boolean playerIsDead;
    private boolean playerWon;

    public Player(PlayScreen screen){
        super(screen.getAtlas().findRegion("player_spritesheet"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(getTexture(), (i * 72) + (i * 1), 156, 72, 97));
        }
        for (int i = 0; i < 3; i++){
            frames.add(new TextureRegion(getTexture(), (i * 72) + (i * 1), 254, 72, 97));
        }
        frames.add(new TextureRegion(getTexture(), 219, 156, 72, 97));
        frames.add(new TextureRegion(getTexture(), 292, 156, 72, 97));
        frames.add(new TextureRegion(getTexture(), 219, 254, 72, 97));
        frames.add(new TextureRegion(getTexture(), 365, 156, 72, 97));
        frames.add(new TextureRegion(getTexture(), 292, 254, 72, 97));

        playerRun = new Animation(0.04f, frames);
        frames.clear();

        playerJump = new TextureRegion(getTexture(), 438, 249, 67, 94);
        playerStand = new TextureRegion(getTexture(), 67, 352, 66, 92);
        playerDead = new TextureRegion(getTexture(), 438, 156, 69, 92);

        definePlayer();
        setBounds(0, 0, 128 / PlatformerGame.PPM, 128 / PlatformerGame.PPM);
        setRegion(playerStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case DEAD:
                region = playerDead;
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                region = playerJump;
                break;
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if(playerIsDead)
            return State.DEAD;
        else if(playerWon)
            return State.WON;
        else if(b2body.getLinearVelocity().y != 0)
            return  State.JUMPING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(64 / PlatformerGame.PPM, 500 / PlatformerGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(60 / PlatformerGame.PPM);
        fdef.filter.categoryBits = PlatformerGame.PLAYER_BIT;
        fdef.filter.maskBits = PlatformerGame.GROUND_BIT |
                PlatformerGame.COINBRICK_BIT |
                PlatformerGame.BRICK_BIT |
                PlatformerGame.OBJECT_BIT |
                PlatformerGame.ENEMY_BIT |
                PlatformerGame.ENEMY_HEAD_BIT |
                PlatformerGame.GOAL_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-25 / PlatformerGame.PPM, 60 / PlatformerGame.PPM), new Vector2(25 / PlatformerGame.PPM, 60 / PlatformerGame.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }

    public boolean isDead(){
        return playerIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void hit(){
        PlatformerGame.manager.get("audio/music/Grasslands Theme.mp3", Music.class).stop();
        PlatformerGame.manager.get("audio/sounds/hit.m4a", Sound.class).play();
        playerIsDead = true;
        Filter filter = new Filter();
        filter.maskBits = PlatformerGame.NOTHING_BIT;
        for(Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
    }

    public void win(){
        PlatformerGame.manager.get("audio/music/Grasslands Theme.mp3", Music.class).stop();
        playerWon = true;
    }
}
