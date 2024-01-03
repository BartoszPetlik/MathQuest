package com.mathquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen implements Screen {
    final MathQuest game;

    Skin skin, mySkin;

    private Map map;

    private int level;
    Stage stage, hudStage;
    Table table;
    Dialog dialog;
    private long actTime, startTime;
    private int actKeys, correctAnswers, wrongAnswers;
    private int gameTime;
    String keyString, levelString, timeString;
    Label keyLabel, levelLabel, timeLabel;

    Quest quest;

    private boolean activeDialog;

    private int i, chestNumber;

    private boolean activeQuest, nextLevel;
    private ArrayList<Rectangle> chests = new ArrayList<>();
    private ArrayList<Rectangle> chestsArea = new ArrayList<>();
    private ArrayList<Boolean> isOpen = new ArrayList<>();
    private Texture character, chrUp1, chrUp2, chrDown1, chrDown2, chrLeft1, chrLeft2, chrRight1, chrRight2;
    private Texture basicCharacter;
    private Texture groundTileTxt,wallTileTxt, chestClosed, chestOpen, chestTxt;

    private Texture groundTileTxt1, wallTileTxt1;
    private Texture groundTileTxt2, wallTileTxt2;
    private Texture groundTileTxt3, wallTileTxt3;

    private float actPosX, actPosY, prevPosX, prevPosY;
    private boolean collisionW, collisionS, collisionA,collisionD;
    private int collisionCounter;

    private OrthographicCamera camera;
    private Rectangle characterHitBox;
    private int counterA = 0;
    private int counterD = 0;
    private int counterW = 0;
    private int counterS = 0;

    public GameScreen(final MathQuest game) {
        this.game = game;

        level = 1;
        activeQuest = false;

//HUD initial -----------------------------------------------------------------------
        startTime = TimeUtils.millis();

        stage = new Stage(new ScreenViewport());
        hudStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(hudStage);
        table = new Table();
        table.setFillParent(true);
        table.top();
        table.padTop(7);
        skin = new Skin(Gdx.files.internal("shadeui/uiskin.json"));
        mySkin = new Skin(Gdx.files.internal("mySkinHUD/my_Skin_HUD.json"));
        keyLabel = new Label("Klucze: 0/3", mySkin);
        levelLabel = new Label("Poziom: 1", mySkin);
        timeLabel = new Label("Czas: 0", mySkin);
        table.add(keyLabel).expandX().width(341).padLeft(230);
        table.add(levelLabel).expandX().width(321);
        table.add(timeLabel).expandX().width(331);

        hudStage.addActor(table);
//-------------------------------------------------------------------------------------

        Gdx.input.setInputProcessor(stage);

        quest = new Quest(stage);
//Txt load

        basicCharacter = new Texture(Gdx.files.internal("characterTextures/out2.png"));
        character = basicCharacter;
        chrUp1 = new Texture(Gdx.files.internal("characterTextures/back1.png"));
        chrUp2 = new Texture(Gdx.files.internal("characterTextures/back2.png"));
        chrDown1 = new Texture(Gdx.files.internal("characterTextures/front1.png"));
        chrDown2 = new Texture(Gdx.files.internal("characterTextures/front2.png"));
        chrLeft1 = new Texture(Gdx.files.internal("characterTextures/left1.png"));
        chrLeft2 = new Texture(Gdx.files.internal("characterTextures/left2.png"));
        chrRight1 = new Texture(Gdx.files.internal("characterTextures/right1.png"));
        chrRight2 = new Texture(Gdx.files.internal("characterTextures/right2.png"));

        groundTileTxt1 = new Texture(Gdx.files.internal("tileTextures/Swamp_Tile_12.png"));
        wallTileTxt1 = new Texture(Gdx.files.internal("tileTextures/Swamp_Tile_31.png"));

        groundTileTxt2 = new Texture(Gdx.files.internal("tileTextures/Tile_12.png"));
        wallTileTxt2 = new Texture(Gdx.files.internal("tileTextures/Tile_36.png"));

        groundTileTxt3 = new Texture(Gdx.files.internal("tileTextures/IndustrialTile_46.png"));
        wallTileTxt3 = new Texture(Gdx.files.internal("tileTextures/IndustrialTile_25.png"));
        chestClosed = new Texture(Gdx.files.internal("chest_closed.png"));
        chestOpen = new Texture(Gdx.files.internal("Chest_open.png"));
//----------------------------------------------------------------------------------------------------

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 872);

        Gdx.input.setCursorCatched(true);

        characterHitBox = new Rectangle();
        characterHitBox.x = 33;
        characterHitBox.y = 33;
        characterHitBox.width = 50;
        characterHitBox.height = 72;

        addChest(50, 585);

        isOpen.add(false);
        isOpen.add(false);
        isOpen.add(false);

        chestNumber = 0;

        prevPosX = 0;
        prevPosY = 0;

        correctAnswers = 0;
        wrongAnswers = 0;

        map = new Map(level);
    }

    @Override
    public void show() {

    }
    public void mapRender(ArrayList<Rectangle> groundTiles, ArrayList<Rectangle> wallTiles, int level) {
        if (level == 1) {
            groundTileTxt = groundTileTxt1;
            wallTileTxt = wallTileTxt1;
        }else if (level == 2) {
            groundTileTxt = groundTileTxt2;
            wallTileTxt = wallTileTxt2;
            map = new Map(level);
        }else {
            groundTileTxt = groundTileTxt3;
            wallTileTxt = wallTileTxt3;
            map = new Map(level);
        }

        for (Rectangle element:map.groundTiles) {
            game.batch.draw(groundTileTxt, element.x, element.y);
        }

        for (Rectangle element:map.wallTiles) {
            game.batch.draw(wallTileTxt, element.x, element.y);
        }
    }

    private void addChest (int x, int y) {
        chests.add(new Rectangle());
        chests.get(level - 1).x = x;
        chests.get(level - 1).y = y;
        chests.get(level - 1).width = 64;
        chests.get(level - 1).height = 48;

        chestsArea.add(new Rectangle());
        chestsArea.get(level - 1).x = chests.get(level - 1).x - 32;
        chestsArea.get(level - 1).y = chests.get(level - 1).y - 32;
        chestsArea.get(level - 1).width = 64 + 64;
        chestsArea.get(level - 1).height = 48 + 64;
    }

    private void updateChestPos (int x, int y, int number) {
        chests.get(number).x = x;
        chests.get(number).y = y;
        chestsArea.get(number).x = chests.get(number).x - 32;
        chestsArea.get(number).y = chests.get(number).y - 32;
    }

    private void dispLevelMsg () {
        activeDialog = true;
        newDialog("Informacja", 500, 300);

        table.add(new Label("Gratulacje!", skin, "title-plain")).padBottom(30).row();
        table.add(new Label("Przechodzisz do kolejnego poziomu!", skin, "title-plain")).padBottom(30).row();
        TextButton okButton = new TextButton("Ok", skin,"round");

        dialog.button(okButton, true);

        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                dialog.remove();
                activeDialog = false;
                nextLevel = true;
                Gdx.input.setCursorCatched(true);
            }
        });

        dialog.show(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        mapRender(map.groundTiles, map.wallTiles, level);

        i = 0;

        for (Rectangle element:chests) {
            if (isOpen.get(i) == true) {
                chestTxt = chestOpen;
            }else {
                chestTxt = chestClosed;
            }
            game.batch.draw(chestTxt, element.x, element.y);
            i += 1;
        }
        game.batch.draw(character, characterHitBox.x, characterHitBox.y);
        game.batch.end();
// HUD -------------------------------------------------------------
        if (correctAnswers != 6) {
            actTime = TimeUtils.millis();
        }

        gameTime = (int) ((actTime - startTime)/1000);
        timeString = "Czas: " + gameTime;
        timeLabel.setText(timeString);

        levelString = "Poziom: " + level;
        levelLabel.setText(levelString);

        keyString = "Klucze: " + actKeys + "/" + level;
        keyLabel.setText(keyString);

        hudStage.act(Gdx.graphics.getDeltaTime());
        hudStage.draw();

//Movement ---------------------------------------------------------
        if (Gdx.input.isKeyPressed(Input.Keys.A) && activeQuest == false) {
            counterD = 0;
            counterS = 0;
            counterW = 0;
            if (!collisionA) {
                characterHitBox.x -= 200 * Gdx.graphics.getDeltaTime();
            }
            counterA += 1;
            if (counterA == 1) {
                character = chrLeft1;
            }else if (counterA == 1 + Const.ANIM_TIME){
                character = chrLeft2;
            }else if (counterA == 1 + 2*Const.ANIM_TIME) {
                counterA = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && activeQuest == false) {
            counterS = 0;
            counterA = 0;
            counterW = 0;
            if (!collisionD) {
                characterHitBox.x += 200 * Gdx.graphics.getDeltaTime();
            }
            counterD += 1;
            if (counterD == 1) {
                character = chrRight1;
            } else if (counterD == 1 + Const.ANIM_TIME) {
                character = chrRight2;
            } else if (counterD == 1 + 2 * Const.ANIM_TIME) {
                counterD = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && activeQuest == false) {
            counterD = 0;
            counterA = 0;
            counterS = 0;
            if (!collisionW) {
                characterHitBox.y += 200 * Gdx.graphics.getDeltaTime();
            }
            counterW += 1;
            if (counterW == 1) {
                character = chrUp1;
            } else if (counterW == 1 + Const.ANIM_TIME) {
                character = chrUp2;
            } else if (counterW == 1 + 2 * Const.ANIM_TIME) {
                counterW = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && activeQuest == false) {
            counterD = 0;
            counterA = 0;
            counterW = 0;
            if (!collisionS) {
                characterHitBox.y -= 200 * Gdx.graphics.getDeltaTime();
            }
            counterS += 1;
            if (counterS == 1) {
                character = chrDown1;
            } else if (counterS == 1 + Const.ANIM_TIME) {
                character = chrDown2;
            } else if (counterS == 1 + 2 * Const.ANIM_TIME) {
                counterS = 0;
            }
        }
//Exit ------------------------------------------------------------------------------------
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && activeDialog == false) {
            activeDialog = true;
            newDialog("", 550, 300);

            table.add(new Label("Czy na pewno chcesz wyjsc do menu?", skin, "title-plain"));


            TextButton yesButton = new TextButton("Tak", skin,"round");
            TextButton noButton = new TextButton("Nie", skin,"round");

            dialog.button(yesButton, true);
            dialog.button(noButton, true);

            yesButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dialog.hide();
                    dialog.remove();
                    game.setScreen(new MainMenu(game));
                }
            });
            noButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dialog.hide();
                    dialog.remove();
                    activeDialog = false;
                    Gdx.input.setCursorCatched(true);
                }
            });
            dialog.show(stage);
        }
