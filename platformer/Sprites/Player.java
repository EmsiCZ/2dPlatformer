package com.olszar.platformer.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Screens.PlayScreen;

/**
 * Created by lubos on 17.11.2016.
 */
public class Player extends Sprite {
    public enum State { JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerStand;
    private Animation playerRun;
    private TextureRegion playerJump;
    private float stateTimer;
    private boolean runningRight;

    public Player(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("p3_spritesheet"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(getTexture(), (i * 72) + (i * 1), 0, 72, 97));
        }
        for (int i = 0; i < 3; i++){
            frames.add(new TextureRegion(getTexture(), (i * 72) + (i * 1), 98, 72, 97));
        }
        frames.add(new TextureRegion(getTexture(), 219, 0, 72, 97));
        frames.add(new TextureRegion(getTexture(), 292, 0, 72, 97));
        frames.add(new TextureRegion(getTexture(), 219, 98, 72, 97));
        frames.add(new TextureRegion(getTexture(), 365, 0, 72, 97));
        frames.add(new TextureRegion(getTexture(), 292, 98, 72, 97));

        playerRun = new Animation(0.04f, frames);
        frames.clear();

        playerJump = new TextureRegion(getTexture(), 438, 93, 67, 94);
        playerStand = new TextureRegion(getTexture(), 67, 196, 66, 92);

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
        if(b2body.getLinearVelocity().y != 0)
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
        fdef.filter.maskBits = PlatformerGame.DEFAULT_BIT | PlatformerGame.COINBRICK_BIT | PlatformerGame.BRICK_BIT;
        fdef.filter.groupIndex = PlatformerGame.DESTROYED_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-25 / PlatformerGame.PPM, 60 / PlatformerGame.PPM), new Vector2(25 / PlatformerGame.PPM, 60 / PlatformerGame.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }
}
