package com.mathquest.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Klasa startowa dla wersji desktopowej gry MathQuest.
 * Uruchamia grę w trybie desktopowym za pomocą biblioteki LibGDX.
 */
public class DesktopLauncher {

	/**
	 * Metoda główna programu, uruchamiająca grę.
	 * @param arg Argumenty wiersza poleceń (nie są używane w tym przypadku).
	 */
	public static void main(String[] arg) {
		// Konfiguracja aplikacji desktopowej
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);

		// Inicjalizacja stałych gry
		Const constant = new Const();

		// Konfiguracja okna gry
		config.setWindowedMode(Const.gWidth, Const.gHeight);
		config.useVsync(true);
		config.setResizable(false);
		config.setTitle("MathQuest");
		config.setDecorated(false);

		// Uruchomienie gry z konfiguracją
		new Lwjgl3Application(new MathQuest(), config);
	}
}
