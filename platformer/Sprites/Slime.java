package com.olszar.platformer.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Screens.PlayScreen;

/**
 * Created by lubos on 20.12.2017.
 */

public class Slime extends Enemy {
    public enum State { WALKING, DEAD };
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean walkingRight;

    public Slime(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemies_spritesheet"), 52, 125, 50, 28));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemies_spritesheet"), 0, 125, 51, 26));

        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 100 / PlatformerGame.PPM, 54 / PlatformerGame.PPM);
        setToDestroy = false;
        destroyed = false;
        walkingRight = true;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("slime-dead"), 0, 0, 51, 26));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 8);

            TextureRegion region = walkAnimation.getKeyFrame(stateTime, true);
            if ((b2body.getLinearVelocity().x > 0 || walkingRight) && !region.isFlipX()) {
                region.flip(true, false);
                walkingRight = true;
            } else if ((b2body.getLinearVelocity().x < 0 || !walkingRight) && region.isFlipX()) {
                region.flip(true, false);
                walkingRight = false;
            }
            setRegion(region);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY() /*500 / PlatformerGame.PPM, 500 / PlatformerGame.PPM*/);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape body = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-25, 45).scl(1 / PlatformerGame.PPM);
        vertice[1] = new Vector2(25, 45).scl(1 / PlatformerGame.PPM);
        vertice[2] = new Vector2(-45, 0).scl(1 / PlatformerGame.PPM);
        vertice[3] = new Vector2(45, 0).scl(1 / PlatformerGame.PPM);
        body.set(vertice);


        /*CircleShape shape = new CircleShape();
        shape.setRadius(27 / PlatformerGame.PPM);*/
        fdef.filter.categoryBits = PlatformerGame.ENEMY_BIT;
        fdef.filter.maskBits = PlatformerGame.GROUND_BIT |
                PlatformerGame.COINBRICK_BIT |
                PlatformerGame.BRICK_BIT |
                PlatformerGame.ENEMY_BIT |
                PlatformerGame.OBJECT_BIT |
                PlatformerGame.PLAYER_BIT;

        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);

        //Head of Slime
        PolygonShape head =  new PolygonShape();
        Vector2[] vertice2 = new Vector2[4];
        vertice2[0] = new Vector2(-28, 48).scl(1 / PlatformerGame.PPM);
        vertice2[1] = new Vector2(28, 48).scl(1 / PlatformerGame.PPM);
        vertice2[2] = new Vector2(-15, 20).scl(1 / PlatformerGame.PPM);
        vertice2[3] = new Vector2(15, 20).scl(1 / PlatformerGame.PPM);
        head.set(vertice2);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = PlatformerGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        PlatformerGame.manager.get("audio/sounds/stomp.m4a", Sound.class).play();
    }

    public boolean isDestroyed(){
        return destroyed || setToDestroy;
    }
}