//Collision detection -----------------------------------------------------------
        actPosX = characterHitBox.x;
        actPosY = characterHitBox.y;
        collisionCounter = 0;

        for (Rectangle element:map.wallTiles) {
            if (element.overlaps(characterHitBox)) {
                if (actPosX - prevPosX > 0) {
                    collisionD = true;
                }else if (actPosX - prevPosX < 0) {
                    collisionA = true;
                }else if (actPosY - prevPosY > 0) {
                    collisionW = true;
                }else if (actPosY - prevPosY < 0) {
                    collisionS = true;
                }
                collisionCounter += 1;
            }
        }
        for (Rectangle element:chests) {
            if (element.overlaps(characterHitBox)) {
                if (actPosX - prevPosX > 0) {
                    collisionD = true;
                }else if (actPosX - prevPosX < 0) {
                    collisionA = true;
                }else if (actPosY - prevPosY > 0) {
                    collisionW = true;
                }else if (actPosY - prevPosY < 0) {
                    collisionS = true;
                }
            }
        }

        if (collisionS || collisionW || collisionD || collisionA) {
            characterHitBox.x = prevPosX;
            characterHitBox.y = prevPosY;
        }else {
            prevPosX = actPosX;
            prevPosY = actPosY;
        }
        if (collisionCounter == 0) {
            collisionW = false;
            collisionA = false;
            collisionD = false;
            collisionS = false;
        }
