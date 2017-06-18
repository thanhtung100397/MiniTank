package com.tankzor.game.dialog;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 6/9/2017.
 */

public abstract class EnterNameDialog extends BaseDialog {
    private TextField nameField;

    public EnterNameDialog() {
        getWindow().getTitleLabel().setText("Enter Name");
    }

    @Override
    protected void initViews() {
        GameImages gameImages = GameImages.getInstance();
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = gameImages.getUiSkin().getDrawable(GameImages.KEY_TEXT_FIELD_BACKGROUND);
        textFieldStyle.background.setMinWidth(Dimension.buttonWidth);
        textFieldStyle.background.setMinHeight(Dimension.buttonHeight);
        textFieldStyle.font = gameImages.getGameFont();
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.disabledFontColor = Color.DARK_GRAY;

        nameField = new TextField("", textFieldStyle);
        nameField.setMessageText("Enter your name");
        nameField.setAlignment(Align.center);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.BUTTON_BACKGROUND_IMAGE_NAME)));
        buttonStyle.down.setMinWidth(Dimension.buttonWidth);
        buttonStyle.down.setMinHeight(Dimension.buttonHeight);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;
        TextButton confirmButton = new TextButton("Confirm", buttonStyle);
        confirmButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                String name = nameField.getText();
                if(!name.equals("")){
                    onNameConfirmed(name);
                    dismiss();
                }
            }
        });

        Window window = getWindow();
        window.add(nameField).pad(30).fillX().row();
        window.add(confirmButton);
    }

    protected abstract void onNameConfirmed(String name);
}