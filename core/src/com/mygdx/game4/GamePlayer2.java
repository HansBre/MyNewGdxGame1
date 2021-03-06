package com.mygdx.game4;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.List;

public class GamePlayer2 extends GameItem {

    public final static int PLAYER_SIZE = 0;
    private boolean isJumping = false;
    private final int stepValue = 12;

    private float angle = 0;
    private int groundLevel = MainGameClass.GROUND_LEVEL;

    private boolean onPlatform = false;
    private GamePlatform currentPlatform = null;

    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 4;
    private Animation walkAnimation;
    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    //private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;
    private float stateTime;

    public GamePlayer2(int x, int y, int width, int height, String path) {
        super(x, y, width, height, path);
        //sprite = new Sprite(new Texture(Gdx.files.internal(path)));
        walkSheet = new Texture(Gdx.files.internal(path));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

        walkFrames = new TextureRegion[(FRAME_COLS * FRAME_ROWS)];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {

                walkFrames[index++] = tmp[i][j];

            }
        }
        walkAnimation = new Animation(0.033f, walkFrames);

        stateTime = 0f;
    }

    @Override
    public void render(SpriteBatch spriteBatch){
        stateTime += Gdx.graphics.getDeltaTime();

        currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        spriteBatch.draw(currentFrame, this.x-180, this.y-50);


    }

    @Override
    public void update(List<GameItem> gameItems) {
        //Sjekker hopping, y-verdi settes her:
        if (isJumping && angle < Math.PI) {
            this.y = groundLevel + (int)((310f) * Math.sin(angle));
            angle += (8)*(Math.PI/180f);
            onPlatform = false;
        } else {
            this.y = groundLevel;
            angle = 0;
            isJumping = false;
        }

        //Dersom spiller p� platform, flytt innafor denne:
        if (this.onPlatform) {
            if (this.on(currentPlatform)) {
                this.move(currentPlatform);  //Justerer enten groundLevel eller this.x
            } else {
                this.onPlatform = false;
                this.currentPlatform = null;
            }
        }

        //Dersom ikke p� platform, sjekk alle platformer:
        if (this.onPlatform == false) {
            //Utenfor, faller evt. ned:
            if(groundLevel >= MainGameClass.GROUND_LEVEL){
                groundLevel -= 8;
            }
            //Sjekker seg selv mot alle plattformer. Dersom p� en platform sjekkes ikke resten...
            for (int i = 0; i < gameItems.size(); i++) {
                GamePlatform pf = (GamePlatform) gameItems.get(i);
                //P� platformen:
                if (this.on(pf)) {
                    this.onPlatform = true;
                    this.currentPlatform = pf;
                    groundLevel = (int) pf.y + (int) pf.getHeight();
                }
            }
        }
    }
    private boolean on(GamePlatform platform) {
        return (((this.x + this.getWidth()/2f) >= (platform.x)) && ((this.x + this.getWidth()/2f <= platform.x + platform.getWidth())) &&
                ((this.y >= platform.y)));
    }


    private void move(GamePlatform pf) {
        //Spilleren skal bevege seg etter platformen:
        if(pf.isHorisontalFalseVerticalTrue()==false){
            if (pf.getMoveRight())
                this.x += pf.getSpeed();
            else
                this.x -= pf.getSpeed();
        }else {
            if (pf.isMoveUp())
                groundLevel+= pf.getSpeed();    //NB! justerer p� groundLevel!!
            else
                groundLevel-= pf.getSpeed();
        }
    }

    @Override
    public void handleInput(OrthographicCamera camera) {
        if (Gdx.input.isKeyPressed(Input.Keys.Y)) {
            this.y += stepValue;
            if ((this.y + GamePlayer2.PLAYER_SIZE) >= (MainGameClass.WORLD_HEIGHT)) {
                this.y = MainGameClass.WORLD_HEIGHT - GamePlayer2.PLAYER_SIZE;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            this.y -= stepValue;
            if (this.y <= 0) {
                this.y = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.x -= stepValue;
            if (this.x < 1500) {
                camera.translate(-MainGameClass.CAMERA_PAN_SPEED, 0, 0);
            }
            if (this.x < 0) {
                this.x = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.x += stepValue;
            if (this.x > 500) {
                camera.translate(MainGameClass.CAMERA_PAN_SPEED, 0, 0);
            }
            if ((this.x + GamePlayer2.PLAYER_SIZE) >= MainGameClass.WORLD_WIDTH) {
                this.x = (MainGameClass.WORLD_WIDTH - GamePlayer2.PLAYER_SIZE);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            camera.translate(-9, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            camera.translate(9, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            //if (!isJumpingDown)
            isJumping = true;
        }
//         if (Gdx.input.isKeyPressed(Keys.W)) {
//          camera.rotate(-rotationSpeed, 0, 0, 1);
//         }
//         if (Gdx.input.isKeyPressed(Keys.E)) {
//          camera.rotate(rotationSpeed, 0, 0, 1);
//         }

//         camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100/camera.viewportWidth);
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, MainGameClass.WORLD_WIDTH / camera.viewportWidth);
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, MainGameClass.WORLD_HEIGHT / camera.viewportHeight);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, MainGameClass.WORLD_WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, MainGameClass.WORLD_HEIGHT - effectiveViewportHeight / 2f);
    }

}