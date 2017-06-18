package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 4/29/2017.
 */

public class FPSDisplay {
    public static boolean isEnable = true;
    private float time = 0;
    private Label fpsDisplay;
    private Camera camera;
    private Batch batch;

    public FPSDisplay(Batch batch, OrthographicCamera camera) {
        this.camera = camera;
        this.batch = batch;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = GameImages.getInstance().getGameFont();
        labelStyle.fontColor = Color.GREEN;
        fpsDisplay = new Label("", labelStyle);
        fpsDisplay.setFontScale(2.0f);
        fpsDisplay.setPosition(Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() - 30);
    }

    public void updateAndRender() {
        if (!isEnable) {
            return;
        }

        //update
        if (time >= 1) {
            time = 0;
            fpsDisplay.setText(Gdx.graphics.getFramesPerSecond() + "");
        }
        time += Gdx.graphics.getDeltaTime();

        //render
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        fpsDisplay.draw(batch, 1);
        batch.end();
    }
}
