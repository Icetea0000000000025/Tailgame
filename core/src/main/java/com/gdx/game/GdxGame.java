package com.gdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.screens.MainMenuScreen;

public class GdxGame extends Game {

    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Default LibGDX font
        shapeRenderer = new ShapeRenderer();

        // Start at Main Menu
        setScreen(new MainMenuScreen(this));
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
