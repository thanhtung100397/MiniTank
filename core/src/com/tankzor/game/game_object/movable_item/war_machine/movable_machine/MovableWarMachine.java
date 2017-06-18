package com.tankzor.game.game_object.movable_item.war_machine.movable_machine;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.RoundSet;
import com.tankzor.game.game_object.SpawnLocation;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.network.Network;

/**
 * Created by Admin on 11/7/2016.
 */

public abstract class MovableWarMachine extends WarMachine {
    public static final float MOVE_TIME_PER_SPEED_POINT = 0.8f;
    public static final int HIGH_SPEED = 1;
    public static final int NO_BOOST = 0;
    public static final int SLOW_DOWN = -1;

    protected int previousOrient;
    protected float baseSpeed;
    protected int currentSpeedType;
    protected SpawnLocation spawnLocation;
    protected Terrain incomingGetInTerrain;

    public MovableWarMachine(SpawnLocation spawnLocation,
                             int type,
                             int teamID,
                             int hitPoint,
                             float speed,
                             OnDynamicDamagedEntityMovingListener entityMovingListener,
                             OnEntityDestroyedListener entityDestroyedListener,
                             LightingManager lightingManager,
                             MapInformationProvider mapInformationProvider,
                             AirManager airManager,
                             AssetLoader assetLoader,
                             WarMachineStateListener trackListener) {//can't-fire machine
        super(spawnLocation.x, spawnLocation.y,
                type,
                teamID,
                hitPoint,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader);
        this.spawnLocation = spawnLocation;
        if (trackListener != null) {
            addWarMachineStateListener(trackListener);
        }
        this.baseSpeed = speed;
        retrieveBaseSpeed();
    }

    public void boostSpeed(int value) {
        switch (value) {
            case HIGH_SPEED: {
                this.moveTimePerTerrainBox /= 1.5f;
                this.currentRotateFrameDuration /= 1.5f;
            }
            break;

            case SLOW_DOWN: {
                this.moveTimePerTerrainBox *= 1.5f;
                this.currentRotateFrameDuration *= 1.5f;
            }
            break;

            default: {
                break;
            }
        }
    }

    public void changeSpeedByBaseSpeed(int type) {
        if (currentSpeedType == type) {
            return;
        }
        int delta = type - currentSpeedType;
        this.currentSpeedType = type;
        switch (delta) {
            case 1: {
                this.moveTimePerTerrainBox /= 1.5f;
                this.currentRotateFrameDuration /= 1.5f;
            }
            break;

            case -1: {
                this.moveTimePerTerrainBox *= 1.5f;
                this.currentRotateFrameDuration *= 1.5f;
            }
            break;

            case 2: {
                this.moveTimePerTerrainBox /= 1.5f * 1.5f;
                this.currentRotateFrameDuration /= 1.5f * 1.5f;
            }
            break;

            case -2: {
                this.moveTimePerTerrainBox *= 1.5f * 1.5f;
                this.currentRotateFrameDuration *= 1.5f * 1.5f;
            }
            break;

            default: {
                break;
            }
        }
    }

    public void retrieveBaseSpeed() {
        this.currentSpeedType = NO_BOOST;
        this.moveTimePerTerrainBox = MOVE_TIME_PER_SPEED_POINT / baseSpeed;
        this.currentRotateFrameDuration = ROTATE_FRAME_DURATION_PER_SPEED_POINT / baseSpeed;
    }

    protected void calculateMoveDistance(int orient) {
        switch (orient) {
            case UP_ORIENT: {
                this.moveDistanceX = 0;
                this.moveDistanceY = ITEM_SIZE;
            }
            break;

            case DOWN_ORIENT: {
                this.moveDistanceX = 0;
                this.moveDistanceY = -ITEM_SIZE;
            }
            break;

            case LEFT_ORIENT: {
                this.moveDistanceX = -ITEM_SIZE;
                this.moveDistanceY = 0;
            }
            break;

            case RIGHT_ORIENT: {
                this.moveDistanceX = ITEM_SIZE;
                this.moveDistanceY = 0;
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public boolean setNextOrient(int orient) {
        if (currentRotateAction == null) {
            if (orient == currentImageIndex) {
                addMoveAction();
                return false;
            }
            addAction(new WarMachineRotateAction(orient));
        } else {
            currentRotateAction.setNextOrient(orient);
        }
        return true;
    }

    public abstract void addMoveAction();

    public int getPreviousOrient() {
        return previousOrient;
    }

    @Override
    public void destroy() {
        spawnLocation.currentSpawnCount--;
        spawnLocation.spawnWarMachine();

        WarMachineManager warMachineManager = (WarMachineManager) getStage();
        if (warMachineManager != null) {
            warMachineManager.removeMovableMachine(this);
        }
        incomingGetInTerrain.unRegisterIncomingWarMachineEnter(this);
        super.destroy();
    }

    protected class MovingAction extends MoveToAction {
        private boolean isEntered;

        public MovingAction(int orient, float duration) {
            setDuration(duration);
            calculateMoveDistance(orient);
            float xDes = MovableWarMachine.this.getX() + moveDistanceX;
            float yDes = MovableWarMachine.this.getY() + moveDistanceY;
            setPosition(xDes, yDes);
            this.isEntered = false;
            changeSpeedByBaseSpeed(mapInformationProvider.getTerrain((int) (getX() / ITEM_SIZE), (int) (getY() / ITEM_SIZE)).getSpeedBoostType());
            incomingGetInTerrain = mapInformationProvider.getTerrain((int) (xDes / ITEM_SIZE), (int) (yDes / ITEM_SIZE));
            incomingGetInTerrain.registerIncomingWarMachineEnter(MovableWarMachine.this);
        }

        @Override
        public boolean act(float delta) {
            if (super.act(delta)) {
                notifyWarMachineStopMovingToAllListener();
                entityMovingListener.onDynamicDamagedEntityExit(MovableWarMachine.this, previousX, previousY, previousX, previousY);
                onMovingActionStop();
                previousX = MovableWarMachine.this.getX();
                previousY = MovableWarMachine.this.getY();
                isEntered = false;

                moveDistanceX = 0;
                moveDistanceY = 0;

                previousOrient = currentImageIndex;
                return true;
            }
            if (!isEntered) {
                entityMovingListener.onDynamicDamagedEntityEnter(MovableWarMachine.this, getX(), getY(), getX(), getY());
                notifyWarMachineStartMovingToAllListener();
                isEntered = true;
            }
            onMovingActionOnProcess();
            return false;
        }
    }

    protected void onMovingActionStop() {
        mapInformationProvider.takeItemIfPossible(this);
    }

    protected abstract void onMovingActionOnProcess();

    public SpawnLocation getSpawnLocation() {
        return spawnLocation;
    }
}
