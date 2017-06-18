package com.tankzor.game.gamehud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.manager.WarMachineNumberListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.OnNewWeaponAddedListener;
import com.tankzor.game.game_object.movable_item.weapon.WeaponListener;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.game_object.support_item.BoostSpeedItem;
import com.tankzor.game.game_object.support_item.ForceField;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.ui.EndGameWidget;

/**
 * Created by Admin on 11/14/2016.
 */
public class GameHUD extends Stage implements WarMachineNumberListener, WeaponListener, OnNewWeaponAddedListener {
    public static final float TOAST_MESSAGE_VISIBLE_TIME = 3.0f;

    private DPad movingTouchPad;
    private ImageButton fireButton;
    private ImageButton switchButton;
    private ImageButton pauseButton;
    private ChargeButton boostSpeedButton;
    private ChargeButton fastRepairButton;
    private ImageButton flashLightButton;
    private HorizontalGroup playerHealDisplay;
    private HorizontalGroup otherHealDisplay;
    private InformationPanel informationPanel;
    private EndGameWidget endGameWidget;
    private Label messageLabel;
    private float currentToastMessageShowTime = -1;

    private PlayerWeaponMenu playerWeaponMenu;
    private TouchPadInputEventListener touchPadListener;

    private TouchScreenStage touchScreenStage;
    private InputMultiplexer gameHUDInputMultiplexer;
    private TerrainManager terrainManager;
    private Tankzor tankzor;
    private WeaponManager weaponManager;

    public GameHUD(Viewport viewport, Batch batch, Tankzor tankzor, WeaponManager weaponManager){
        super(viewport, batch);
        this.tankzor = tankzor;
        this.weaponManager = weaponManager;
        weaponManager.setWeaponListener(this);
        weaponManager.setWeaponUpdateListener(this);
    }

    public void initHUD(AssetLoader assetLoader){
        initViews(assetLoader);
        addViews();
    }

    public void setTouchPadInputEventListener(TouchPadInputEventListener listener) {
        this.touchPadListener = listener;
        movingTouchPad.setInputListener(listener);
    }

    public TouchScreenStage getTouchScreenStage() {
        return touchScreenStage;
    }

