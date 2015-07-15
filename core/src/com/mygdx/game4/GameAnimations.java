package com.mygdx.game4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Henrik on 08.07.2015.
 */

public class GameAnimations extends GameItem {

    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 4;
    private Animation walkAnimation;
    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    //private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;
    private float stateTime;
    private float xPosition;
    private float yPosition;

    public GameAnimations(float xPosition, float yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        walkSheet = new Texture(Gdx.files.internal("SlimeAni.png"));

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

    public GameAnimations(int x, int y, int width, int height, String path) {
        super(x, y, width, height, path);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        stateTime += Gdx.graphics.getDeltaTime();

        currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        spriteBatch.draw(currentFrame, xPosition, yPosition);
        //spriteBatch.draw(sprite, this.x, this.y, width/2f, height/2, width, height, 1, 1, 0);
        //spriteBatch.end();
    }
}
