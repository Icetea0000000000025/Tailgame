package com.gdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.game.GdxGame;

public class MainMenuScreen implements Screen {

    private final GdxGame game;
    private final Viewport viewport;
    private final GlyphLayout layout;

    public MainMenuScreen(GdxGame game) {
        this.game = game;
        this.viewport = new FitViewport(800, 600);
        this.layout = new GlyphLayout();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Clear background with dark slate blue
        Gdx.gl.glClearColor(0.1f, 0.12f, 0.18f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Input handling
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        viewport.apply();
        game.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        game.getBatch().begin();

        // Title
        game.getFont().setColor(Color.GOLD);
        game.getFont().getData().setScale(2.5f);
        layout.setText(game.getFont(), "MY RPG GAME");
        game.getFont().draw(game.getBatch(), "MY RPG GAME",
                (viewport.getWorldWidth() - layout.width) / 2f, 420);

        // Subtitle / Prompt
        game.getFont().setColor(Color.WHITE);
        game.getFont().getData().setScale(1.2f);
        String prompt = "Press [ SPACE ] or [ ENTER ] to Start";
        layout.setText(game.getFont(), prompt);
        game.getFont().draw(game.getBatch(), prompt,
                (viewport.getWorldWidth() - layout.width) / 2f, 280);

        // Controls hint
        game.getFont().setColor(Color.LIGHT_GRAY);
        game.getFont().getData().setScale(1.0f);
        String controls = "Move: W, A, S, D or Arrow Keys | Exit: ESC";
        layout.setText(game.getFont(), controls);
        game.getFont().draw(game.getBatch(), controls,
                (viewport.getWorldWidth() - layout.width) / 2f, 180);

        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