    private void initViews(AssetLoader assetLoader) {
        int widthScreen = Gdx.graphics.getWidth();
        int heightScreen = Gdx.graphics.getHeight();

        touchScreenStage = new TouchScreenStage(this, assetLoader);

        endGameWidget = new EndGameWidget(tankzor, this);
        endGameWidget.setVisible(false);

        float widthHealDisplay = Dimension.healPaneWidth * (WarMachine.MAX_HEAL_PANE_PER_ROW + ForceField.MAX_FORCE_FIELD_PANE_PER_ROW);
        float heightHealDisplay = Dimension.healPaneHeight;
        playerHealDisplay = new HorizontalGroup();
        playerHealDisplay.setBounds(Dimension.healPaneHeight,
                heightScreen - heightHealDisplay * 2,
                widthHealDisplay,
                heightHealDisplay);
        otherHealDisplay = new HorizontalGroup();
        otherHealDisplay.fill();
        otherHealDisplay.setBounds(Dimension.healPaneHeight,
                playerHealDisplay.getY() - heightHealDisplay * 5,
                widthHealDisplay,
                heightHealDisplay * 4);

        movingTouchPad = new DPad();
        movingTouchPad.setPosition(30, 30);

//        movingTouchPad = new MovingTouchPad.MovingTouchPadBuilder().build();
//        movingTouchPad.setBounds(15,
//                15,
//                movingTouchPad.getWidth(),
//                movingTouchPad.getHeight());

        Skin skin = GameImages.getInstance().getUiSkin();

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        Drawable drawable = skin.getDrawable(GameImages.KEY_NORMAL_BIG_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.bigCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.bigCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;

        drawable = skin.getDrawable(GameImages.KEY_PRESS_BIG_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.bigCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.bigCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_FIRE_BUTTON_ICON);

        fireButton = new ImageButton(imageButtonStyle);
        fireButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (touchPadListener != null) {
                    touchPadListener.onFireButtonInputEvent(true);
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (touchPadListener != null) {
                    touchPadListener.onFireButtonInputEvent(false);
                }
            }
        });
        fireButton.setBounds(widthScreen - Dimension.bigCircleButtonBackgroundSize - 35,
                35,
                Dimension.bigCircleButtonBackgroundSize,
                Dimension.bigCircleButtonBackgroundSize);

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_MENU_BUTTON_ICON);
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        imageButtonStyle.imageDown = drawable;

        drawable = skin.getDrawable(GameImages.KEY_PRESS_MENU_BUTTON_ICON);
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        imageButtonStyle.imageOver = drawable;
        imageButtonStyle.imageUp = drawable;

        pauseButton = new ImageButton(imageButtonStyle);
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playerWeaponMenu.getDisplayActiveItemPane().setVisible(true);
                playerWeaponMenu.getWeaponList().setVisible(false);
                tankzor.setScreen(tankzor.getPauseScreen());
            }
        });
        pauseButton.setBounds((widthScreen - Dimension.smallCircleButtonBackgroundSize) / 2,
                heightScreen - Dimension.smallCircleButtonBackgroundSize - 20,
                Dimension.smallCircleButtonBackgroundSize,
                Dimension.smallCircleButtonBackgroundSize);

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        imageButtonStyle.up = drawable;
        drawable = skin.getDrawable(GameImages.KEY_PRESS_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_FLASH_LIGHT_ICON);

        flashLightButton = new ImageButton(imageButtonStyle);
        flashLightButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchPadListener.onTouchFlashLight();
            }
        });
        flashLightButton.setBounds(fireButton.getX() + fireButton.getWidth() - Dimension.smallCircleButtonBackgroundSize,
                                fireButton.getY() + fireButton.getHeight() + 20,
                                Dimension.smallCircleButtonBackgroundSize,
                                Dimension.smallCircleButtonBackgroundSize);

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        imageButtonStyle.up = drawable;
        drawable = skin.getDrawable(GameImages.KEY_PRESS_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallIconSize);
        drawable.setMinHeight(Dimension.smallIconSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_SWITCH_BUTTON_ICON);
        switchButton = new ImageButton(imageButtonStyle);
        switchButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (touchPadListener != null) {
                    touchPadListener.onTouchSwitchWeaponsButton();
                }
            }
        });
        switchButton.setBounds(fireButton.getX(),
                fireButton.getY() + fireButton.getHeight() + 5,
                Dimension.smallCircleButtonBackgroundSize,
                Dimension.smallCircleButtonBackgroundSize);

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.smallCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;
        drawable = skin.getDrawable(GameImages.KEY_PRESS_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.smallCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.disabled = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_REPAIR_BUTTON_ICON);

        fastRepairButton = new ChargeButton(imageButtonStyle) {

            @Override
            protected void onPress() {
                if(doEffect()){
                    weaponItem.decrease(1);
                    if (weaponItem.weaponModel.capacity == 0){
                        weaponItem.remove();
                    }
                }else {
                    showToastMessage("Your tank HP is maximum");
                }
            }

            @Override
            public boolean doEffect() {
                PlayerWarMachine playerWarMachine = touchPadListener.getPlayerWarMachine();
                if (playerWarMachine == null || playerWarMachine.isDestroyed() || playerWarMachine.getHitPoint() == playerWarMachine.getMaxHitPoint()) {
                    return false;
                }
                playerWarMachine.setHitPoint(playerWarMachine.getMaxHitPoint());
                return true;
            }
        };
        fastRepairButton.setPosition(fireButton.getX() - Dimension.smallCircleButtonBackgroundSize - 5,
                fireButton.getY() + fireButton.getHeight() - Dimension.smallCircleButtonBackgroundSize);
        WeaponManager.WeaponItem repairItem = weaponManager.getWeaponItemByID(SupportItem.REPAIR_KIT);
        if(repairItem != null){
            fastRepairButton.setWeaponItem(repairItem);
        }

        imageButtonStyle = new ImageButton.ImageButtonStyle();
        drawable = skin.getDrawable(GameImages.KEY_NORMAL_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.smallCircleButtonBackgroundSize);
        imageButtonStyle.up = drawable;
        drawable = skin.getDrawable(GameImages.KEY_PRESS_SMALL_DPAD_BUTTON_BACKGROUND);
        drawable.setMinWidth(Dimension.smallCircleButtonBackgroundSize);
        drawable.setMinHeight(Dimension.smallCircleButtonBackgroundSize);
        imageButtonStyle.over = drawable;
        imageButtonStyle.down = drawable;
        imageButtonStyle.disabled = drawable;
        imageButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_BOOST_BUTTON_ICON);
        boostSpeedButton = new ChargeButton(imageButtonStyle) {

            @Override
            public boolean doEffect() {
                PlayerWarMachine playerWarMachine = touchPadListener.getPlayerWarMachine();
                if (playerWarMachine == null || playerWarMachine.isDestroyed()) {

                    return false;
                }
                BoostSpeedItem boostSpeedItem =
                        new BoostSpeedItem(playerWeaponMenu.getDisplayActiveItemPane().getSpeedCoolDownDisplay());
                terrainManager.addActor(boostSpeedItem);
                boostSpeedItem.addEffectTo(playerWarMachine);
                return true;
            }
        };
        boostSpeedButton.setPosition(fireButton.getX() - Dimension.smallCircleButtonBackgroundSize - 20,
                                    fireButton.getY());
        WeaponManager.WeaponItem boostSpeedItem = weaponManager.getWeaponItemByID(SupportItem.BOOST_SPEED);
        if(boostSpeedItem != null){
            boostSpeedButton.setWeaponItem(boostSpeedItem);
        }


        playerWeaponMenu = new PlayerWeaponMenu(playerHealDisplay.getX(), otherHealDisplay.getY() - Dimension.padding * 1.5f, weaponManager);

        informationPanel = new InformationPanel(assetLoader);

        messageLabel = new Label("", GameImages.getInstance().getLabelStyle());
        messageLabel.setFontScale(Dimension.normalFontScale);
        messageLabel.setAlignment(Align.center);
        messageLabel.setBounds(0, heightScreen - Dimension.messageLabelHeight * 2, widthScreen, Dimension.messageLabelHeight);
        messageLabel.setVisible(false);
    }

    public void showToastMessage(String content) {
        messageLabel.setText(content);
        messageLabel.setVisible(true);
        currentToastMessageShowTime = 0;
    }

    public void switchTouchScreen(boolean isEnable) {
        if (isEnable) {
            touchScreenStage.setFocus(true);
            tankzor.getGameScreen().pause();
            Gdx.input.setInputProcessor(touchScreenStage.getInputProcessor());
        } else {
            touchScreenStage.setFocus(false);
            tankzor.getGameScreen().resume();
            Gdx.input.setInputProcessor(tankzor.getGameInputMultiplexer());
        }
    }

    private void addViews() {
        cancelTouchFocus();
        addActor(fireButton);
        addActor(flashLightButton);
        addActor(switchButton);
        addActor(pauseButton);
        addActor(playerHealDisplay);
        addActor(otherHealDisplay);
        addActor(playerWeaponMenu);
        addActor(informationPanel);
        addActor(endGameWidget);
        addActor(messageLabel);
        addActor(fastRepairButton);
        addActor(boostSpeedButton);
        addActor(movingTouchPad);

        gameHUDInputMultiplexer = new InputMultiplexer();
        gameHUDInputMultiplexer.addProcessor(this);
    }

    public void showEndGameWidget(int mode) {
        if(endGameWidget.isOver()){
            return;
        }
        gameHUDInputMultiplexer.addProcessor(endGameWidget);
        endGameWidget.setMode(mode);
        endGameWidget.setIsOver(true);
        endGameWidget.setVisible(true);
    }

    public void hideEndGameWidget() {
        gameHUDInputMultiplexer.removeProcessor(endGameWidget);
        endGameWidget.setVisible(false);
    }

    public InputMultiplexer getGameHUDInputMultiplexer() {
        return gameHUDInputMultiplexer;
    }

    @Override
    public void act() {
        if (touchScreenStage.isFocus()) {
            touchScreenStage.act();
        } else {
            super.act();
        }
        if (currentToastMessageShowTime == -1) {
            return;
        }
        if (currentToastMessageShowTime >= TOAST_MESSAGE_VISIBLE_TIME) {
            messageLabel.setVisible(false);
            currentToastMessageShowTime = -1;
        } else {
            currentToastMessageShowTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void draw() {
        if (touchScreenStage.isFocus()) {
            touchScreenStage.draw();
            return;
        }
        super.draw();
    }

    public void showPlayerHeal(PlayerWarMachine playerWarMachine) {
        playerHealDisplay.clearChildren();
        playerHealDisplay.addActor(playerWarMachine.getHealPaneTable());
        playerHealDisplay.addActor(playerWarMachine.getForceField().getForceFieldPaneTable());
    }

    public void showOtherHealDisplay(WarMachine warMachine) {
        if(warMachine.getHitPoint() != 0 && warMachine.getHealPaneTable().getParent() == null) {
            otherHealDisplay.clearChildren();
            otherHealDisplay.addActor(warMachine.getHealPaneTable());
            otherHealDisplay.addActor(warMachine.getForceField().getForceFieldPaneTable());
        }
    }

    public PlayerWeaponMenu getPlayerWeaponMenu() {
        return playerWeaponMenu;
    }

    public void setTerrainManager(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    @Override
    public void onEnemiesNumberChange(int number) {
        informationPanel.setEnemiesNumber(number);
        if (number == 0 && (TerrainManager.gameMode == TerrainManager.DEATH_MATCH || TerrainManager.gameMode == TerrainManager.ATK_DEF_HQ)) {
            showEndGameWidget(EndGameWidget.WIN_ID);
        }
    }

    @Override
    public void onNoWeaponLeft() {
        if (currentToastMessageShowTime == -1) {
            showToastMessage("Damned! Out of weapon\nWe must return to our spawn position to access workshop for purchasing some");
        }
    }

    @Override
    public void onCurrentWeaponSwitch(WeaponManager.WeaponItem weaponItem) {
        GameSounds.getInstance().playSFX(GameSounds.CHOOSE_WEAPON_SFX_ID);
        playerWeaponMenu.getDisplayActiveItemPane().updateCurrentWeaponItem(weaponItem);
    }

    @Override
    public void onNewWeaponAdded(WeaponManager.WeaponItem weaponItem, int index) {
        playerWeaponMenu.getWeaponList().addNewWeaponItemButton(weaponItem, index);
        switch (weaponItem.weaponModel.id){
            case SupportItem.REPAIR_KIT:{
                fastRepairButton.setWeaponItem(weaponItem);
            }
            break;

            case SupportItem.BOOST_SPEED:{
                boostSpeedButton.setWeaponItem(weaponItem);
            }
            break;

            default:{
                break;
            }
        }
    }
}
