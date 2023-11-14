package com.mathquest.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		Const constant = new Const();
		config.setWindowedMode(Const.gWidth, Const.gHeight);
		config.useVsync(true);
		config.setResizable(false);
		config.setTitle("MathQuest");
		new Lwjgl3Application(new MathQuest(), config);
	}
}
