package com.olszar.platformer.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Sprites.Enemy;
import com.olszar.platformer.Sprites.InteractiveTileObject;
import com.olszar.platformer.Sprites.Player;
import com.olszar.platformer.Sprites.Slime;

/**
 * Created by lubos on 19.12.2017.
 */

public class WorldContactListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if(object.getUserData() instanceof InteractiveTileObject){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch(cDef){
            case PlatformerGame.ENEMY_HEAD_BIT | PlatformerGame.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == PlatformerGame.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                Gdx.app.log("Zaslapni", "Collision");
                break;
            case PlatformerGame.ENEMY_BIT | PlatformerGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == PlatformerGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PlatformerGame.PLAYER_BIT | PlatformerGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == PlatformerGame.PLAYER_BIT && !((Slime) fixB.getUserData()).isDestroyed())
                    ((Player)fixA.getUserData()).hit();
                else if(!((Slime) fixA.getUserData()).isDestroyed())
                    ((Player)fixB.getUserData()).hit();
                Gdx.app.log("Zemri", "Collision");
                break;
            case PlatformerGame.ENEMY_BIT | PlatformerGame.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PlatformerGame.PLAYER_BIT | PlatformerGame.GOAL_BIT:
                if(fixA.getFilterData().categoryBits == PlatformerGame.PLAYER_BIT)
                    ((Player)fixA.getUserData()).win();
                else
                    ((Player)fixB.getUserData()).win();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
