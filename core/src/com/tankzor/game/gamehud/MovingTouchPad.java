package com.tankzor.game.gamehud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 11/13/2016.
 */

public class MovingTouchPad extends Touchpad {
    public static final float PERCENT_BOUND = (float) (Math.sqrt(2) / 2);
    private TouchPadInputEventListener inputListener;

    public MovingTouchPad(float deadzoneRadius, TouchpadStyle style) {
        super(deadzoneRadius, style);
    }

    public void setInputListener(TouchPadInputEventListener inputListener) {
        this.inputListener = inputListener;
    }

    public static class MovingTouchPadBuilder {
        private TouchpadStyle touchPadStyle;

        public MovingTouchPadBuilder() {
            Skin skin = GameImages.getInstance().getUiSkin();
            touchPadStyle = new TouchpadStyle();
            touchPadStyle.background = skin.getDrawable(GameImages.KEY_NORMAL_BIG_DPAD_BUTTON_BACKGROUND);
            touchPadStyle.knob = skin.getDrawable(GameImages.KEY_NORMAL_SMALL_DPAD_BUTTON_BACKGROUND);
        }

        public MovingTouchPad build() {
            return new MovingTouchPad(10, touchPadStyle);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(inputListener != null){
            inputListener.onTouchGesturePad(this);
        }
    }
}
