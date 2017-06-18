package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.util.XmlMapReader;

/**
 * Created by Admin on 12/27/2016.
 */

public class ListMissionScreen extends BaseScreen {
    private Stage screenStage;
    private Image background;
    private Image lineImage;
    private Label tileLabel;
    private TextButton backButton;
    private Array<MissionItem> listMissions;
    private Array<MissionButton> listButtons;
    private float xTouchDown, yTouchDown;
    private boolean isDragged;
    private int currentMissionIndex = -1;
    private ScrollPane scrollPane;

    public ListMissionScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        int widthScreen = Gdx.graphics.getWidth();
        int heightScreen = Gdx.graphics.getHeight();

        Skin skin = GameImages.getInstance().getUiSkin();

        background = new Image(skin.getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, widthScreen, heightScreen);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.background = skin.getDrawable(GameImages.KEY_LABEL_BACKGROUND);
        labelStyle.font = GameImages.getInstance().getGameFont();

        tileLabel = new Label("Missions", labelStyle);
        tileLabel.setAlignment(Align.center);
        tileLabel.setFontScale(Dimension.normalFontScale);
        tileLabel.setBounds(0, heightScreen - Dimension.screenTitleHeight, widthScreen, Dimension.screenTitleHeight);

        InputListener inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                xTouchDown = x;
                yTouchDown = y;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragged) {
                    isDragged = false;
                    return;
                }
                currentMissionIndex = ((MissionButton) event.getTarget().getParent()).missionItem.index;
                showObjectiveScreen(listMissions.get(currentMissionIndex));
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Math.abs(xTouchDown - x) > 10 || Math.abs(yTouchDown - y) > 10) {
                    isDragged = true;
                }
            }
        };

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        listMissions = XmlMapReader.readListMissionXml();
        listButtons = new Array<MissionButton>();
        for (int i = 0; i < listMissions.size; i++) {
            listMissions.get(i).index = i;
            MissionButton missionButton = new MissionButton(listMissions.get(i), buttonStyle);
            missionButton.addListener(inputListener);
            listButtons.add(missionButton);
        }

        Drawable lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        lineImage = new Image(lineDrawable);

        backButton = new TextButton("Back to Main Menu", buttonStyle);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getParent().setScreen(getParent().getMenuScreen());
            }
        });
    }

    public MissionItem nextMission(){
        if(currentMissionIndex == listMissions.size - 1){
            currentMissionIndex = -1;
        }
        return listMissions.get(++currentMissionIndex);
    }

    public void showObjectiveScreen(MissionItem missionItem) {
        Tankzor parent = getParent();
        ObjectiveScreen objectiveScreen = parent.getObjectiveScreen();
        objectiveScreen.setContent(missionItem);
        parent.setScreen(objectiveScreen);
    }

    @Override
    public void show() {
//        scrollPane.scrollTo(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameMultiplexer.addProcessor(screenStage);
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(background);
        screenStage.addActor(tileLabel);

        VerticalGroup missionGroup = new VerticalGroup();
        missionGroup.fill();
        for (MissionButton missionButton : listButtons) {
            missionGroup.addActor(missionButton);
        }
        missionGroup.space(Dimension.buttonSpace);
        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.space(30);
        buttonGroup.addActor(lineImage);
        buttonGroup.addActor(backButton);
        missionGroup.addActor(buttonGroup);

        scrollPane = new ScrollPane(missionGroup);
        scrollPane.setBounds(20, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - tileLabel.getHeight());
        scrollPane.setTouchable(Touchable.enabled);

        screenStage.addActor(scrollPane);
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

    private class MissionButton extends TextButton {
        private MissionItem missionItem;

        public MissionButton(MissionItem missionItem, TextButtonStyle style) {
            super(missionItem.name, style);
            this.missionItem = missionItem;
            Label buttonLabel = getLabel();
            buttonLabel.setAlignment(Align.left);
            buttonLabel.setFontScale(Dimension.normalFontScale);
        }
    }

    public static class MissionItem {
        private int index;
        public String name;
        public String hint;
        public String objective;

        public MissionItem(String name, String hint, String objective) {
            this.name = name;
            this.hint = hint;
            this.objective = objective;
        }

        public int getIndex() {
            return index;
        }

        public int getLevel() {
            return index + 1;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
