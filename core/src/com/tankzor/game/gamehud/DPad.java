package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 1/26/2017.
 */

public class DPad extends Table {
    private TouchPadInputEventListener eventListener;

    public DPad() {
        Skin skin = GameImages.getInstance().getUiSkin();

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        Drawable drawable = skin.getDrawable(GameImages.KEY_NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;

        drawable = skin.getDrawable(GameImages.KEY_PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;

        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_UP_ICON);

        ImageButton upButton = new ImageButton(imageButtonStyle);
        upButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.UP_ORIENT, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.UP_ORIENT, false);
            }
        });

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;

        drawable = skin.getDrawable(GameImages.KEY_PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;

        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_LEFT_ICON);

        ImageButton leftButton = new ImageButton(imageButtonStyle);
        leftButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.LEFT_ORIENT, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.LEFT_ORIENT, false);
            }
        });

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;

        drawable = skin.getDrawable(GameImages.KEY_PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_BACK_ICON);

        ImageButton backButton = new ImageButton(imageButtonStyle);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(PlayerWarMachine.BACK_UP, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(PlayerWarMachine.BACK_UP, false);
            }
        });

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;

        drawable = skin.getDrawable(GameImages.KEY_PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_RIGHT_ICON);

        ImageButton rightButton = new ImageButton(imageButtonStyle);
        rightButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.RIGHT_ORIENT, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.RIGHT_ORIENT, false);
            }
        });

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;

        drawable = skin.getDrawable(GameImages.KEY_PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.mediumCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.mediumCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_DOWN_ICON);

        ImageButton downButton = new ImageButton(imageButtonStyle);
        downButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.DOWN_ORIENT, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                eventListener.onTouchDPadEvent(WarMachine.DOWN_ORIENT, false);
            }
        });
        
        setSize(Dimension.DPADSize, Dimension.DPADSize);

        add(upButton).align(Align.center).colspan(3).row();
        add(leftButton).align(Align.left);
        add(backButton).align(Align.center).pad(Dimension.buttonSpace);
        add(rightButton).align(Align.right).row();
        add(downButton).align(Align.center).colspan(3);

        setTouchable(Touchable.enabled);
    }

    public void setInputListener(TouchPadInputEventListener inputListener) {
        this.eventListener = inputListener;
    }
}
