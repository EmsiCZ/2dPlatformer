package com.olszar.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.olszar.platformer.Screens.PlayScreen;

public class PlatformerGame extends Game {
	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;
	public static final float PPM = 100;

	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COINBRICK_BIT = 8;
	public static final short DESTROYED_BIT = 16;

	public SpriteBatch batch;

	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/Grasslands Theme.mp3", Music.class);
		manager.load("audio/sounds/jump.wav", Sound.class);
		manager.load("audio/sounds/pickup_coin.m4a", Sound.class);
		manager.finishLoading();

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
