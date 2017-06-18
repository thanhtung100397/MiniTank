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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.MissionRecorder;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.main.Tankzor;

import javafx.scene.Parent;

/**
 * Created by Admin on 5/9/2017.
 */

public class MissionResultScreen extends BaseScreen {
    Stage screenStage;
    private TankBackground tankBackground;
    private Label titleLabel, missionName, moneyLabel, money, starLabel, star, addOnLabel, addOn;
    private Label playTimeLabel, playTime, unitDestroyedLabel, unitDestroyed, shotCountLabel, shotCount;
    private Label takenDamageLabel, takenDamage, travelDistanceLabel, travelDistance;
    private TextButton backButton, nextMissionButton;
    private Image lineImage;
    private ScrollPane scrollPane;
    private VerticalGroup rootGroup;

    public MissionResultScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        tankBackground = new TankBackground();

        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

        titleLabel = new Label("", labelStyle);
        titleLabel.setFontScale(Dimension.largeFontScale);

        missionName = new Label("", labelStyle);
        missionName.setFontScale(Dimension.normalFontScale);

        moneyLabel = new Label("Coin total: ", labelStyle);
        moneyLabel.setFontScale(Dimension.normalFontScale);
        money = new Label("0", labelStyle);
        money.setFontScale(Dimension.normalFontScale);

        addOnLabel = new Label("Add-on: ", labelStyle);
        addOnLabel.setFontScale(Dimension.normalFontScale);
        addOn = new Label("0", labelStyle);
        addOn.setFontScale(Dimension.normalFontScale);

        starLabel = new Label("Star total: ", labelStyle);
        starLabel.setFontScale(Dimension.normalFontScale);
        star = new Label("0", labelStyle);
        star.setFontScale(Dimension.normalFontScale);

        playTimeLabel = new Label("Mission time: ", labelStyle);
        playTimeLabel.setFontScale(Dimension.normalFontScale);
        playTime = new Label("0", labelStyle);
        playTime.setFontScale(Dimension.normalFontScale);

        unitDestroyedLabel = new Label("Units destroyed: ", labelStyle);
        unitDestroyedLabel.setFontScale(Dimension.normalFontScale);
        unitDestroyed = new Label("0", labelStyle);
        unitDestroyed.setFontScale(Dimension.normalFontScale);

        shotCountLabel = new Label("Shots: ", labelStyle);
        shotCountLabel.setFontScale(Dimension.normalFontScale);
        shotCount = new Label("0", labelStyle);
        shotCount.setFontScale(Dimension.normalFontScale);

        takenDamageLabel = new Label("Taken damage: ", labelStyle);
        takenDamageLabel.setFontScale(Dimension.normalFontScale);
        takenDamage = new Label("0", labelStyle);
        takenDamage.setFontScale(Dimension.normalFontScale);

        travelDistanceLabel = new Label("Travel distance: ", labelStyle);
        travelDistanceLabel.setFontScale(Dimension.normalFontScale);
        travelDistance = new Label("0", labelStyle);
        travelDistance.setFontScale(Dimension.normalFontScale);

        Skin skin = GameImages.getInstance().getUiSkin();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        Drawable lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        lineImage = new Image(lineDrawable);

        nextMissionButton = new TextButton("Next Mission", buttonStyle);
        nextMissionButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        nextMissionButton.getLabel().setFontScale(Dimension.normalFontScale);
        nextMissionButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Tankzor parent = getParent();
                ListMissionScreen listMissionScreen = parent.getListMissionScreen();
                listMissionScreen.showObjectiveScreen(listMissionScreen.nextMission());
            }
        });

        backButton = new TextButton("Back to Missions List", buttonStyle);
        backButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.addListener(new InputListener() {
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
    }

    public void setMissionRecorder(MissionRecorder missionRecorder) {
        nextMissionButton.setDisabled(!missionRecorder.isComplete);
        titleLabel.setText(missionRecorder.isComplete ? "Mission Completed!" : "Mission Failed!");
        missionName.setText(missionRecorder.name);
        PlayerProfile playerProfile = PlayerProfile.getInstance();
        playerProfile.addMoney(missionRecorder.money);
        money.setText(playerProfile.getMoney() + " coin(s)");
        if(missionRecorder.isComplete) {
            float additionalInterest = playerProfile.getAdditionalInterest();
            addOn.setText((int) (playerProfile.getMoney() * additionalInterest) + "   (" + additionalInterest + "%)");
        }else {
            addOn.setText("0");
        }
        playerProfile.addStar(missionRecorder.star);
        star.setText((playerProfile.getStar()) + "");
        playTime.setText(missionRecorder.getPlayTime() + "");
        unitDestroyed.setText(missionRecorder.unitDestroyed + "");
        shotCount.setText(missionRecorder.shotCount + "");
        takenDamage.setText(missionRecorder.takenDamage + "");
        travelDistance.setText(missionRecorder.travelDistance + " m");
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(tankBackground);

        rootGroup = new VerticalGroup();
        rootGroup.fill();
        rootGroup.align(Align.left);
        rootGroup.pad(Dimension.buttonSpace * 2);

        rootGroup.addActor(titleLabel);
        rootGroup.addActor(missionName);

        Table table = new Table();
        table.align(Align.left);
        table.padTop(Dimension.buttonSpace * 2);
        table.add(moneyLabel).align(Align.left).width(500);
        table.add(money).align(Align.left);
        table.row();
        table.add(addOnLabel).align(Align.left).width(500);
        table.add(addOn).align(Align.left);
        table.row();
        table.add(starLabel).align(Align.left).width(500);
        table.add(star).align(Align.left);
        table.row();
        table.add(playTimeLabel).align(Align.left).width(500);
        table.add(playTime).align(Align.left);
        table.row();
        table.add(unitDestroyedLabel).align(Align.left).width(500);
        table.add(unitDestroyed).align(Align.left);
        table.row();
        table.add(shotCountLabel).align(Align.left).width(500);
        table.add(shotCount).align(Align.left);
        table.row();
        table.add(takenDamageLabel).align(Align.left).width(500);
        table.add(takenDamage).align(Align.left);
        table.row();
        table.add(travelDistanceLabel).align(Align.left).width(500);
        table.add(travelDistance).align(Align.left);
        table.row();
        rootGroup.addActor(table);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.padTop(Dimension.buttonSpace);
        buttonGroup.space(Dimension.buttonSpace);
        buttonGroup.addActor(lineImage);
        buttonGroup.addActor(nextMissionButton);
        buttonGroup.addActor(backButton);
        rootGroup.addActor(buttonGroup);

        scrollPane = new ScrollPane(rootGroup);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 100);
        scrollPane.setTouchable(Touchable.enabled);
        screenStage.addActor(scrollPane);
    }

    @Override
    public void show() {
        gameMultiplexer.addProcessor(screenStage);
        float heightScreen = Gdx.graphics.getHeight();
        scrollPane.scrollTo(0, rootGroup.getHeight() - heightScreen, Gdx.graphics.getWidth(), heightScreen);
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
