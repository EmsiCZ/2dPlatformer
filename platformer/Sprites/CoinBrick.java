package com.olszar.platformer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.olszar.platformer.PlatformerGame;

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
        /*Gdx.app.log("Brick", "Collision");
        setCategoryFilter(PlatformerGame.DESTROYED_BIT);
        getCell().setTile(null);*/
    }
}
