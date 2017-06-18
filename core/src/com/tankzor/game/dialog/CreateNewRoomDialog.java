package com.tankzor.game.dialog;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;

import java.util.HashMap;

/**
 * Created by Admin on 6/8/2017.
 */

public class CreateNewRoomDialog extends BaseDialog {
    private static final int MAX_PLAYER = 4;
    private static final int MIN_PLAYER = 1;
    private TextField roomNameField;
    private int maxPlayer = MIN_PLAYER;
    private Label maxPlayerNumberLabel;
    private String ownerName;

    public CreateNewRoomDialog(String ownerName) {
        canDismissWithoutAction = true;
        this.ownerName = ownerName;
    }

    @Override
    protected void initViews() {
        GameImages gameImages = GameImages.getInstance();
        Window window = getWindow();

        window.getTitleLabel().setText("Create New Room");

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = gameImages.getUiSkin().getDrawable(GameImages.KEY_TEXT_FIELD_BACKGROUND);
        textFieldStyle.background.setMinWidth(Dimension.buttonWidth);
        textFieldStyle.background.setMinHeight(Dimension.buttonHeight);
        textFieldStyle.font = gameImages.getGameFont();
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.disabledFontColor = Color.DARK_GRAY;
        roomNameField = new TextField("", textFieldStyle);
        roomNameField.setMessageText("Enter room name");
        roomNameField.setAlignment(Align.center);
        roomNameField.setMaxLength(32);

        Label maxPlayerLabel = new Label("Player ", gameImages.getLabelStyle());
        maxPlayerLabel.setFontScale(Dimension.normalFontScale);
        maxPlayerNumberLabel = new Label(MIN_PLAYER+"", gameImages.getLabelStyle());
        maxPlayerLabel.setAlignment(Align.center);
        maxPlayerLabel.setFontScale(Dimension.normalFontScale);

        TextButton.TextButtonStyle changeButtonStyle = new TextButton.TextButtonStyle();
        float buttonSize = 48;
        changeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.NORMAL_SMALL_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        changeButtonStyle.up.setMinWidth(buttonSize);
        changeButtonStyle.up.setMinHeight(buttonSize);
        changeButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.PRESS_SMALL_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        changeButtonStyle.down.setMinWidth(buttonSize);
        changeButtonStyle.down.setMinHeight(buttonSize);
        changeButtonStyle.font = GameImages.getInstance().getGameFont();
        changeButtonStyle.fontColor = Color.WHITE;
        changeButtonStyle.overFontColor = Color.LIGHT_GRAY;
        TextButton addButton = new TextButton("+", changeButtonStyle);
        addButton.getLabel().setFontScale(Dimension.normalFontScale);
        addButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(maxPlayer < MAX_PLAYER) {
                    maxPlayer++;
                    maxPlayerNumberLabel.setText(maxPlayer+"");
                }
            }
        });
        TextButton subButton = new TextButton("-", changeButtonStyle);
        subButton.getLabel().setFontScale(Dimension.normalFontScale);
        subButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(maxPlayer > MIN_PLAYER) {
                    maxPlayer--;
                    maxPlayerNumberLabel.setText(maxPlayer+"");
                }
            }
        });


        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.BUTTON_BACKGROUND_IMAGE_NAME)));
        buttonStyle.down.setMinWidth(Dimension.buttonWidth / 2);
        buttonStyle.down.setMinHeight(Dimension.buttonHeight);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        final TextButton createRoomButton = new TextButton("Create Room", buttonStyle);
        createRoomButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                String name = roomNameField.getText();
                if(name.equals("")){
                    return;
                }
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("result","");
                try {
                    WarpClient.getInstance().createRoom(name, ownerName, maxPlayer, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
                onStartCreateRoom();
            }
        });

        TextButton cancelButton = new TextButton("Cancel", buttonStyle);
        cancelButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dismiss();
            }
        });

        window.add(roomNameField).padBottom(20).fillX().colspan(2).padRight(20).padLeft(20).padBottom(30).row();
        window.add(maxPlayerLabel).padLeft(20).padRight(10).padBottom(40);
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.space(15);
        horizontalGroup.addActor(subButton);
        horizontalGroup.addActor(maxPlayerNumberLabel);
        horizontalGroup.addActor(addButton);
        window.add(horizontalGroup).padBottom(40).row();
        window.add(createRoomButton).padRight(10);
        window.add(cancelButton);
    }

    protected void onStartCreateRoom(){

    }
}