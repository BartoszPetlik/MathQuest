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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu implements Screen {

    MathQuest game;

    private Stage stage;
    private Skin skin, mySkin;
    private Table table;
    private Texture backgroundTxt;

    OrthographicCamera camera;

    public MainMenu(MathQuest game) {
        this.game = game;

        backgroundTxt = new Texture(Gdx.files.internal("background.png"));

        Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("cursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));
        cursorPixmap.dispose();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Const.gWidth, Const.gHeight);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal(Const.skinPath));
        mySkin = new Skin(Gdx.files.internal("mySkin/my_skin.json"));

        TextButton startButton = new TextButton("Rozpocznij", skin, Const.buttonType);
        startButton.setSize(500,500);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        TextButton helpButton = new TextButton("Pomoc", skin, Const.buttonType);
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog dialog = new Dialog("Pomoc", skin) {
                    @Override
                    public float getPrefWidth() {
                        return 600;
                    }

                    @Override
                    public float getPrefHeight() {
                        return 300;
                    }
                };

                Table table = new Table();
                table.center();
                dialog.getContentTable().add(table);

                table.add(new Label("Poruszanie bohaterem: W, S, A, D", skin, "title-plain")).padBottom(30).row();
                table.add(new Label("Uruchomienie zadania obok skrzynki: Q", skin, "title-plain")).padBottom(30).row();
                table.add(new Label("Wyjscie do menu: ESC", skin, "title-plain")).padBottom(30).row();

                TextButton button = new TextButton("Rozumiem", skin,"round");

                dialog.button(button, true);

                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        dialog.hide();
                        dialog.remove();
                    }
                });

                dialog.show(stage);
            }
        });

        TextButton exitButton = new TextButton("Zamknij", skin, Const.buttonType);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.add(new Label("MathQuest", mySkin)).padBottom(80f).row();
        table.add(startButton).pad(30f).row();
        table.add(helpButton).pad(10f).row();
        table.add(exitButton).padTop(30f).padBottom(200f).row();
        stage.addActor(table);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(backgroundTxt, 0, 0);

        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        mySkin.dispose();
    }
}
