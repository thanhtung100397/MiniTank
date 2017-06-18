package com.tankzor.game.dialog;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 6/7/2017.
 */

public abstract class NotificationDialog extends BaseDialog {
    private Label messageLabel;
    private TextButton actionButton;

    public NotificationDialog(String title){
        getWindow().getTitleLabel().setText(title);
    }

    public void setMessage(String message){
        messageLabel.setText(message);
    }

    @Override
    protected void initViews() {
        Window window = getWindow();

        GameImages gameImages = GameImages.getInstance();
        Label.LabelStyle labelStyle = gameImages.getLabelStyle();
        messageLabel = new Label("",labelStyle);
        messageLabel.setFontScale(Dimension.normalFontScale);
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);
        window.add(messageLabel).padTop(10).padBottom(10).fillX().row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = gameImages.getUiSkin().getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.down.setMinWidth(Dimension.buttonWidth);
        buttonStyle.down.setMinHeight(Dimension.buttonHeight);
        buttonStyle.font = gameImages.getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;
        actionButton = new TextButton("Action", buttonStyle);
        actionButton.getLabel().setFontScale(Dimension.normalFontScale);
        actionButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onActionButtonPress();
            }
        });
        window.add(actionButton).align(Align.bottom);
    }

    public void setActionButtonLabel(String label){
        actionButton.setText(label);
    }

    protected abstract void onActionButtonPress();
}
