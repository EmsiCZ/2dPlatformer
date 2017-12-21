package com.olszar.platformer.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.olszar.platformer.PlatformerGame;

/**
 * Created by lubos on 21.12.2017.
 */

public class MenuScreen implements Screen {
    private final Stage stage;
    private Music music;

    public MenuScreen(final PlatformerGame game){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        TextButton newGameButton = new TextButton("New game", new Skin(Gdx.files.internal("skins/glassy-ui.json")));
        newGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.stop();
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        });

        TextButton exitGameButton = new TextButton("Exit game", new Skin(Gdx.files.internal("skins/glassy-ui.json")));
        exitGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                dispose();
            }
        });

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label label = new Label("Platformer game", font);
        label.setFontScale(8);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(label);
        table.row().padTop(100);
        table.add(newGameButton);
        table.row().padTop(50);
        table.add(exitGameButton);

        stage.addActor(table);

        music = PlatformerGame.manager.get("audio/music/Worldmap Theme.mp3", Music.class);
        music.setLooping(true);
        music.play();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);

        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
