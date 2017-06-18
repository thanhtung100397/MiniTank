package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 12/27/2016.
 */
public class MenuScreen extends BaseScreen {
    public static final String BUTTON_SHOP_LABEL = "Workshop";
    private Stage screenStage;
    private TextButton campaignButton;
    private TextButton onlineMatchButton;
    private TextButton workshopButton;
    private TextButton settingButton;
    private TankBackground tankBackground;

    public MenuScreen(Tankzor parent, Viewport viewport, SpriteBatch gameBatch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, gameBatch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        Skin skin = GameImages.getInstance().getUiSkin();

        tankBackground = new TankBackground();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.down.setMinWidth(Dimension.buttonWidth);
        buttonStyle.down.setMinHeight(Dimension.buttonHeight);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.disabledFontColor = Color.DARK_GRAY;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        campaignButton = new TextButton("Campaign", buttonStyle);
        campaignButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        campaignButton.getLabel().setFontScale(Dimension.normalFontScale);
        campaignButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Tankzor parent = getParent();
                parent.setScreen(parent.getListMissionScreen());
            }
        });

        onlineMatchButton = new TextButton("Online Match", buttonStyle);
        onlineMatchButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        onlineMatchButton.getLabel().setFontScale(Dimension.normalFontScale);
        onlineMatchButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Tankzor parent = getParent();
                ListRoomScreen listRoomScreen = parent.getListRoomScreen();
                try {
                    listRoomScreen.initAppWarp();
                    listRoomScreen.showEnterNameDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                parent.setScreen(listRoomScreen);
            }
        });

        workshopButton = new TextButton(BUTTON_SHOP_LABEL, buttonStyle);
        workshopButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        workshopButton.getLabel().setFontScale(Dimension.normalFontScale);
        workshopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Tankzor parent = getParent();
                WorkshopScreen workshopScreen = parent.getWorkshopScreen();
                workshopScreen.setPreviousScreen(MenuScreen.this);
                parent.setScreen(workshopScreen);
            }
        });

        settingButton = new TextButton("Settings", buttonStyle);
        settingButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        settingButton.getLabel().setFontScale(Dimension.normalFontScale);
        settingButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Tankzor parent = getParent();
                SettingsScreen settingsScreen = parent.getSettingsScreen();
                settingsScreen.setPreviousScreen(MenuScreen.this);
                parent.setScreen(parent.getSettingsScreen());
            }
        });
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(tankBackground);

        int widthScreen = Gdx.graphics.getWidth();
        int heightScreen = Gdx.graphics.getHeight();

        Table table = new Table();
        table.add(campaignButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.add(onlineMatchButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.add(workshopButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.add(settingButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.setBounds(0, 0, widthScreen, heightScreen);

        screenStage.addActor(table);
    }

    @Override
    public void show() {
        super.show();
        gameMultiplexer.addProcessor(screenStage);
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
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
}
