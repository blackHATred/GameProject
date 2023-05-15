package com.bmstu.ppm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import jdk.internal.misc.OSEnvironment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class PPMGame extends Game {
	public Skin skin;
	public boolean DEBUG = true;
	public HashMap<STAGE, Screen> screens = new HashMap<>();
	public Settings settings = new Settings();

	
	@Override
	public void create () {
		try {
			settings.load();
		} catch (IOException e) {
			try {
				settings.FOV = 60;
				settings.volume = 100;
				settings.graphics = 8;
				settings.save();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		screens.put(STAGE.MAIN_MENU, new MainMenuScreen(this));
		screens.put(STAGE.MAIN_MENU_LOAD, new LoadLevelScreen(this));
		screens.put(STAGE.MAIN_MENU_SETTINGS, new SettingsScreen(this));
		screens.put(STAGE.LEVEL1, new MainMenuScreen(this));

		this.setScreen(screens.get(STAGE.MAIN_MENU));
	}

	@Override
	public void render () {super.render();}
	
	@Override
	public void dispose () {}
}
