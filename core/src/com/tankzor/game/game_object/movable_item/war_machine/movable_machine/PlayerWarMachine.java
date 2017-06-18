package com.tankzor.game.game_object.movable_item.war_machine.movable_machine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.PlayerSpawnLocation;
import com.tankzor.game.game_object.animation.OnExplosionFinishListener;
import com.tankzor.game.game_object.decorations.RingFlickerListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.GroundWeaponManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.*;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.ui.PlayerCamera;
import com.tankzor.game.util.QuadRectangle;

import box2dLight.PointLight;

/**
 * Created by Admin on 11/7/2016.
 */
public class PlayerWarMachine extends MovableWarMachine implements OnExplosionFinishListener {
    public static final int BACK_UP = 16;
    private PlayerCamera camera;

    public boolean fireButtonPress = false;
    public int nextMoveOrient = -1;

    private boolean isMovable;
    private OnWarMachineFiringListener firingListener;
    private boolean isDestroyed = false;
    private Array<WeaponManager.WeaponItem> listReloadWeaponItems;
    private QuadRectangle thermovisionBound;
    private float thermovisionTranslate;
    private PointLight flashLight;
    private WeaponManager weaponManager;
    private GroundWeaponManager groundWeaponManager;
    private boolean isDamageable = true;

    public PlayerWarMachine(PlayerSpawnLocation spawnLocation,
                            OnDynamicDamagedEntityMovingListener entityMovingListener,
                            OnEntityDestroyedListener entityDestroyedListener,
                            LightingManager lightingManager,
                            MapInformationProvider mapInformationProvider,
                            AirManager airManager,
                            AssetLoader assetLoader,
                            OnWarMachineFiringListener firingListener,
                            WarMachineStateListener trackListener,
                            WeaponManager weaponManager) {
        super(spawnLocation,
                PlayerProfile.getInstance().getTankType(),
                ALLIES_TEAM,
                PlayerProfile.getInstance().getTotalHitPoint(),
                PlayerProfile.getInstance().getSpeed(),
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader,
                trackListener);
        this.firingListener = firingListener;
        this.isPlayerItem = true;
        this.weaponManager = weaponManager;
        this.weaponManager.setPlayerWarMachine(this);
        this.isMovable = true;
        this.groundWeaponManager = mapInformationProvider.getGroundWeaponManager();

        PlayerProfile playerProfile = PlayerProfile.getInstance();
        forceField.addMaximumNumber(playerProfile.getForceField());
        forceField.setBaseRecoverTime(playerProfile.getForceFieldRecoverTime());
        listReloadWeaponItems = new Array<WeaponManager.WeaponItem>();

        initThermovisionBound();

        if (lightingManager.getDayMode() != LightingManager.DAY_MODE) {
            flashLight = lightingManager.createItemPointLightAttachToObject(7 * GameEntity.ITEM_SIZE, playerProfile.getFlashLightColor(), false, this);
            flashLight.setActive(true);
        }

        forceField.getRing().setRingFlickerListener(new RingFlickerListener() {
            @Override
            public void onStartFlicker() {
                isDamageable = false;
            }

            @Override
            public void onFinishFlicker() {
                isDamageable = true;
            }
        });
    }

    private void initThermovisionBound() {
        thermovisionBound = new QuadRectangle(0, 0, 0, 0);
        updateThermovisionBound();
    }

    public QuadRectangle getThermovisionBound() {
        thermovisionBound.x = getX() - thermovisionTranslate;
        thermovisionBound.y = getY() - thermovisionTranslate;
        return thermovisionBound;
    }

    public void setCamera(PlayerCamera camera) {
        this.camera = camera;
        this.camera.setPlayerWarMachine(this);
        camera.updatePosition();
    }

