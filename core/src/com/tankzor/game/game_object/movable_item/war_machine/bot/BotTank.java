package com.tankzor.game.game_object.movable_item.war_machine.bot;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.SpawnLocation;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.PathFindingProvider;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.OnWarMachineFiringListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.WarMachineStateListener;
import com.tankzor.game.util.FloatPoint;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12/17/2016.
 */

public class BotTank extends MovableBotWarMachine {
    protected OnWarMachineFiringListener firingListener;
    protected boolean canFireBullet = false;
    private float currentReloadTime;
    private NavigationGrid<GridCell> pathFindingMapIgnoreSomeWall;

    public BotTank(SpawnLocation spawnLocation,
                   int type,
                   int teamID,
                   int hitPoint,
                   float speed,
                   OnDynamicDamagedEntityMovingListener entityMovingListener,
                   OnEntityDestroyedListener entityDestroyedListener,
                   MapInformationProvider mapInformationProvider,
                   AirManager airManager,
                   LightingManager lightingManager,
                   AssetLoader assetLoader,
                   WarMachineStateListener trackListener,
                   Array<GridPoint2> controlAreas,
                   int rangeSize,
                   PathFindingProvider pathFindingProvider,
                   OnWarMachineFiringListener firingListener) {
        super(spawnLocation,
                type,
                teamID,
                hitPoint,
                speed,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader,
                trackListener,
                controlAreas,
                rangeSize,
                pathFindingProvider);
        this.firingListener = firingListener;
        this.currentReloadTime = 0;
        setDebug(true);
    }

    @Override
    public void setBaseWeaponModel(WeaponModel baseWeaponModel) {
        super.setBaseWeaponModel(baseWeaponModel);
        this.currentReloadTime = baseWeaponModel.reloadTime;
    }

    @Override
    protected void setUpMapFindingSystems(PathFindingProvider pathFindingProvider) {
        pathFindingMapIgnoreSomeWall = pathFindingProvider.getNavigationGrid();
        pathFindingMap = pathFindingProvider.getNavigationGridIgnoreSomeWall();
        pathFinder = pathFindingProvider.getPathFinder();
    }

    @Override
    public void addMoveAction() {
        if (currentPathsLocation.size() == 0) {
            hasNoMovingActionLeft();
            return;
        }
        FloatPoint location = currentPathsLocation.get(0);
        int nextOrient = findOrient(location.x, location.y);
        if (nextOrient == -1) {
            nextOrient = 0;
        }
        Terrain nextTerrain = mapInformationProvider.getTerrain((int) (location.x / ITEM_SIZE), (int) (location.y / ITEM_SIZE));
        WarMachine warMachineOnTerrain = nextTerrain.getWarMachineOn();
        if (nextTerrain.hasWallOn() ||
                (warMachineOnTerrain != null && warMachineOnTerrain.getTeamID() + teamID == 0)) {
            canFireBullet = true;
            addAction(new WarMachineRotateAction(nextOrient));
            this.terrainWaitingFor = nextTerrain;
            this.terrainWaitingFor.registerEmptyTerrainListener(this);
        }else if(nextTerrain.hasIncomingWarMachineEnter() || warMachineOnTerrain != null) {
            SequenceAction otherAction = tryToFindOtherWay(nextOrient, false);
            if (otherAction != null) {
                addAction(otherAction);
                isAttackTargetChangedLocation = true;
            } else {
                addAction(new WarMachineRotateAction(nextOrient));
                this.terrainWaitingFor = nextTerrain;
                this.terrainWaitingFor.registerEmptyTerrainListener(this);
            }
        }else {
            currentPathsLocation.remove(0);
            isMoving = true;
            if (nextOrient == currentImageIndex) {
                addAction(new MovingAction(currentImageIndex, moveTimePerTerrainBox));
            } else {
                addAction(Actions.sequence(new WarMachineRotateAction(nextOrient), new MovingAction(nextOrient, moveTimePerTerrainBox)));
            }
        }
    }

    @Override
    public void getOutOfThisWay(int fromOrient) {
        if (isMoving || canFireBullet) {
            return;
        }
        SequenceAction actions = null;
        switch (fromOrient) {
            case UP_ORIENT: {
                actions = tryToFindOtherWay(DOWN_ORIENT, true);
            }
            break;

            case DOWN_ORIENT: {
                actions = tryToFindOtherWay(UP_ORIENT, true);
            }
            break;

            case LEFT_ORIENT: {
                actions = tryToFindOtherWay(RIGHT_ORIENT, true);
            }
            break;

            case RIGHT_ORIENT: {
                actions = tryToFindOtherWay(LEFT_ORIENT, true);
            }
            break;

            default: {
                break;
            }
        }
        if (actions != null) {
            if (actions.getActions().size == 2) {
                isMoving = true;
            }
            addAction(actions);
        }
    }

