package com.olszar.platformer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Scenes.Hud;

/**
 * Created by lubos on 27.11.2016.
 */
public class CoinBrick extends InteractiveTileObject {
    public CoinBrick(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformerGame.COINBRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        if(fixture.getFilterData().categoryBits == PlatformerGame.COINBRICK_BIT){
            PlatformerGame.manager.get("audio/sounds/pickup_coin.m4a", Sound.class).play();
        }
        setCategoryFilter(PlatformerGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(1);
    }
}
