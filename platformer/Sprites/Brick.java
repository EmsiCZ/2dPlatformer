package com.olszar.platformer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Screens.PlayScreen;

/**
 * Created by lubos on 27.11.2016.
 */
public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds){

        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformerGame.COINBRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        if(fixture.getFilterData().categoryBits == PlatformerGame.COINBRICK_BIT){
            PlatformerGame.manager.get("audio/sounds/brick.ogg", Sound.class).play();
        }
        setCategoryFilter(PlatformerGame.DESTROYED_BIT);
        getCell().setTile(null);
    }
}