    @Override
    protected SequenceAction getSuitableSequenceAction(Terrain terrain, int orient) {
        if (terrain == null) {
            return null;
        }
        WarMachine warMachineOnTerrain = terrain.getWarMachineOn();
        Wall wallOnTerrain = terrain.getWallOn();

        if(wallOnTerrain != null){
            if(wallOnTerrain.getType() <= Wall.PLANT_TYPE){
                canFireBullet = true;
                isMoving = false;
                if (terrainWaitingFor != null) {
                    terrainWaitingFor.removeEmptyTerrainListener(this);
                    terrainWaitingFor = null;
                }
                terrainWaitingFor = terrain;
                terrainWaitingFor.registerEmptyTerrainListener(this);
                WarMachineRotateAction rotateAction = new WarMachineRotateAction(orient);
                return Actions.sequence(rotateAction);
            }
        }else if(warMachineOnTerrain != null){
            if(warMachineOnTerrain.getTeamID() + teamID == 0){
                canFireBullet = true;
                isMoving = false;
                if (terrainWaitingFor != null) {
                    terrainWaitingFor.removeEmptyTerrainListener(this);
                    terrainWaitingFor = null;
                }
                terrainWaitingFor = terrain;
                terrainWaitingFor.registerEmptyTerrainListener(this);
                WarMachineRotateAction rotateAction = new WarMachineRotateAction(orient);
                return Actions.sequence(rotateAction);
            }
        }else if(!terrain.hasIncomingWarMachineEnter()){
            if (terrainWaitingFor != null) {
                terrainWaitingFor.removeEmptyTerrainListener(this);
                terrainWaitingFor = null;
            }
            isMoving = true;
            return Actions.sequence(new WarMachineRotateAction(orient), new MovingAction(orient, moveTimePerTerrainBox));
        }
        return null;
    }

    @Override
    public List<GridCell> findPathTo(float xEnd, float yEnd) {
        List<GridCell> pathsToDestination = new ArrayList<GridCell>();

        int iStart = (int) (getX() / ITEM_SIZE);
        int jStart = (int) (getY() / ITEM_SIZE);

        int iEnd = (int) (xEnd / ITEM_SIZE);
        int jEnd = (int) (yEnd / ITEM_SIZE);

        Terrain terrain = mapInformationProvider.getTerrain(iEnd, jEnd);
        if (terrain == null || terrain.getWallOn() != null) {
            return pathsToDestination;
        }
        pathsToDestination.addAll(getFastestPathToDestination(iStart, jStart, iEnd, jEnd));
        return pathsToDestination;
    }

    @Override
    public boolean setupPathTo(List<GridCell> pathsToDestination, boolean isLimitPathNumber) {
        if (pathsToDestination.size() == 0 ||
                (isLimitPathNumber && (pathsToDestination.size() > maxTerrainToTarget))) {
            return false;
        }
        currentPathsLocation.clear();
        for (int i = 0; i < pathsToDestination.size() - 1; i++) {
            GridCell aCell = pathsToDestination.get(i);
            currentPathsLocation.add(new FloatPoint(aCell.x * ITEM_SIZE, aCell.y * ITEM_SIZE));
        }
        return true;
    }

    private List<GridCell> getFastestPathToDestination(int iStart, int jStart, int iEnd, int jEnd) {
        List<GridCell> pathToDestination = new ArrayList<GridCell>();
        List<GridCell> temp = pathFinder.findPath(iStart, jStart, iEnd, jEnd, pathFindingMap);
        if (temp != null) {
            pathToDestination.addAll(temp);
        }

        if (pathToDestination.size() == 0) {
            return pathToDestination;
        }
        List<GridCell> pathToDestinationIgnoreSomeWall = new ArrayList<GridCell>();
        temp = pathFinder.findPath(iStart, jStart, iEnd, jEnd, pathFindingMapIgnoreSomeWall);
        if (temp != null) {
            pathToDestinationIgnoreSomeWall.addAll(temp);
        }
        if (pathToDestinationIgnoreSomeWall.size() == 0) {
            return pathToDestination;
        }
        return (pathToDestinationIgnoreSomeWall.size() <= pathToDestination.size() + 3) ?
                pathToDestinationIgnoreSomeWall : pathToDestination;
    }

    protected int findOpenFireOrient() {//find the fire orient (best orient == current orient)
        if (currentAttackTarget == null) {
            return -1;
        }
        int fireOrient = findOrient(currentAttackTarget.getX(), currentAttackTarget.getY());
        if (fireOrient != -1 && !hasStrongWallOnTheWayToTarget(currentAttackTarget, fireOrient)) {
            return fireOrient;
        }
        return -1;
    }

