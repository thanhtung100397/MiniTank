package com.tankzor.game.game_object.movable_item.war_machine.bot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.RoundSet;
import com.tankzor.game.game_object.SpawnLocation;
import com.tankzor.game.game_object.immovable_item.EmptyTerrainListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.TerrainObservable;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.PathFindingProvider;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.WarMachineStateListener;
import com.tankzor.game.util.FloatPoint;
import com.tankzor.game.util.QuadRectangle;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12/13/2016.
 */

public abstract class MovableBotWarMachine extends MovableWarMachine implements WarMachineStateListener, EmptyTerrainListener {
    protected AStarGridFinder<GridCell> pathFinder;
    protected NavigationGrid<GridCell> pathFindingMap;
    protected float attackRangeSize;
    protected float maxTerrainToTarget;
    protected List<FloatPoint> currentPathsLocation;
    protected WarMachine currentAttackTarget;
    protected WarMachine supportTarget;
    protected boolean isAttackTargetChangedLocation = false;
    protected boolean isSupportTargetChangedLocation = false;
    protected boolean isMoving = false;
    protected Array<GridPoint2> controlAreas;
    protected TerrainObservable terrainWaitingFor;

    protected QuadRectangle rangeBound;

    public MovableBotWarMachine(SpawnLocation spawnLocation,
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
                                WarMachineStateListener trackListener,
                                Array<GridPoint2> controlAreas,
                                int attackRange,
                                PathFindingProvider pathFindingProvider) {
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
                trackListener);
        this.controlAreas = controlAreas;
        setUpMapFindingSystems(pathFindingProvider);
        currentPathsLocation = new ArrayList<FloatPoint>();
        this.attackRangeSize = attackRange * ITEM_SIZE;
        this.maxTerrainToTarget = attackRange * 2;

