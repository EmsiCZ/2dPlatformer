package com.olszar.platformer.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Screens.PlayScreen;

/**
 * Created by lubos on 17.11.2016.
 */
public class Controller {
    Viewport viewport;
    boolean upPressed, rightPressed, leftPressed;
    OrthographicCamera cam;

    public Controller(SpriteBatch sb, Stage stage){
        cam = new OrthographicCamera();
        viewport = new FitViewport(PlatformerGame.V_WIDTH, PlatformerGame.V_HEIGHT, cam);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();

        Image upImg = new Image(new Texture("Controllers/up.png"));
        upImg.setSize(200, 200);
        upImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image leftImg = new Image(new Texture("Controllers/left.png"));
        leftImg.setSize(200, 200);
        leftImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("Controllers/right.png"));
        rightImg.setSize(200, 200);
        rightImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight()).pad(0, 25, 25, 25);
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight()).padBottom(25);

        table.add(upImg).size(upImg.getWidth(), upImg.getHeight()).pad(0, PlatformerGame.V_WIDTH - leftImg.getWidth() - rightImg.getWidth() - upImg.getWidth() -75, 25, 25);

        stage.addActor(table);
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}
