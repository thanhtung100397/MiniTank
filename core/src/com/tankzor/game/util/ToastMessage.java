package com.tankzor.game.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 1/25/2017.
 */

public class ToastMessage extends Actor {
    private BitmapFont font;
    private String content;
    private float x, y;

    public ToastMessage(String content, float duration, float x, float y) {
        font = GameImages.getInstance().getGameFont();
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, content, x, y);
    }
}
