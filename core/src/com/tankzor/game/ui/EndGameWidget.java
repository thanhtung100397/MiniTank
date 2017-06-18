package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.common_value.MissionRecorder;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.gamehud.GameHUD;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 12/26/2016.
 */

public class EndGameWidget extends Group implements InputProcessor{
    public static final String WIN_CONTENT = "MISSION COMPLETED!";
    public static final String LOSE_CONTENT = "MISSION FAILED!";
    public static final int WIN_ID = 0;
    public static final int LOSE_ID = 1;

    private Tankzor parent;
    private Label contentLabel;
    private int currentMode;
    private GameHUD gameHUD;
    private boolean isOver = false;

    public EndGameWidget(Tankzor parent, GameHUD gameHUD) {
        this.parent = parent;
        this.gameHUD = gameHUD;

        int widthScreen = Gdx.graphics.getWidth();
        int heightScreen = Gdx.graphics.getHeight();
        setBounds(0, (heightScreen - Dimension.endGameWidgetHeight) / 2, widthScreen, Dimension.endGameWidgetHeight);

        Image image = new Image(GameImages.getInstance().getUiSkin().getDrawable(GameImages.KEY_END_GAME_WIDGET_BACKGROUND));
        image.setBounds(0, 0, getWidth(), getHeight());
        addActor(image);

        contentLabel = new Label("", GameImages.getInstance().getLabelStyle());
        contentLabel.setAlignment(Align.center);
        contentLabel.setFontScale(Dimension.normalFontScale);
        contentLabel.setBounds(0, (getHeight() - contentLabel.getPrefHeight()) / 2, widthScreen, contentLabel.getPrefHeight());
        addActor(contentLabel);

        Label label2 = new Label("Touch to exit", GameImages.getInstance().getLabelStyle());
        label2.setFontScale(Dimension.smallFontScale);
        label2.setBounds(getWidth() - label2.getWidth() - 5, 5, label2.getWidth(), label2.getHeight());
        addActor(label2);
    }

    public void setMode(int id){
        switch (id){
            case WIN_ID:{
                currentMode = id;
                contentLabel.setText(WIN_CONTENT);
            }
            break;

            case LOSE_ID:{
                currentMode = id;
                contentLabel.setText(LOSE_CONTENT);
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (currentMode){
            case WIN_ID:
            case LOSE_ID: {
                parent.getGameScreen().endGame();
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                MissionRecorder missionRecorder = playerProfile.endMissionRecorder();
                missionRecorder.isComplete = currentMode == WIN_ID;
                MissionResultScreen missionResultScreen = parent.getMissionResultScreen();
                missionResultScreen.setMissionRecorder(PlayerProfile.getInstance().endMissionRecorder());
                gameHUD.hideEndGameWidget();
                GameSounds.getInstance().stopBGM();
                parent.setScreen(missionResultScreen);
                playerProfile.savePlayerData();
            }
            break;

            default:{
                break;
            }
        }
        isOver = false;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
    }
}
