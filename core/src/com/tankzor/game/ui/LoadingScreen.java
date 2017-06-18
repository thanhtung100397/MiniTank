package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 1/5/2017.
 */

public class LoadingScreen extends BaseScreen {
    private Stage screenStage;
    private CircleProgressBar progressBar;
    private Skin skin;
    private Image background;

    public LoadingScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        skin = new Skin();

        int widthScreen = Gdx.graphics.getWidth();
        int heightScreen = Gdx.graphics.getHeight();

        background = new Image(GameImages.getInstance().getUiSkin().getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, widthScreen, heightScreen);

        progressBar = new CircleProgressBar();
        progressBar.setBounds(widthScreen - progressBar.getWidth() - 10, 10, progressBar.getWidth(), progressBar.getHeight());
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(background);
        screenStage.addActor(progressBar);
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(false);
        progressBar.start();
    }

    @Override
    public void hide() {
        progressBar.stopAndRewind();
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        screenStage.act();
        screenStage.draw();
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}
