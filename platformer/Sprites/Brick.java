package com.olszar.platformer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.olszar.platformer.PlatformerGame;

/**
 * Created by lubos on 27.11.2016.
 */
public class Brick extends InteractiveTileObject {
    public Brick(World world, TiledMap map, Rectangle bounds){

        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformerGame.COINBRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(PlatformerGame.DESTROYED_BIT);
        getCell().setTile(null);
    }
}
