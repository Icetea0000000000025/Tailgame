package com.gdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

    public enum Direction {
        DOWN, LEFT, RIGHT, UP
    }

    private float x;
    private float y;
    private float speed = 150f; // pixels per second
    private Direction direction = Direction.DOWN;
    private boolean isMoving = false;

    // Sprite & Animations
    private Texture spriteSheet;
    private Animation<TextureRegion> walkDown;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;
    private Animation<TextureRegion> walkUp;
    private float stateTime = 0f;

    // Player rendering size
    public static final float FRAME_SIZE = 32f; // Render size on screen

    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        loadAnimations();
    }

    private void loadAnimations() {
        spriteSheet = new Texture(Gdx.files.internal("sprites/characters/Farmer.png"));
        // Split 64x64 sprite sheet into 4x4 grid (16x16 per cell)
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 16, 16);

        float frameDuration = 0.15f;
        walkDown  = new Animation<>(frameDuration, tmp[0]);
        walkLeft  = new Animation<>(frameDuration, tmp[1]);
        walkRight = new Animation<>(frameDuration, tmp[2]);
        walkUp    = new Animation<>(frameDuration, tmp[3]);

        walkDown.setPlayMode(Animation.PlayMode.LOOP);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        walkRight.setPlayMode(Animation.PlayMode.LOOP);
        walkUp.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        float moveX = 0;
        float moveY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveX -= 1;
            direction = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveX += 1;
            direction = Direction.RIGHT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveY += 1;
            direction = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveY -= 1;
            direction = Direction.DOWN;
        }

        isMoving = (moveX != 0 || moveY != 0);

        if (isMoving) {
            // Normalize diagonal movement
            float length = (float) Math.sqrt(moveX * moveX + moveY * moveY);
            moveX /= length;
            moveY /= length;

            x += moveX * speed * delta;
            y += moveY * speed * delta;
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        batch.draw(currentFrame, x, y, FRAME_SIZE, FRAME_SIZE);
    }

    private TextureRegion getCurrentFrame() {
        Animation<TextureRegion> currentAnim = switch (direction) {
            case DOWN -> walkDown;
            case LEFT -> walkLeft;
            case RIGHT -> walkRight;
            case UP -> walkUp;
        };

        if (isMoving) {
            return currentAnim.getKeyFrame(stateTime, true);
        } else {
            return currentAnim.getKeyFrames()[0]; // Idle standing frame
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setPosition(float x, float y) { this.x = x; this.y = y; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public void dispose() {
        if (spriteSheet != null) {
            spriteSheet.dispose();
        }
    }
}
