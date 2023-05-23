package com.bmstu.ppm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bmstu.ppm.levels.Level;
import com.bmstu.ppm.menu.LoadLevelScreen;
import com.bmstu.ppm.menu.MainMenuScreen;
import com.bmstu.ppm.menu.SettingsScreen;

import java.io.*;
import java.util.HashMap;

public class PPMGame extends Game {
	public Skin skin;
	public boolean DEBUG = true;
	public HashMap<STAGE, Screen> screens = new HashMap<>();
	public Settings settings = new Settings();
	public String saveFilePath;
	public Music music;
	public STAGE currentLevel = STAGE.MAIN_MENU;
	
	@Override
	public void create () {
		try {
			settings.load();
		} catch (IOException e) {
			settings.FOV = 60;
			settings.volume = 100;
			settings.graphics = 8;
			settings.fishEye = false;
			settings.save();
		}
		saveFilePath = Gdx.files.getLocalStoragePath();
		System.out.println(saveFilePath);

		Gdx.graphics.setWindowedMode(
				Integer.parseInt(settings.resolution.split("x")[0]),
				Integer.parseInt(settings.resolution.split("x")[1])
		);
		Gdx.graphics.setUndecorated(true);


		music = Gdx.audio.newMusic(Gdx.files.internal("music/OveMelaa-ThemeCrystalized.mp3"));
		music.setVolume((float) settings.volume / 100);
		music.setLooping(true);
		music.play();
		
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		screens.put(STAGE.MAIN_MENU, new MainMenuScreen(this));
		screens.put(STAGE.MAIN_MENU_LOAD, new LoadLevelScreen(this));
		screens.put(STAGE.MAIN_MENU_SETTINGS, new SettingsScreen(this));
		screens.put(STAGE.PAUSE, new SettingsScreen(this));
		screens.put(STAGE.DEBUG_LEVEL, new Level(
				this,
				new int[][]{
						{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
						{2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,1,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,2,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
						{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
				},
				new Texture(Gdx.files.internal("Textures/Clouds/Clouds 7/1.png")),
				new Texture(Gdx.files.internal("Textures/Clouds/Clouds 7/1.png"))
		));
		screens.put(STAGE.LEVEL1, new MainMenuScreen(this));
		screens.put(STAGE.LEVEL2, new MainMenuScreen(this));
		this.setScreen(screens.get(STAGE.MAIN_MENU));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		music.dispose();
	}
}
