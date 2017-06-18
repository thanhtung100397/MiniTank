package com.tankzor.game.game_object.movable_item.war_machine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.animation.MachineSmoke;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.DynamicDamagedEntity;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.WarMachineStateListener;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;
import com.tankzor.game.game_object.support_item.ForceField;
import com.tankzor.game.gamehud.HealBar;
import com.tankzor.game.gamehud.Pane;
import com.tankzor.game.gamehud.PaneTable;
import com.tankzor.game.network.Network;
import com.tankzor.game.util.CircleList;

import sun.nio.ch.Net;

/**
 * Created by Admin on 11/6/2016.
 */

public abstract class WarMachine extends DynamicDamagedEntity {
    public static final float ROTATE_FRAME_DURATION_PER_SPEED_POINT = 0.055f;
    public static final float SUPPORT_RANGE_SIZE = 5 * ITEM_SIZE;
    public static final int MAX_HEAL_PANE_PER_ROW = 25;

    public static final int LIGHT_TANK_TYPE = 0;
    public static final int HEAVY_TANK_TYPE = 1;
    public static final int KAMIKAZE_TANK_TYPE = 2;
    public static final int ARTILLERY_TANK_TYPE = 3;
    public static final int TURRET_TYPE = 4;

    public static final int DOWN_ORIENT = 0;
    public static final int LEFT_ORIENT = 4;
    public static final int UP_ORIENT = 8;
    public static final int RIGHT_ORIENT = 12;

    public static final int SMOKE_HP_VALUE = 2;

    protected HealBar healBar;
    protected float currentRotateFrameDuration;
    protected Array<WarMachineStateListener> stateListeners;

    protected Rectangle supportBound;
    protected WeaponModel baseWeaponModel;

    protected MachineSmoke machineSmoke;
    private AirManager airManager;
    protected ForceField forceField;
    protected PaneTable healPaneTable;
    private int value;

    private String id;

    protected WarMachineRotateAction currentRotateAction = null;
    private AssetLoader assetLoader;

    public WarMachine(float x, float y,
                      int type,
                      int teamID,
                      int hitPoint,
                      OnDynamicDamagedEntityMovingListener entityMovingListener,
                      OnEntityDestroyedListener entityDestroyedListener,
                      LightingManager lightingManager,
                      MapInformationProvider mapInformationProvider,
                      AirManager airManager,
                      AssetLoader assetLoader) {
        super(x, y,
                ITEM_SIZE, ITEM_SIZE,
                teamID,
                hitPoint,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider);
        this.airManager = airManager;
        this.assetLoader = assetLoader;
        init(x, y, ITEM_SIZE, ITEM_SIZE, teamID);
        this.images = assetLoader.getWarMachineImages(type);
    }

    public WarMachine(float x, float y,
                      float width, float height,
                      int teamID,
                      int hitPoint,
                      OnDynamicDamagedEntityMovingListener entityMovingListener,
                      OnEntityDestroyedListener entityDestroyedListener,
                      LightingManager lightingManager,
                      MapInformationProvider mapInformationProvider,
                      AirManager airManager,
                      AssetLoader assetLoader) {//constructor for building entity
        super(x, y,
                width, height,
                teamID,
                hitPoint,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider);
        this.airManager = airManager;
        this.assetLoader = assetLoader;
        init(x, y, width, height, teamID);
    }

    public String getId() {
        return id;
    }

    protected void onAttachToGameWorld() {
        Terrain terrainStepOn = this.mapInformationProvider.getTerrain((int) (getX() / ITEM_SIZE), (int) (getY() / ITEM_SIZE));
        terrainStepOn.registerIncomingWarMachineEnter(this);//register for entering first
        terrainStepOn.addItemOn(this);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(stage != null){
            onAttachToGameWorld();
        }
    }

