package com.tankzor.game.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.dialog.DialogManager;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 12/27/2016.
 */

public abstract class BaseScreen implements Screen {
    private SpriteBatch batch;
    private Tankzor parent;
    private Viewport viewport;
    private BaseScreen previousScreen;
    protected InputMultiplexer gameMultiplexer;

    public BaseScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameMultiplexer) {
        this.parent = parent;
        this.viewport = viewport;
        this.batch = batch;
        this.gameMultiplexer = gameMultiplexer;
        initViews();
        addViews();
    }

    protected abstract void initViews();

    public void setPreviousScreen(BaseScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    public BaseScreen getPreviousScreen() {
        return previousScreen;
    }

    protected abstract void addViews();

    public Tankzor getParent() {
        return parent;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resize(int width, int height) {

    }

    public void onBackPress() {
        if (!DialogManager.getInstance().dismissTopDialog() && previousScreen != null) {
            getParent().setScreen(previousScreen);
        }
    }
}
