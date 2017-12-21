package com.olszar.platformer.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Screens.PlayScreen;

/**
 * Created by lubos on 21.12.2017.
 */

public class Goal extends InteractiveTileObject {
    public Goal(PlayScreen screen, Rectangle bounds){

        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformerGame.GOAL_BIT);
    }
    @Override
    public void onHeadHit() {

    }
}
