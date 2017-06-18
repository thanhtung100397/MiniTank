package com.tankzor.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 1/5/2017.
 */
public class CircleProgressBar extends Actor {
    private TextureRegion image;
    private float originX, originY;
    private float rotation;
    private boolean isRunning = false;

    public CircleProgressBar() {
        image = GameImages.getInstance().getLoadingCircleImage();
        setSize(image.getRegionWidth(), image.getRegionHeight());
        originX = getX() + getWidth() / 2;
        originX = getY() + getHeight() / 2;
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        originX = width / 2;
        originY = height / 2;
    }

    public void start() {
        isRunning = true;
    }

    public void stopAndRewind() {
        isRunning = false;
        rotation = 0;
    }

    @Override
    public void act(float delta) {
        if (isRunning) {
            rotation -= 5;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image, getX(), getY(), originX, originY, getWidth(), getHeight(), 1.0f, 1.0f, rotation);
    }
}
