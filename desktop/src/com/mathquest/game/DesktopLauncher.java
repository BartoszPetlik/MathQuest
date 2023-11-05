package com.mathquest.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mathquest.game.MathQuest;
import sun.jvm.hotspot.HelloWorld;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1024, 832);
		config.useVsync(true);
		config.setResizable(false);
		config.setTitle("MathQuest");
		new Lwjgl3Application(new MathQuest(), config);
	}
}