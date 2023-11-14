package com.mathquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;

public class GameScreen implements Screen {
    final MathQuest game;

    private Map map;

    private int level;

    private Texture character, chrUp1, chrUp2, chrDown1, chrDown2, chrLeft1, chrLeft2, chrRight1, chrRight2;
    private Texture groundTileTxt,wallTileTxt, chest;

    private Texture groundTileTxt1, wallTileTxt1;
    private Texture groundTileTxt2, wallTileTxt2;
    private Texture groundTileTxt3, wallTileTxt3;

    private float actPosX, actPosY, prevPosX, prevPosY;
    private boolean collisionW, collisionS, collisionA,collisionD;
    private int collisionCounter;

    private OrthographicCamera camera;
    private Rectangle characterHitBox, chestHitBox;
    private int counterA = 0;
    private int counterD = 0;
    private int counterW = 0;
    private int counterS = 0;

    public GameScreen(final MathQuest game) {
        this.game = game;

        level = 1;

        character = new Texture(Gdx.files.internal("characterTextures/out2.png"));
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
        chest = new Texture(Gdx.files.internal("chest_closed.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 872);

        characterHitBox = new Rectangle();
        characterHitBox.x = 33;
        characterHitBox.y = 33;
        characterHitBox.width = 50;
        characterHitBox.height = 72;

        chestHitBox = new Rectangle();
        chestHitBox.x = 485;
        chestHitBox.y = 325;
        chestHitBox.width = 32;
        chestHitBox.height = 32;

        prevPosX = 0;
        prevPosY = 0;

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

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        mapRender(map.groundTiles, map.wallTiles, level);

        game.batch.draw(chest, chestHitBox.x, chestHitBox.y);
        game.batch.draw(character, characterHitBox.x, characterHitBox.y);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            level = 2;
        }else if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            level = 3;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
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
                System.out.println("kolizja");
                collisionCounter += 1;
                System.out.println(collisionCounter);
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
        chest.dispose();
        groundTileTxt.dispose();
        wallTileTxt.dispose();
    }
}
