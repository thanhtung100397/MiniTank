package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 1/25/2017.
 */

public class PauseScreen extends BaseScreen {
    private Stage screenStage;
    private TankBackground tankBackground;
    private TextButton exitMissionsButton, workshopButton, resumeButton, settingsButton;

    public PauseScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        tankBackground = new TankBackground(100);

        final GameImages gameImages = GameImages.getInstance();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.down = gameImages.getUiSkin().getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        textButtonStyle.font = gameImages.getGameFont();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.disabledFontColor = Color.LIGHT_GRAY;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;

        resumeButton = new TextButton("Resume", textButtonStyle);
        resumeButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        resumeButton.getLabel().setFontScale(Dimension.normalFontScale);
        resumeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Tankzor parent = getParent();
                parent.setScreen(parent.getGameScreen());
            }
        });

        workshopButton = new TextButton("Workshop", textButtonStyle);
        workshopButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        workshopButton.getLabel().setFontScale(Dimension.normalFontScale);
        workshopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(workshopButton.isDisabled()){
                    return;
                }
                WorkshopScreen workshopScreen = getParent().getWorkshopScreen();
                GameScreen gameScreen = getParent().getGameScreen();
                workshopScreen.setWarMachineManager(gameScreen.getWarMachineManager());
                workshopScreen.setWeaponManager(gameScreen.getWeaponManager());
                workshopScreen.setPreviousScreen(PauseScreen.this);
                getParent().setScreen(workshopScreen);
            }
        });

        settingsButton = new TextButton("Settings", textButtonStyle);
        settingsButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        settingsButton.getLabel().setFontScale(Dimension.normalFontScale);
        settingsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                SettingsScreen settingsScreen = getParent().getSettingsScreen();
                settingsScreen.setPreviousScreen(PauseScreen.this);
                getParent().setScreen(settingsScreen);
            }
        });

        exitMissionsButton = new TextButton("Exit Mission", textButtonStyle);
        exitMissionsButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        exitMissionsButton.getLabel().setFontScale(Dimension.normalFontScale);
        exitMissionsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Tankzor parent = getParent();
                parent.getGameScreen().endGame();
                parent.setScreen(parent.getListMissionScreen());
            }
        });

    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(tankBackground);

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.space(10);
        verticalGroup.addActor(resumeButton);
        verticalGroup.addActor(workshopButton);
        verticalGroup.addActor(settingsButton);
        verticalGroup.addActor(exitMissionsButton);
        verticalGroup.align(Align.center);
        verticalGroup.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        screenStage.addActor(verticalGroup);
    }

    @Override
    public void render(float delta) {
        screenStage.act();
        screenStage.draw();
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }

    @Override
    public void show() {
        gameMultiplexer.addProcessor(screenStage);
        if (getParent().getGameScreen().getPlayerCamera().getPlayerWarMachine().isOnSpawnLocation()) {
            workshopButton.setDisabled(false);
        }else {
            workshopButton.setDisabled(true);
        }
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
    }
}