    private boolean hasStrongWallOnTheWayToTarget(WarMachine target, int orient) {
        Terrain terrain;
        int start;
        int end;
        int temp;
        switch (orient) {
            case UP_ORIENT: {
                temp = (int) (getX() / GameEntity.ITEM_SIZE);
                start = (int) (getY() / GameEntity.ITEM_SIZE) + 1;
                end = (int) (target.getY() / GameEntity.ITEM_SIZE) - 1;
                for (int j = start; j <= end; j++) {
                    terrain = mapInformationProvider.getTerrain(temp, j);
                    if (terrain.hasWallOn() && terrain.getWallOn().getType() > Wall.SPIKE_TYPE) {
                        return true;
                    }
                }
                return false;
            }

            case DOWN_ORIENT: {
                temp = (int) (getX() / GameEntity.ITEM_SIZE);
                start = (int) (target.getY() / GameEntity.ITEM_SIZE) + 1;
                end = (int) (getY() / GameEntity.ITEM_SIZE) - 1;
                for (int j = start; j <= end; j++) {
                    terrain = mapInformationProvider.getTerrain(temp, j);
                    if (terrain.hasWallOn() && terrain.getWallOn().getType() > Wall.SPIKE_TYPE) {
                        return true;
                    }
                }
                return false;
            }

            case LEFT_ORIENT: {
                temp = (int) (getY() / GameEntity.ITEM_SIZE);
                start = (int) (target.getX() / GameEntity.ITEM_SIZE) + 1;
                end = (int) (getX() / GameEntity.ITEM_SIZE) - 1;
                for (int i = start; i <= end; i++) {
                    terrain = mapInformationProvider.getTerrain(i, temp);
                    if (terrain.hasWallOn() && terrain.getWallOn().getType() > Wall.SPIKE_TYPE) {
                        return true;
                    }
                }
                return false;
            }

            case RIGHT_ORIENT: {
                temp = (int) (getY() / GameEntity.ITEM_SIZE);
                start = (int) (getX() / GameEntity.ITEM_SIZE) + 1;
                end = (int) (target.getX() / GameEntity.ITEM_SIZE) - 1;
                for (int i = start; i <= end; i++) {
                    terrain = mapInformationProvider.getTerrain(i, temp);
                    if (terrain.hasWallOn() && terrain.getWallOn().getType() > Wall.SPIKE_TYPE) {
                        return true;
                    }
                }
                return false;
            }

            default: {
                return true;
            }
        }
    }

    @Override
    public void fire() {
        firingListener.onBotFired(this);
    }

    @Override
    protected boolean checkActingCondition(float delta) {
        if ((WarMachineManager.isEnemiesFrenzy && teamID == ENEMIES_TEAM)) {
            return false;
        }

        if (currentReloadTime < baseWeaponModel.reloadTime) {
            currentReloadTime += delta;
        }

        if(currentRotateAction != null){
            return true;
        }

        if (canFireBullet) {
            if ((currentReloadTime >= baseWeaponModel.reloadTime)) {
                fire();
                currentReloadTime = 0;
            }
        }

        if (isMoving && !canFireBullet) {
            canFireBullet = findOpenFireOrient() == currentImageIndex;
        } else {
            if (currentAttackTarget == null) {
                ((WarMachineManager) getStage()).updateAttackTargetFor(this);
            }
            if (isAttackTargetChangedLocation) {
                isAttackTargetChangedLocation = false;
                if (!updatePathToCurrentAttackTarget()) {
                    canFireBullet = false;
                    setCurrentAttackTarget(null);
                    setupDefaultMoving();
                }
                if (terrainWaitingFor != null) {
                    terrainWaitingFor.removeEmptyTerrainListener(this);
                    this.terrainWaitingFor = null;
                }
            }

            //change fire orient if possible
            int fireOrient = findOpenFireOrient();
            if (fireOrient != -1) {
                if (fireOrient != currentImageIndex) {
                    if (terrainWaitingFor != null) {
                        terrainWaitingFor.removeEmptyTerrainListener(this);
                        this.terrainWaitingFor = null;
                    }
                    addAction(new WarMachineRotateAction(fireOrient));
                }
                canFireBullet = true;
            } else {
                if (terrainWaitingFor == null) {
                    canFireBullet = false;
                    addMoveAction();
                }
            }
        }
        return true;
    }

    @Override
    public void onTerrainIsEmpty() {
        canFireBullet = false;
        super.onTerrainIsEmpty();
    }

    @Override
    public void destroy() {
        if (terrainWaitingFor != null) {
            terrainWaitingFor.removeEmptyTerrainListener(this);
        }
        super.destroy();
    }
}
