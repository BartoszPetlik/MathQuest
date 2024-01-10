package com.mathquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Klasa reprezentująca główne menu gry MathQuest.
 * Implementuje interfejs LibGDX Screen, który reprezentuje ekran gry.
 */
public class MainMenu implements Screen {

    /** Referencja do obiektu gry MathQuest. */
    MathQuest game;

    /** Scena do zarządzania interfejsem użytkownika. */
    public Stage stage;

    /** Skórka do stylizacji elementów interfejsu. */
    public Skin skin, mySkin;

    /** Tabela do organizacji elementów interfejsu. */
    public Table table;

    /** Tekstura tła menu głównego. */
    public Texture backgroundTxt;

    /** Kamera orthograficzna dla renderowania. */
    OrthographicCamera camera;

    /**
     * Konstruktor klasy MainMenu.
     * Inicjalizuje wszystkie niezbędne elementy interfejsu, skonfigurowane są przyciski i ich funkcjonalności.
     * @param game Referencja do obiektu gry MathQuest.
     */
    public MainMenu(MathQuest game) {
        this.game = game;

        // Inicjalizacja tła
        backgroundTxt = new Texture(Gdx.files.internal("other/background.png"));

        // Ustawienie niestandardowego kursora
        Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("cursor/cursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));
        cursorPixmap.dispose();

        // Konfiguracja kamery
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Const.gWidth, Const.gHeight);

        // Inicjalizacja sceny
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Inicjalizacja skórek
        skin = new Skin(Gdx.files.internal(Const.skinPath));
        mySkin = new Skin(Gdx.files.internal("mySkin/my_skin.json"));

        // Konfiguracja przycisków i ich funkcjonalności
        TextButton startButton = new TextButton("Rozpocznij", skin, Const.buttonType);
        startButton.setSize(500, 500);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton helpButton = new TextButton("Pomoc", skin, Const.buttonType);
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showHelpDialog();
            }
        });

        TextButton exitButton = new TextButton("Zamknij", skin, Const.buttonType);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Konfiguracja tabeli i dodanie elementów
        table = new Table();
        table.setFillParent(true);
        table.add(new Label("MathQuest", mySkin)).padBottom(80f).row();
        table.add(startButton).pad(30f).row();
        table.add(helpButton).pad(10f).row();
        table.add(exitButton).padTop(30f).padBottom(200f).row();
        stage.addActor(table);
    }

    // Implementacja metod z interfejsu Screen

    @Override
    public void show() {
        // Implementacja metody show
    }

    @Override
    public void render(float delta) {
        // Implementacja metody render
        renderBackground();
        renderStage();
    }

    @Override
    public void resize(int width, int height) {
        // Implementacja metody resize
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implementacja metody pause
    }

    @Override
    public void resume() {
        // Implementacja metody resume
    }

    @Override
    public void hide() {
        // Implementacja metody hide
    }

    @Override
    public void dispose() {
        // Implementacja metody dispose
        stage.dispose();
        skin.dispose();
        mySkin.dispose();
        backgroundTxt.dispose();
    }

    /**
     * Metoda renderująca tło menu głównego.
     */
    public void renderBackground() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTxt, 0, 0);
        game.batch.end();
    }

    /**
     * Metoda renderująca scenę gry.
     */
    public void renderStage() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Metoda wywołująca dialog z informacjami dotyczącymi sterowania w grze.
     * Zawiera podstawowe wskazówki dotyczące poruszania się bohaterem, wykonywania
     * zadań obok skrzynek oraz opcji wyjścia do menu.
     */
    public void showHelpDialog() {
        // Tworzenie nowego obiektu dialogu z określonym tytułem i stylem skórki
        Dialog dialog = new Dialog("Pomoc", skin) {
            @Override
            public float getPrefWidth() {
                return 600;
            }

            @Override
            public float getPrefHeight() {
                return 350;
            }
        };

        // Tworzenie tabeli do układania treści wewnątrz dialogu
        Table table = new Table();
        table.center();
        dialog.getContentTable().add(table);

        // Dodawanie etykiet informacyjnych do tabeli
        table.add(new Label("Poruszanie bohaterem: W, S, A, D", skin, "title-plain")).padBottom(30).row();
        table.add(new Label("Uruchomienie zadania obok skrzynki: Q", skin, "title-plain")).padBottom(30).row();
        table.add(new Label("Wyjscie do menu: ESC", skin, "title-plain")).padBottom(30).row();

        // Dodawanie przycisku do zamknięcia dialogu
        TextButton button = new TextButton("Rozumiem", skin, "round");
        dialog.button(button, true);

        // Dodawanie słuchacza zdarzeń dla przycisku
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Ukrycie i usunięcie dialogu po kliknięciu przycisku
                dialog.hide();
                dialog.remove();
            }
        });

        // Wyświetlenie dialogu na scenie
        dialog.show(stage);
    }

}
