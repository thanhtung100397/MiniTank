package com.tankzor.game.gamehud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.BulletManager;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Aircraft;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.game_object.support_item.RepairItem;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.ui.PlayerCamera;

/**
 * Created by Admin on 1/2/2017.
 */

public class TouchScreenStage extends Stage {
    private PlayerCamera playerCamera;
    private TerrainManager terrainManager;
    private BulletManager bulletManager;
    private WeaponManager.WeaponItem weaponItem;
    private GameHUD gameHUD;
    private InputMultiplexer inputMultiplexer;
    private boolean isFocus = false;
    private float xTouchDownCancelButton, yTouchDownCancelButton;
    private boolean isDragged;
    private boolean canPan;
    private TargetMark targetMark;

    public TouchScreenStage(GameHUD parent, final AssetLoader assetLoader) {
        super(parent.getViewport(), parent.getBatch());
        this.gameHUD = parent;

        this.targetMark = new TargetMark(assetLoader);
        addActor(targetMark);
        targetMark.setVisible(false);

        Label messageLabel = new Label("Drag on screen to choose position\nTap on the target mark again to activating your item", GameImages.getInstance().getLabelStyle());
        messageLabel.setFontScale(Dimension.normalFontScale);
        messageLabel.setAlignment(Align.center);
        messageLabel.setBounds(0, Gdx.graphics.getHeight() - Dimension.messageLabelHeight, Gdx.graphics.getWidth(), Dimension.messageLabelHeight);
        addActor(messageLabel);

        Skin skin = GameImages.getInstance().getUiSkin();
        ImageButton.ImageButtonStyle cancelButtonStyle = new ImageButton.ImageButtonStyle();
        cancelButtonStyle.down = skin.getDrawable(GameImages.KEY_PRESS_BIG_DPAD_BUTTON_BACKGROUND);
        cancelButtonStyle.over = skin.getDrawable(GameImages.KEY_PRESS_BIG_DPAD_BUTTON_BACKGROUND);
        cancelButtonStyle.up = skin.getDrawable(GameImages.KEY_NORMAL_BIG_DPAD_BUTTON_BACKGROUND);
        cancelButtonStyle.imageUp = skin.getDrawable(GameImages.KEY_CANCEL_FIRE_BUTTON_ICON);

        ImageButton cancelButton = new ImageButton(cancelButtonStyle);
        cancelButton.setBounds(Gdx.graphics.getWidth() - Dimension.bigCircleButtonBackgroundSize - 35,
                35,
                Dimension.bigCircleButtonBackgroundSize,
                Dimension.bigCircleButtonBackgroundSize);

        cancelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                xTouchDownCancelButton = x;
                yTouchDownCancelButton = y;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragged) {
                    isDragged = false;
                    return;
                }
                targetMark.setVisible(false);
                playerCamera.updatePosition();
                gameHUD.switchTouchScreen(false);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Math.abs(xTouchDownCancelButton - x) > 10 || Math.abs(yTouchDownCancelButton - y) > 10) {
                    isDragged = true;
                }
            }
        });

        addActor(cancelButton);

        final GestureDetector touchScreenDetector = new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                GameSounds.getInstance().playSFX(GameSounds.CHOOSE_TARGET_SFX_ID);
                targetMark.setVisible(true);

                Vector3 worldLocation = new Vector3(x, y, 0);
                playerCamera.unproject(worldLocation);
                Vector3 screenLocation = new Vector3((int) (worldLocation.x / GameEntity.ITEM_SIZE) * GameEntity.ITEM_SIZE,
                                                    (int) (worldLocation.y / GameEntity.ITEM_SIZE) * GameEntity.ITEM_SIZE,
                                                    0);
                playerCamera.project(screenLocation);

                if (targetMark.setLocation(screenLocation.x, screenLocation.y)) {
                    switch (weaponItem.weaponModel.id) {
                        case AreaWeapon.AIR_STRIKE: {
                            gameHUD.switchTouchScreen(false);
                            targetMark.setVisible(false);
                            bulletManager.onFiredAreaWeapon(new Aircraft(worldLocation.x, worldLocation.y,
                                    weaponItem.weaponModel.damage,
                                    weaponItem.weaponModel.explosion,
                                    PlayerProfile.getInstance().getNumberOfBomb(),
                                    terrainManager.getExplosionManager(),
                                    terrainManager,
                                    terrainManager.getLightingManager(),
                                    assetLoader));
                            weaponItem.decrease(1);
                            playerCamera.updatePosition();
                        }
                        break;

                        case SupportItem.REPAIR_KIT: {
                            Terrain terrain = terrainManager.getTerrain((int) (worldLocation.x / GameEntity.ITEM_SIZE),
                                    (int) (worldLocation.y / GameEntity.ITEM_SIZE));
                            WarMachine warMachine = terrain.getWarMachineOn();
                            if (warMachine != null && warMachine.getHitPoint() < warMachine.getMaxHitPoint()) {
                                RepairItem repairItem = new RepairItem();
                                repairItem.addEffectTo(terrain.getWarMachineOn());
                                gameHUD.switchTouchScreen(false);
                                targetMark.setVisible(false);
                                weaponItem.decrease(1);
                                playerCamera.updatePosition();
                            }
                        }
                        break;

                        default: {
                            break;
                        }
                    }
                }
                canPan = true;
                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                if (!canPan) {
                    return false;
                }
                playerCamera.translate(-deltaX, deltaY);
                targetMark.translate(deltaX, -deltaY);
                return true;
            }
        });

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(touchScreenDetector);
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean value) {
        isFocus = value;
        canPan = false;
    }

    public void setGameCamera(PlayerCamera playerCamera) {
        this.playerCamera = playerCamera;
    }

    public void setTerrainManager(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public void setBulletManager(BulletManager bulletManager) {
        this.bulletManager = bulletManager;
    }

    public void setWeaponItem(WeaponManager.WeaponItem weaponItem) {
        this.weaponItem = weaponItem;
    }

    public InputMultiplexer getInputProcessor() {
        return inputMultiplexer;
    }

    public TargetMark getTargetMark() {
        return targetMark;
    }
}