    private void init(float x, float y, float width, float height, int teamID) {
        this.stateListeners = new Array<WarMachineStateListener>(1);
        this.healBar = new HealBar(this, assetLoader);
        this.currentRotateFrameDuration = ROTATE_FRAME_DURATION_PER_SPEED_POINT;

        this.supportBound = new Rectangle(x - (SUPPORT_RANGE_SIZE - width) / 2,
                y - (SUPPORT_RANGE_SIZE - height) / 2,
                SUPPORT_RANGE_SIZE,
                SUPPORT_RANGE_SIZE);

        this.healPaneTable = new PaneTable((teamID == ALLIES_TEAM) ? Pane.GREEN_PANE : Pane.RED_PANE,
                maxHitPoint,
                Pane.PANE_NORMAL,
                MAX_HEAL_PANE_PER_ROW,
                assetLoader);

        this.forceField = new ForceField(this, 0, 0);

        this.machineSmoke = new MachineSmoke(this, airManager, assetLoader);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Rectangle getSupportBound() {
        return supportBound.setPosition(getX() - (SUPPORT_RANGE_SIZE - getWidth()) / 2,
                getY() - (SUPPORT_RANGE_SIZE - getHeight()) / 2);
    }

    @Override
    public int takeDamage(int damage, boolean byPlayer) {
        damage = forceField.takeDamage(damage);
        if (damage > 0) {
            damage = super.takeDamage(damage, byPlayer);
            if (byPlayer && teamID == ENEMIES_TEAM && this.hitPoint == 0) {
                PlayerProfile.getInstance().getMissionRecorder().unitDestroyed++;
                PlayerProfile.getInstance().addMoney(value);
            }
            return damage;
        }
        return 0;
    }

    @Override
    public void setHitPoint(int hitPoint) {
        healPaneTable.setPane(hitPoint - this.hitPoint);
        super.setHitPoint(hitPoint);
        if (hitPoint <= SMOKE_HP_VALUE) {
            createMachineSmoke();
        } else {
            stopMachineSmoke();
        }
        healBar.update();
        if (this.hitPoint == 0) {
            destroy();
        }
    }

    public WeaponModel getBaseWeaponModel() {
        return baseWeaponModel;
    }

    public void setBaseWeaponModel(WeaponModel baseWeaponModel) {
        this.baseWeaponModel = baseWeaponModel;
    }

    /**
     * This method is called before super.act(delta) to do something necessary.
     *
     * @return true when super.act(delta) is allowed to call after that, false otherwise
     */
    protected boolean checkActingCondition(float delta) {
        return true;
    }

    @Override
    public void act(float delta) {
        forceField.act(delta);
        machineSmoke.act(delta);
        if (checkActingCondition(delta)) {
            super.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawDecorateItem(batch);
        super.draw(batch, parentAlpha);
        healBar.draw(batch);
    }

    protected void drawDecorateItem(Batch batch) {
        forceField.draw(batch);
    }

    public boolean setNextOrient(int orient) {
        if(orient == 11){
            Gdx.app.log("setNextOrient", "orient: 11");
        }
        if (currentRotateAction == null) {
            addAction(new WarMachineRotateAction(orient));
        } else {
            if (orient == currentRotateAction.nextOrient) {
                return false;
            }
            currentRotateAction.setNextOrient(orient);
        }
        return true;
    }

    protected void onStopRotate() {
        currentRotateAction = null;
    }

    public abstract void fire();

    protected void createMachineSmoke() {
        if (airManager == null) {
            return;
        }
        machineSmoke.start();
    }

    public void stopMachineSmoke() {
        machineSmoke.stop();
    }

    public void addWarMachineStateListener(WarMachineStateListener changeLocationListener) {
        stateListeners.add(changeLocationListener);
    }

    public void removeWarMachineActionListener(WarMachineStateListener changeLocationListener) {
        stateListeners.removeValue(changeLocationListener, true);
    }

    public void notifyWarMachineStartMovingToAllListener() {
        for (int i = stateListeners.size - 1; i >= 0; i--) {
            stateListeners.get(i).onWarMachineStartMoving(this);
        }
    }

    public void notifyWarMachineStopMovingToAllListener() {
        for (int i = stateListeners.size - 1; i >= 0; i--) {
            stateListeners.get(i).onWarMachineStopMoving(this);
        }
    }

    public void notifyWarMachineDestroyedToAllListener() {
        for (int i = stateListeners.size - 1; i >= 0; i--) {
            stateListeners.get(i).onWarMachineDestroyed(this);
        }
    }

    @Override
    public void destroy() {
        entityMovingListener.onDynamicDamagedEntityExit(this, previousX, previousY, previousX + moveDistanceX, previousY + moveDistanceY);
        entityDestroyedListener.onWarMachineDestroyed(this, null);
        notifyWarMachineDestroyedToAllListener();
        machineSmoke.clearSmoke();
        healPaneTable.remove();
        forceField.getForceFieldPaneTable().remove();
        if (value > 0) {
            PlayerProfile.getInstance().addMoney(value);
        }
        super.destroy();
    }

    public int getBulletHeight() {
        return Bullet.LOW_BULLET;
    }

    public ForceField getForceField() {
        return forceField;
    }

    public PaneTable getHealPaneTable() {
        return healPaneTable;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public AirManager getAirManager() {
        return airManager;
    }

    public class WarMachineRotateAction extends Action {
        private float frameTimeElapse;
        private CircleList.Node<Integer> startNode;
        private int nextOrient = -1;
        private boolean isClockwise;

        public WarMachineRotateAction(int nextOrient) {
            this.frameTimeElapse = currentRotateFrameDuration;
            this.startNode = Dimension.getWarMachineRotateImageIndex().getNode(currentImageIndex);
            setNextOrient(nextOrient);
            currentRotateAction = this;
        }

        public void setNextOrient(int nextOrient) {
            if (nextOrient != this.nextOrient) {
                this.nextOrient = nextOrient;
                this.isClockwise = Dimension.getWarMachineRotateImageIndex().getBestIterator(currentImageIndex, nextOrient);
            }
        }

        @Override
        public boolean act(float delta) {
            if (frameTimeElapse >= currentRotateFrameDuration) {
                if (currentImageIndex == this.nextOrient) {
                    onStopRotate();
                    return true;
                }
                if (isClockwise) {
                    startNode = startNode.next;
                } else {
                    startNode = startNode.previous;
                }
                currentImageIndex = startNode.getValue();
                frameTimeElapse = 0;
            }
            frameTimeElapse += delta;
            return false;
        }
    }
}
