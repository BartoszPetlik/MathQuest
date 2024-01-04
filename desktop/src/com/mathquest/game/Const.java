package com.mathquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Klasa przechowująca stałe wartości używane w grze MathQuest.
 */
public class Const {

    /** Szerokość ekranu gry. */
    static int gWidth;

    /** Wysokość ekranu gry. */
    static int gHeight;

    /** Wysokość paska HUD. */
    static int barHeight;

    /** Ścieżka do pliku skórki (skin) interfejsu użytkownika. */
    static String skinPath;

    /** Typ przycisku używany w interfejsie użytkownika. */
    static String buttonType;

    /** Czas trwania animacji w klatkach. */
    static final int ANIM_TIME = 20;

    /**
     * Konstruktor klasy Const.
     * Inicjalizuje stałe wartości używane w grze MathQuest.
     */
    public Const() {
        gWidth = 1024;
        gHeight = 872;
        barHeight = 40;
        skinPath = "shadeui/uiskin.json";
        buttonType = "round";
    }
}
