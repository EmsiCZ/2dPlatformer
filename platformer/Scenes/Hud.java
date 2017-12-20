package com.olszar.platformer.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.olszar.platformer.PlatformerGame;

/**
 * Created by lubos on 15.11.2016.
 */
public class Hud implements Disposable{
    //New camera, because world will be moving
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private static Integer lifes;

    Label countdownLabel;
    static Label scoreLabel;
    static Label lifeLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        lifes = 3;

        viewport = new FitViewport(PlatformerGame.V_WIDTH, PlatformerGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        scoreLabel =  new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        lifeLabel =  new Label(String.format("%03d", lifes), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        countdownLabel.setFontScale(3);
        scoreLabel.setFontScale(3);
        lifeLabel.setFontScale(3);

        Image heartImg = new Image(new Texture("Hud/hud_heartFull.png"));
        heartImg.setSize(53, 45);

        Image coinImg = new Image(new Texture("Hud/hud_coins.png"));
        coinImg.setSize(47, 47);

        table.add(heartImg).expandX().padTop(10);
        table.add(countdownLabel).expandX().padTop(10);
        table.add(coinImg).expandX().padTop(10);

        table.row();
        table.add(lifeLabel).expandX();
        table.add().expandX();
        table.add(scoreLabel).expandX();


        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;

        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%03d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
