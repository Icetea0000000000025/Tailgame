package com.gdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.game.GdxGame;
import com.gdx.game.entities.Player;

public class GameScreen implements Screen {

    private final GdxGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Viewport hudViewport;

    private final Player player;

    // World bounds
    public static final float WORLD_WIDTH = 1200f;
    public static final float WORLD_HEIGHT = 900f;

    public GameScreen(GdxGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(400, 300, camera);
        hudViewport = new FitViewport(800, 600);

        // Spawn player at center of world
        player = new Player(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Return to menu on ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
            return;
        }

        // Update player logic
        player.update(delta);

        // Clamp player inside world bounds
        float clampedX = MathUtils.clamp(player.getX(), 0, WORLD_WIDTH - Player.FRAME_SIZE);
        float clampedY = MathUtils.clamp(player.getY(), 0, WORLD_HEIGHT - Player.FRAME_SIZE);
        player.setPosition(clampedX, clampedY);

        // Smooth camera follow (lerp)
        float targetCamX = MathUtils.clamp(player.getX() + Player.FRAME_SIZE / 2f,
                viewport.getWorldWidth() / 2f, WORLD_WIDTH - viewport.getWorldWidth() / 2f);
        float targetCamY = MathUtils.clamp(player.getY() + Player.FRAME_SIZE / 2f,
                viewport.getWorldHeight() / 2f, WORLD_HEIGHT - viewport.getWorldHeight() / 2f);
        camera.position.x += (targetCamX - camera.position.x) * 0.1f;
        camera.position.y += (targetCamY - camera.position.y) * 0.1f;
        camera.update();

        // Clear screen with lush grass green
        Gdx.gl.glClearColor(0.22f, 0.45f, 0.28f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1. Draw World Background & Grid
        viewport.apply();
        ShapeRenderer shape = game.getShapeRenderer();
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Line);

        // Draw World Border
        shape.setColor(new Color(0.12f, 0.3f, 0.16f, 1f));
        shape.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw 32x32 Tile Grid
        shape.setColor(new Color(0.20f, 0.40f, 0.25f, 0.6f));
        for (float x = 0; x <= WORLD_WIDTH; x += 32f) {
            shape.line(x, 0, x, WORLD_HEIGHT);
        }
        for (float y = 0; y <= WORLD_HEIGHT; y += 32f) {
            shape.line(0, y, WORLD_WIDTH, y);
        }
        shape.end();

        // 2. Draw Entities
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        player.render(game.getBatch());
        game.getBatch().end();

        // 3. Draw HUD (Screen Overlay)
        hudViewport.apply();
        game.getBatch().setProjectionMatrix(hudViewport.getCamera().combined);
        game.getBatch().begin();
        game.getFont().setColor(Color.WHITE);
        game.getFont().getData().setScale(1.0f);

        game.getFont().draw(game.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 580);
        game.getFont().draw(game.getBatch(), String.format("Pos: (%.1f, %.1f)", player.getX(), player.getY()), 20, 555);
        game.getFont().draw(game.getBatch(), "[WASD / Arrows] Move | [ESC] Menu", 20, 530);

        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hudViewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        player.dispose();
    }
}
