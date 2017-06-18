package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 12/27/2016.
 */

public class ObjectiveScreen extends BaseScreen {
    private static final String BUTTON_START_MISSION_LABEL = "Start Mission";
    private static final String BUTTON_BACK_LABEL = "Back to Missions List";

    private Stage screenStage;
    private Image lineImage;
    private TextButton startMissionButton;
    private TextButton workshopButton;
    private TextButton backButton;
    private Label missionNameLabel;
    private Label missionHintLabel;
    private Label missionObjectiveLabel;
    private TankBackground tankBackground;
    private ScrollPane scrollPane;
    private ListMissionScreen.MissionItem missionItem;

    public ObjectiveScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        Skin skin = GameImages.getInstance().getUiSkin();

        tankBackground = new TankBackground();

        missionNameLabel = new Label("", GameImages.getInstance().getLabelStyle());
        missionNameLabel.setFontScale(Dimension.largeFontScale);

        missionHintLabel = new Label("", GameImages.getInstance().getLabelStyle());
        missionHintLabel.setFontScale(Dimension.normalFontScale);
        missionHintLabel.setWrap(true);

        missionObjectiveLabel = new Label("", GameImages.getInstance().getLabelStyle());
        missionObjectiveLabel.setFontScale(Dimension.normalFontScale);
        missionObjectiveLabel.setWrap(true);

        Drawable lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        lineImage = new Image(lineDrawable);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        startMissionButton = new TextButton(BUTTON_START_MISSION_LABEL, buttonStyle);
        startMissionButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        startMissionButton.getLabel().setFontScale(Dimension.normalFontScale);
        startMissionButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                GameScreen gameScreen = getParent().getGameScreen();
                PlayerProfile.getInstance().prepareMissionRecorder(missionItem.name);
                gameScreen.initAndStartCampaignMap(missionItem.getLevel());
            }
        });

        workshopButton = new TextButton(MenuScreen.BUTTON_SHOP_LABEL, buttonStyle);
        workshopButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        workshopButton.getLabel().setFontScale(Dimension.normalFontScale);
        workshopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                WorkshopScreen workshopScreen = getParent().getWorkshopScreen();
                workshopScreen.setPreviousScreen(ObjectiveScreen.this);
                getParent().setScreen(workshopScreen);
            }
        });

        backButton = new TextButton(BUTTON_BACK_LABEL, buttonStyle);
        backButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getParent().setScreen(getPreviousScreen());
            }
        });
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(tankBackground);

        float widthScreen = Gdx.graphics.getWidth();
        float heightScreen = Gdx.graphics.getHeight();

        VerticalGroup parentGroup = new VerticalGroup();
        parentGroup.fill();
        parentGroup.space(Dimension.padding);
        parentGroup.addActor(missionNameLabel);
        parentGroup.addActor(missionHintLabel);
        parentGroup.addActor(missionObjectiveLabel);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.padTop(Dimension.buttonGroupMargin);
        buttonGroup.space(Dimension.buttonSpace);
        buttonGroup.addActor(lineImage);
        buttonGroup.addActor(startMissionButton);
        buttonGroup.addActor(workshopButton);
        buttonGroup.addActor(backButton);
        parentGroup.addActor(buttonGroup);

        scrollPane = new ScrollPane(parentGroup);
        scrollPane.setBounds(Dimension.padding, 0, widthScreen, heightScreen - Dimension.padding - Dimension.tankPictureBackgroundHeight);
        screenStage.addActor(scrollPane);
    }

    public void setContent(ListMissionScreen.MissionItem missionItem){
        this.missionItem = missionItem;
        missionNameLabel.setText(missionItem.name);
        missionHintLabel.setText(missionItem.hint);
        missionObjectiveLabel.setText(missionItem.objective);
    }

    @Override
    public void show() {
        gameMultiplexer.addProcessor(screenStage);
//        scrollPane.scrollTo(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
