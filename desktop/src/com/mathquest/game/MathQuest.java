package com.mathquest.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Klasa reprezentująca główną klasę gry MathQuest.
 * Rozszerza klasę Game z frameworka LibGDX.
 */
public class MathQuest extends Game {

    /**
     * Obiekt do rysowania obrazów w grze.
     */
    public SpriteBatch batch;

    /**
     * Metoda wywoływana podczas tworzenia gry.
     * Inicjalizuje obiekt do rysowania i ustawia ekran gry na MainMenu.
     */
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenu(this));
    }

    /**
     * Metoda wywoływana podczas renderowania gry.
     * Wywołuje metodę render z klasy bazowej.
     */
    public void render() {
        super.render();
    }

    /**
     * Metoda wywoływana podczas zamykania gry.
     * Zwalnia zasoby, takie jak obiekt do rysowania.
     */
    public void dispose() {
        batch.dispose();
    }
}