        rangeBound = new QuadRectangle(getX() - (attackRange - ITEM_SIZE) / 2,
                getY() - (attackRange - ITEM_SIZE) / 2,
                attackRangeSize,
                attackRangeSize);
    }

    protected abstract void setUpMapFindingSystems(PathFindingProvider pathFindingProvider);

    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        super.drawDebugBounds(shapes);
        shapes.rect(getX() - (attackRangeSize - ITEM_SIZE) / 2,
                getY() - (attackRangeSize - ITEM_SIZE) / 2,
                attackRangeSize,
                attackRangeSize);
        for (int i = 0; i < currentPathsLocation.size(); i++) {
            shapes.rect(currentPathsLocation.get(i).x,
                    currentPathsLocation.get(i).y,
                    ITEM_SIZE,
                    ITEM_SIZE);
        }

        if (currentAttackTarget != null) {
            shapes.circle(currentAttackTarget.getX() + ITEM_SIZE / 2, currentAttackTarget.getY() + ITEM_SIZE / 2, ITEM_SIZE / 2);
        }
    }

    public QuadRectangle getRangeBound() {
        rangeBound.x = getX() - (attackRangeSize - ITEM_SIZE) / 2;
        rangeBound.y = getY() - (attackRangeSize - ITEM_SIZE) / 2;
        return rangeBound;
    }

    public void solveAttackTarget(Array<MovableWarMachine> listMovableTargetsInRange, Array<WarMachine> listImmovableTargetInRange) {
        WarMachine availableAttackTarget = null;
        WarMachine temp;
        List<GridCell> pathsToDestination = null;
        if (listMovableTargetsInRange.size != 0) {
            //solve movable target(s)
            availableAttackTarget = listMovableTargetsInRange.get(0);
            pathsToDestination = findPathTo(availableAttackTarget.getX(), availableAttackTarget.getY());
            for (int i = 1; i < listMovableTargetsInRange.size; i++) {
                temp = listMovableTargetsInRange.get(i);
                List<GridCell> tempPathToDestination = findPathTo(temp.getX(), temp.getY());
                if (tempPathToDestination.size() < pathsToDestination.size()) {
                    pathsToDestination = tempPathToDestination;
                    availableAttackTarget = temp;
                }
            }
        } else if (listImmovableTargetInRange.size != 0) {
            //solve immovable target(s)
            availableAttackTarget = listImmovableTargetInRange.get(0);
            pathsToDestination = findPathTo(availableAttackTarget.getX(), availableAttackTarget.getY());
            for (int i = 0; i < listImmovableTargetInRange.size; i++) {
                temp = listImmovableTargetInRange.get(i);
                List<GridCell> tempPathToDestination = findPathTo(temp.getX(), temp.getY());
                if (tempPathToDestination.size() < pathsToDestination.size()) {
                    pathsToDestination = tempPathToDestination;
                    availableAttackTarget = temp;
                }
            }
        }
        setCurrentAttackTarget(availableAttackTarget);
        if(pathsToDestination != null) {
            setupPathTo(pathsToDestination, true);
        }
    }

    public void setCurrentAttackTarget(WarMachine newAttackTarget) {
        if (currentAttackTarget != newAttackTarget) {
            if (currentAttackTarget != null) {
                currentAttackTarget.removeWarMachineActionListener(this);
            }
            currentAttackTarget = newAttackTarget;
            if (currentAttackTarget != null) {
                currentAttackTarget.addWarMachineStateListener(this);
                isAttackTargetChangedLocation = true;
            }
        }
    }

    public void setSupportTarget(WarMachine supportTarget) {
        this.supportTarget = supportTarget;
        if (supportTarget != null) {
            isSupportTargetChangedLocation = true;
        }
    }

    @Override
    public void addMoveAction() {
        if (currentPathsLocation.size() == 0) {
            hasNoMovingActionLeft();
            return;
        }
        FloatPoint location = currentPathsLocation.get(0);
        int nextOrient = findOrient(location.x, location.y);
        Terrain nextTerrain = mapInformationProvider.getTerrain((int) (location.x / ITEM_SIZE), (int) (location.y / ITEM_SIZE));
        if (nextTerrain.hasIncomingWarMachineEnter() || nextTerrain.hasWarMachineOn()) {
            SequenceAction otherAction = tryToFindOtherWay(nextOrient, false);
            if (otherAction != null) {
                isMoving = true;
                addAction(otherAction);
                isAttackTargetChangedLocation = true;
            } else {
                addAction(new WarMachineRotateAction(currentImageIndex));
                this.terrainWaitingFor = nextTerrain;
                this.terrainWaitingFor.registerEmptyTerrainListener(this);
            }
        } else {
            currentPathsLocation.remove(0);
            isMoving = true;
            if (nextOrient == currentImageIndex) {
                addAction(new MovingAction(currentImageIndex, moveTimePerTerrainBox));
            } else {
                addAction(Actions.sequence(new WarMachineRotateAction(nextOrient), new MovingAction(nextOrient, moveTimePerTerrainBox)));
            }
        }
    }

    public void getOutOfThisWay(int fromOrient) {
        if (isMoving) {
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
            isMoving = true;
            addAction(actions);
            isAttackTargetChangedLocation = true;
        }
    }

    protected SequenceAction tryToFindOtherWay(int invalidOrient, boolean canMoveBack) {//pass invalidOrient = -1 for 4 orient
        SequenceAction result;
        int i = (int) (getX() / ITEM_SIZE);
        int j = (int) (getY() / ITEM_SIZE);
        switch (invalidOrient) {
            case UP_ORIENT: {
                Terrain rightTerrain = mapInformationProvider.getTerrain(i + 1, j);
                result = getSuitableSequenceAction(rightTerrain, RIGHT_ORIENT);
                if (result != null) {
                    return result;
                }
                Terrain leftTerrain = mapInformationProvider.getTerrain(i - 1, j);
                result = getSuitableSequenceAction(leftTerrain, LEFT_ORIENT);
                if (result != null) {
                    return result;
                }
                if (canMoveBack) {
                    Terrain downTerrain = mapInformationProvider.getTerrain(i, j - 1);
                    result = getSuitableSequenceAction(downTerrain, DOWN_ORIENT);
                    if (result != null) {
                        return result;
                    }
                }
            }
            break;

            case DOWN_ORIENT: {
                Terrain leftTerrain = mapInformationProvider.getTerrain(i - 1, j);
                result = getSuitableSequenceAction(leftTerrain, LEFT_ORIENT);
                if (result != null) {
                    return result;
                }
                Terrain rightTerrain = mapInformationProvider.getTerrain(i + 1, j);
                result = getSuitableSequenceAction(rightTerrain, RIGHT_ORIENT);
                if (result != null) {
                    return result;
                }
                if (canMoveBack) {
                    Terrain upTerrain = mapInformationProvider.getTerrain(i, j + 1);
                    result = getSuitableSequenceAction(upTerrain, UP_ORIENT);
                    if (result != null) {
                        return result;
                    }
                }
            }
            break;

            case LEFT_ORIENT: {
                Terrain downTerrain = mapInformationProvider.getTerrain(i, j - 1);
                result = getSuitableSequenceAction(downTerrain, DOWN_ORIENT);
                if (result != null) {
                    return result;
                }
                Terrain upTerrain = mapInformationProvider.getTerrain(i, j + 1);
                result = getSuitableSequenceAction(upTerrain, UP_ORIENT);
                if (result != null) {
                    return result;
                }
                if (canMoveBack) {
                    Terrain rightTerrain = mapInformationProvider.getTerrain(i + 1, j);
                    result = getSuitableSequenceAction(rightTerrain, RIGHT_ORIENT);
                    if (result != null) {
                        return result;
                    }
                }
            }
            break;

            case RIGHT_ORIENT: {
                Terrain upTerrain = mapInformationProvider.getTerrain(i, j + 1);
                result = getSuitableSequenceAction(upTerrain, UP_ORIENT);
                if (result != null) {
                    return result;
                }
                Terrain downTerrain = mapInformationProvider.getTerrain(i, j - 1);
                result = getSuitableSequenceAction(downTerrain, DOWN_ORIENT);
                if (result != null) {
                    return result;
                }
                if (canMoveBack) {
                    Terrain leftTerrain = mapInformationProvider.getTerrain(i - 1, j);
                    result = getSuitableSequenceAction(leftTerrain, LEFT_ORIENT);
                    if (result != null) {
                        return result;
                    }
                }
            }
            break;

            default: {
                Terrain upTerrain = mapInformationProvider.getTerrain(i, j + 1);
                result = getSuitableSequenceAction(upTerrain, UP_ORIENT);
                if (result != null) {
                    return result;
                }
                Terrain downTerrain = mapInformationProvider.getTerrain(i, j - 1);
                result = getSuitableSequenceAction(downTerrain, DOWN_ORIENT);
                if (result != null) {
                    return result;
                }

                Terrain leftTerrain = mapInformationProvider.getTerrain(i - 1, j);
                result = getSuitableSequenceAction(leftTerrain, LEFT_ORIENT);
                if (result != null) {
                    return result;
                }

                Terrain rightTerrain = mapInformationProvider.getTerrain(i + 1, j);
                result = getSuitableSequenceAction(rightTerrain, RIGHT_ORIENT);
                if (result != null) {
                    return result;
                }
            }
            break;
        }
        return null;
    }

    protected SequenceAction getSuitableSequenceAction(Terrain terrain, int orient) {
        if (terrain != null &&
                !terrain.hasWallOn() &&
                !terrain.hasIncomingWarMachineEnter() &&
                !terrain.hasWarMachineOn()) {
            return Actions.sequence(new WarMachineRotateAction(orient), new MovingAction(orient, moveTimePerTerrainBox));
        }
        return null;
    }

    @Override
    public void onTerrainIsEmpty() {
        this.terrainWaitingFor = null;
    }

    protected void hasNoMovingActionLeft() {
        if (!updatePathToCurrentAttackTarget()) {
            setupDefaultMoving();
        }
    }

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
        List<GridCell> temp = pathFinder.findPath(iStart, jStart, iEnd, jEnd, pathFindingMap);
        if (temp != null) {
            pathsToDestination.addAll(temp);
        }
        return pathsToDestination;
    }

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

    protected boolean updatePathToCurrentAttackTarget() {
        if (isAttackTargetWithinAttackRange()) {
            return setupPathTo(findPathTo(currentAttackTarget.getX(), currentAttackTarget.getY()), true);
        }
        return false;
    }

    private boolean updatePathToSupportTarget() {
        return !isSupportTargetWithinSupportRange() && setupPathTo(findPathTo(supportTarget.getX(), supportTarget.getY()), false);
    }

    private boolean isAttackTargetWithinAttackRange() {
        return currentAttackTarget != null && getRangeBound().overlaps(currentAttackTarget.getQuadBound());
    }

    private boolean isSupportTargetWithinSupportRange() {
        return supportTarget != null && getBound().overlaps(supportTarget.getSupportBound());
    }

    @Override
    public void fire() {

    }

    @Override
    public void onWarMachineStartMoving(WarMachine warMachine) {

    }

    @Override
    public void onWarMachineStopMoving(WarMachine warMachine) {
        if (warMachine == currentAttackTarget) {
            isAttackTargetChangedLocation = true;
        } else {
            isSupportTargetChangedLocation = true;
        }
    }

    @Override
    public void onWarMachineDestroyed(WarMachine warMachine) {
        if (warMachine.getTeamID() + teamID == 0) {
            setCurrentAttackTarget(null);
        }
    }

    @Override
    protected boolean checkActingCondition(float delta) {
        if (WarMachineManager.isEnemiesFrenzy && teamID == ENEMIES_TEAM) {
            return false;
        }
        if (!isMoving) {
            if (currentAttackTarget == null) {
                ((WarMachineManager) getStage()).updateAttackTargetFor(this);
            }
            if (isAttackTargetChangedLocation) {
                isAttackTargetChangedLocation = false;
                if (!updatePathToCurrentAttackTarget()) {
                    setCurrentAttackTarget(null);
                    setupDefaultMoving();
                }
            }
            addMoveAction();
        }
        return true;
    }

    protected void setupDefaultMoving() {
        if (isSupportTargetChangedLocation) {
            updatePathToSupportTarget();
        } else {
            randomMovingWithinControlArea();
        }
    }

    protected void randomMovingWithinControlArea() {
        GridPoint2 randomLocation = controlAreas.random();
        setupPathTo(findPathTo(randomLocation.x * ITEM_SIZE, randomLocation.y * ITEM_SIZE), false);
    }

    @Override
    protected void onMovingActionStop() {
        super.onMovingActionStop();
        isMoving = false;
    }

    @Override
    protected void onMovingActionOnProcess() {

    }

    protected int findOrient(float xEnd, float yEnd) {
        float x = this.getX();
        float y = this.getY();

        if (x < xEnd + ITEM_SIZE && x + ITEM_SIZE > xEnd) {
            if (y > yEnd) {
                return DOWN_ORIENT;
            } else {
                return UP_ORIENT;
            }
        } else if (y < yEnd + ITEM_SIZE && y + ITEM_SIZE > yEnd) {
            if (x > xEnd) {
                return LEFT_ORIENT;
            } else {
                return RIGHT_ORIENT;
            }
        } else {
            return -1;
        }
    }

    @Override
    public void destroy() {
        if (currentAttackTarget != null) {
            currentAttackTarget.removeWarMachineActionListener(this);
        }
        super.destroy();
    }
}
