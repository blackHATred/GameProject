package com.bmstu.ppm;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.bmstu.ppm.PPMGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.setForegroundFPS(120);
		//config.setWindowedMode(1,1);
		//config.useVsync(true);
		int samples = 4; // качество сглаживания MSAA
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, samples);
		config.setTitle("Pew-Pew-Meow");
		config.setResizable(false);
		new Lwjgl3Application(new PPMGame(), config);
	}
}