    public void reborn() {
        setPosition(spawnLocation.x, spawnLocation.y);
        groundWeaponManager.scanMines(getThermovisionBound());

        PlayerProfile playerProfile = PlayerProfile.getInstance();
        this.maxHitPoint = playerProfile.getTotalHitPoint();
        healPaneTable.addPane(this.maxHitPoint);
        this.hitPoint = maxHitPoint;
        this.healBar.update();

        forceField.addMaximumNumber(playerProfile.getForceField());

        previousX = spawnLocation.x;
        previousY = spawnLocation.y;
        Terrain terrainStepOn = mapInformationProvider.getTerrain((int) (spawnLocation.x / ITEM_SIZE), (int) (spawnLocation.y / ITEM_SIZE));
        terrainStepOn.registerIncomingWarMachineEnter(this);//register for entering first
        terrainStepOn.addItemOn(this);

        isDestroyed = false;
        isMovable = true;
        camera.updatePosition();

        forceField.getRing().flicker(3.0f);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public int takeDamage(int damage, boolean byPlayer) {
        if(isDamageable){
            int damageResult = super.takeDamage(damage, false);
            PlayerProfile.getInstance().getMissionRecorder().takenDamage += damage - damageResult;
            return damageResult;
        }
        return 0;
    }

    @Override
    public void addMoveAction() {
        if (canMove(getCurrentOrient())) {
            addAction(new MovingAction(getCurrentOrient(), moveTimePerTerrainBox));
            isMovable = false;
        }
    }

    @Override
    public void fire() {
        firingListener.onPlayerFired(this);
    }

    @Override
    public void act(float delta) {
        if (isDestroyed) {
            return;
        }

        super.act(delta);

        //reload weapons
        for (int i = listReloadWeaponItems.size - 1; i >= 0; i--) {
            WeaponManager.WeaponItem weaponItem = listReloadWeaponItems.get(i);
            weaponItem.currentReloadTime += delta;
            if (weaponItem.currentReloadTime >= weaponItem.weaponModel.reloadTime) {
                listReloadWeaponItems.removeIndex(i);
            }
        }

        //fire event
        WeaponManager.WeaponItem weaponItem = weaponManager.getCurrentWeaponItem();
        if (fireButtonPress && currentRotateAction == null && weaponManager.isCurrentWeaponReady()) {
            fire();
            weaponItem.currentReloadTime = 0;
            listReloadWeaponItems.add(weaponItem);
        }
    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        super.drawDebugBounds(shapes);
        QuadRectangle bound = getThermovisionBound();
        shapes.rect(bound.x, bound.y, bound.width, bound.height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDestroyed) {
            return;
        }
        super.draw(batch, parentAlpha);
    }

    protected boolean canMove(int orient) {
        if (currentRotateAction != null || isDestroyed) {
            return false;
        }
        calculateMoveDistance(orient);
        int iDes = (int) ((getX() + moveDistanceX) / ITEM_SIZE);
        int jDes = (int) ((getY() + moveDistanceY) / ITEM_SIZE);
        return mapInformationProvider.checkMovable(iDes, jDes, orient);
    }

    @Override
    protected void onStopRotate() {
        super.onStopRotate();
        if (nextMoveOrient != -1) {
            setNextOrient(nextMoveOrient);
        }
    }

    @Override
    public boolean setNextOrient(int orient) {
        if (!isMovable) {
            return false;
        }
        if (orient == BACK_UP) {
            moveBack();
            return true;
        }
        return super.setNextOrient(orient);
    }

    public void moveBack() {
        int revertOrient;
        switch (currentImageIndex) {
            case UP_ORIENT: {
                revertOrient = DOWN_ORIENT;
            }
            break;

            case DOWN_ORIENT: {
                revertOrient = UP_ORIENT;
            }
            break;

            case LEFT_ORIENT: {
                revertOrient = RIGHT_ORIENT;
            }
            break;

            case RIGHT_ORIENT: {
                revertOrient = LEFT_ORIENT;
            }
            break;

            default: {
                revertOrient = DOWN_ORIENT;
            }
        }

        if (canMove(revertOrient)) {
            float xDes = getX() + moveDistanceX;
            float yDes = getY() + moveDistanceY;

            addAction(new MovingAction(revertOrient, moveTimePerTerrainBox));

            isMovable = false;
        }
    }

    @Override
    protected void onMovingActionStop() {
        isMovable = true;
        camera.updatePosition();
        groundWeaponManager.scanMines(getThermovisionBound());
        if (!mapInformationProvider.takeItemIfPossible(this) && nextMoveOrient != -1) {
            setNextOrient(nextMoveOrient);
        }
        PlayerProfile.getInstance().getMissionRecorder().travelDistance += 5;
    }

    @Override
    protected void onMovingActionOnProcess() {
        camera.updatePosition();
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public void updateTankForm(int type) {
        images = getAssetLoader().getWarMachineImages(type);
    }

    public void updateHitPoint(int number) {
        this.maxHitPoint += number;
        this.hitPoint += number;
        healPaneTable.addPane(number);
        healBar.update();
    }

    public void updateThermovisionBound() {
        WeaponModel thermovisionModel = PlayerProfile.getInstance().getWeaponModel(SupportItem.THERMOVISION);
        thermovisionBound.width = (2 * (thermovisionModel.capacity + 1) + 1) * ITEM_SIZE;
        thermovisionBound.height = thermovisionBound.width;
        thermovisionTranslate = (thermovisionModel.capacity + 1) * ITEM_SIZE;
        groundWeaponManager.scanMines(getThermovisionBound());
    }

    @Override
    public void destroy() {
        isDestroyed = true;
        nextMoveOrient = -1;

        clearActions();
        currentRotateAction = null;

        notifyWarMachineDestroyedToAllListener();

        machineSmoke.clearSmoke();

        entityMovingListener.onDynamicDamagedEntityExit(this, previousX, previousY, previousX + moveDistanceX, previousY + moveDistanceY);
        entityDestroyedListener.onWarMachineDestroyed(this, this);

        PlayerProfile playerProfile = PlayerProfile.getInstance();
        WeaponModel temporaryArmorModel = playerProfile.getWeaponModel(SupportItem.TEMPORARY_ARMOR);
        playerProfile.updateWeapon(SupportItem.TEMPORARY_ARMOR, -temporaryArmorModel.capacity);
        playerProfile.addForceField(-forceField.getMaximumNumber());
        healPaneTable.reset();
        forceField.reset();

        playerProfile.addLife(-1);

        incomingGetInTerrain.unRegisterIncomingWarMachineEnter(this);
    }

    public void switchFlashLightMode() {
        if (flashLight == null) {
            return;
        }
        if (flashLight.isActive()) {
            flashLight.setActive(false);
        } else {
            flashLight.setActive(true);
        }
    }

    @Override
    public void onExplosionFinish() {
        spawnLocation.spawnWarMachine();
    }

    public boolean isOnSpawnLocation() {
        return !isDestroyed() && spawnLocation.x == getX() && spawnLocation.y == getY();
    }
}
