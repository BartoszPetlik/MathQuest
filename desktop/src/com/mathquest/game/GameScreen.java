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
    final int ANIM_TIME = 20; // czas animacji

    private Texture character, chrUp1, chrUp2, chrDown1, chrDown2, chrLeft1, chrLeft2, chrRight1, chrRight2;
    private Texture groundTileTxt, wallTileTxt, chest;

    private float actPosX, actPosY, prevPosX, prevPosY;
    private float collisionPosX, getCollisionPosY;
    private boolean collisionW, collisionS, collisionA,collisionD;
    private int collisionCounter;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Rectangle characterHitBox, tileHitBox, tileHitBox1, chestHitBox;
    private ArrayList<Rectangle> wallTiles = new ArrayList<>();
    private ArrayList<Rectangle> groundTiles = new ArrayList<>();
    private int counterA = 0;
    private int counterD = 0;
    private int counterW = 0;
    private int counterS = 0;

    private final int[][] mesh = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    public GameScreen(final MathQuest game) {
        this.game = game;

        character = new Texture(Gdx.files.internal("characterTextures/out2.png"));
        chrUp1 = new Texture(Gdx.files.internal("characterTextures/back1.png"));
        chrUp2 = new Texture(Gdx.files.internal("characterTextures/back2.png"));
        chrDown1 = new Texture(Gdx.files.internal("characterTextures/front1.png"));
        chrDown2 = new Texture(Gdx.files.internal("characterTextures/front2.png"));
        chrLeft1 = new Texture(Gdx.files.internal("characterTextures/left1.png"));
        chrLeft2 = new Texture(Gdx.files.internal("characterTextures/left2.png"));
        chrRight1 = new Texture(Gdx.files.internal("characterTextures/right1.png"));
        chrRight2 = new Texture(Gdx.files.internal("characterTextures/right2.png"));

        groundTileTxt = new Texture(Gdx.files.internal("tileTextures/Tile_12.png"));
        wallTileTxt = new Texture(Gdx.files.internal("tileTextures/Tile_36.png"));
        chest = new Texture(Gdx.files.internal("chest_closed.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 832);

        batch = new SpriteBatch();

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

        for (int i = 0; i < 1024/32; i++) {
            for (int j = 0; j < 832/32; j++) {
                if(mesh[j][i] == 1) {
                    wallTiles.add(new Rectangle());
                    wallTiles.get(wallTiles.size()-1).x = i * 32;
                    wallTiles.get(wallTiles.size()-1).y = j * 32;
                    wallTiles.get(wallTiles.size()-1).width = 32;
                    wallTiles.get(wallTiles.size()-1).height = 32;
                }else {
                    groundTiles.add(new Rectangle());
                    groundTiles.get(groundTiles.size()-1).x = i * 32;
                    groundTiles.get(groundTiles.size()-1).y = j * 32;
                    groundTiles.get(groundTiles.size()-1).width = 32;
                    groundTiles.get(groundTiles.size()-1).height = 32;
                }
            }
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Rectangle element:groundTiles) {
            batch.draw(groundTileTxt, element.x, element.y);
        }

        for (Rectangle element:wallTiles) {
            batch.draw(wallTileTxt, element.x, element.y);
        }

        batch.draw(chest, chestHitBox.x, chestHitBox.y);
        batch.draw(character, characterHitBox.x, characterHitBox.y);
        batch.end();

        actPosX = characterHitBox.x;
        actPosY = characterHitBox.y;
        collisionCounter = 0;

        for (Rectangle element:wallTiles) {
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
            }else if (counterA == 1 + ANIM_TIME){
                character = chrLeft2;
            }else if (counterA == 1 + 2*ANIM_TIME) {
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
            } else if (counterD == 1 + ANIM_TIME) {
                character = chrRight2;
            } else if (counterD == 1 + 2 * ANIM_TIME) {
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
            } else if (counterW == 1 + ANIM_TIME) {
                character = chrUp2;
            } else if (counterW == 1 + 2 * ANIM_TIME) {
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
            } else if (counterS == 1 + ANIM_TIME) {
                character = chrDown2;
            } else if (counterS == 1 + 2 * ANIM_TIME) {
                counterS = 0;
            }
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
        batch.dispose();
        chest.dispose();
        groundTileTxt.dispose();
        wallTileTxt.dispose();
    }
}