//Quest ---------------------------------------------------------------------------------------------------
        if (quest.again) {
            wrongAnswers += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q) && activeQuest == false || quest.again == true) {
            i = 0;
            for (Rectangle element:chestsArea) {
                if (element.overlaps(characterHitBox) && isOpen.get(i) == false) {
                    chestNumber = i;
                    quest = new Quest(stage);
                    quest.operation();
                    activeQuest = true;
                }
                i += 1;
            }
        }

        if (quest.status) {
            activeQuest = false;
            actKeys += 1;
            quest.status = false;
            isOpen.set(chestNumber, true);
            correctAnswers += 1;
            Gdx.input.setCursorCatched(true);
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//------------------------------------------------------------------------------------------------------------

//Map choice --------------------------------------------------------------
        if (actKeys == 1 && level == 1 && nextLevel == true) {
            level = 2;
            actKeys = 0;
            nextLevel = false;

            characterHitBox.x = 700;
            characterHitBox.y = 590;
            character = basicCharacter;

            updateChestPos(180, 595, 0);

            addChest(690, 465);

            for (int i = 0; i < 3; i++) {
                isOpen.set(i, false);
            }
        }else if (actKeys == 2 && level == 2 && nextLevel == true) {
            level = 3;
            actKeys = 0;
            nextLevel = false;

            characterHitBox.x = 700;
            characterHitBox.y = 40;
            character = basicCharacter;

            updateChestPos(690, 570, 0);
            updateChestPos(305, 400, 1);

            addChest(305, 710);

            for (int i = 0; i < 3; i++) {
                isOpen.set(i, false);
            }
        }else if (actKeys == 3 && level == 3 && activeDialog == false) {
            nextLevel = false;
            activeDialog = true;

            newDialog("Informacja", 500, 300);

            table.add(new Label("To jest ostatni poziom!", skin, "title-plain")).padBottom(30).row();
            table.add(new Label("Gratulacje!", skin, "title-plain")).padBottom(30).row();

            TextButton button = new TextButton("Podsumowanie", skin,"round");

            dialog.button(button, true);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dialog.hide();
                    dialog.remove();
                    activeDialog = false;
                    Gdx.input.setCursorCatched(true);

                    newDialog("Podsumowanie",550,400);

                    String time;
                    String total;
                    String correct;
                    String wrong;

                    int totalAnswers = correctAnswers + wrongAnswers;

                    time = "Czas rozgrywki: " + gameTime + " sekund";
                    total = "Liczba wszystkich odpowiedzi: " + totalAnswers;
                    correct = "Liczba poprawnych odpowiedzi: " + correctAnswers;
                    wrong = "Liczba niepoprawnych odpowiedzi: " + wrongAnswers;

                    table.add(new Label(time, skin, "title-plain")).padBottom(30).row();
                    table.add(new Label(total, skin, "title-plain")).padBottom(30).row();
                    table.add(new Label(correct, skin, "title-plain")).padBottom(30).row();
                    table.add(new Label(wrong, skin, "title-plain")).padBottom(30).row();

                    TextButton button = new TextButton("Koniec", skin,"round");

                    dialog.button(button, true);

                    button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            dialog.hide();
                            dialog.remove();
                            activeDialog = false;
                            game.setScreen(new MainMenu(game));
                        }
                    });

                    dialog.show(stage);
                }
            });

            dialog.show(stage);
        }
// Level info -----------------------------------------------------------------------------------------------
        if (((actKeys == 1 && level == 1) || (actKeys == 2 && level == 2)) && activeDialog == false) {
            dispLevelMsg();
        }
    }

    public void newDialog(String title, int width, int height) {
        activeDialog = true;
        dialog = new Dialog(title, skin) {
            @Override
            public float getPrefWidth() {
                return width;
            }

            @Override
            public float getPrefHeight() {
                return height;
            }
        };
        Gdx.input.setCursorCatched(false);

        table = new Table();
        table.center();
        dialog.getContentTable().add(table);
    }

    @Override
    public void resize(int width, int height) {

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
        character.dispose();
        chrUp1.dispose();
        chrUp2.dispose();
        chrDown1.dispose();
        chrDown2.dispose();
        chrLeft1.dispose();
        chrLeft2.dispose();
        chrRight1.dispose();
        chrRight2.dispose();
        game.batch.dispose();
        chestClosed.dispose();
        groundTileTxt.dispose();
        wallTileTxt.dispose();
        stage.dispose();
        hudStage.dispose();
        chestOpen.dispose();
        chestTxt.dispose();
    }
}
