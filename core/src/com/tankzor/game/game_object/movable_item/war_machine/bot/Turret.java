package com.tankzor.game.game_object.movable_item.war_machine.bot;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.*;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 11/25/2016.
 */

public class Turret extends BotWarMachine {
    public static final int ALL_ORIENT = -1;

    private OnWarMachineFiringListener firingListener;
    private float currentReloadTime = 0;
    private int bulletHeight;
    private int rangeSize;
    private Array<Terrain> listTerrainInRange;
    private float currentIdleTime = 0;

    public Turret(float x, float y,
                  int teamID,
                  int hitPoint,
                  OnDynamicDamagedEntityMovingListener onDynamicDamagedEntityMovingListener,
                  OnEntityDestroyedListener itemDestroyedListener,
                  LightingManager lightingManager,
                  MapInformationProvider mapInformationProvider,
                  AirManager airManager,
                  AssetLoader assetLoader,
                  OnWarMachineFiringListener firingListener,
                  int rangeSize,
                  int bulletHeight,
                  int orient,
                  WeaponModel weaponModel) {
        super(x, y,
                TURRET_TYPE,
                teamID,
                hitPoint,
                onDynamicDamagedEntityMovingListener,
                itemDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader);
        this.bulletHeight = bulletHeight;
        this.firingListener = firingListener;
        this.baseWeaponModel = weaponModel;
        this.currentReloadTime = weaponModel.reloadTime;
        this.rangeSize = rangeSize;
        listTerrainInRange = new Array<Terrain>();

        switch (orient) {
            case ALL_ORIENT: {
                this.timeToPerformRandomRotate = 2.0f;
                setUpTurretRange(UP_ORIENT);
                setUpTurretRange(DOWN_ORIENT);
                setUpTurretRange(LEFT_ORIENT);
                setUpTurretRange(RIGHT_ORIENT);
            }
            break;

            default: {
                this.currentImageIndex = orient;
                this.timeToPerformRandomRotate = -1;
                setUpTurretRange(orient);
            }
            break;
        }

//        setDebug(true);
    }

    @Override
    public int getBulletHeight() {
        return bulletHeight;
    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        super.drawDebugBounds(shapes);
        for (Terrain terrain : listTerrainInRange) {
            shapes.rect(terrain.getX(), terrain.getY(), GameEntity.ITEM_SIZE, GameEntity.ITEM_SIZE);
        }
    }

    private void setUpTurretRange(int orient) {
        int i = (int) (getX() / GameEntity.ITEM_SIZE);
        int j = (int) (getY() / GameEntity.ITEM_SIZE);
        Terrain temp;
        int index;
        int row = mapInformationProvider.getRowNumber();
        int column = mapInformationProvider.getColumnNumber();

        int startIndex;
        int endIndex;
        switch (orient) {
            case UP_ORIENT: {
                startIndex = j + 1;
                endIndex = startIndex + rangeSize;
                endIndex = (endIndex < row) ? endIndex : row;
                for (index = startIndex; index < endIndex; index++) {
                    temp = mapInformationProvider.getTerrain(i, index);
                    if (!isTerrainInRangeValid(temp)) {
                        break;
                    }
                    temp.registerTerrainListener(this, orient);
                    listTerrainInRange.add(temp);
                }
            }
            break;

            case DOWN_ORIENT: {
                startIndex = j - 1;
                endIndex = startIndex - rangeSize;
                endIndex = (endIndex > 0) ? endIndex : 0;
                for (index = startIndex; index > endIndex; index--) {
                    temp = mapInformationProvider.getTerrain(i, index);
                    if (!isTerrainInRangeValid(temp)) {
                        break;
                    }
                    temp.registerTerrainListener(this, orient);
                    listTerrainInRange.add(temp);
                }
            }
            break;

            case LEFT_ORIENT: {
                startIndex = i - 1;
                endIndex = startIndex - rangeSize;
                endIndex = (endIndex > 0) ? endIndex : 0;
                for (index = startIndex; index > endIndex; index--) {
                    temp = mapInformationProvider.getTerrain(index, j);
                    if (!isTerrainInRangeValid(temp)) {
                        break;
                    }
                    temp.registerTerrainListener(this, orient);
                    listTerrainInRange.add(temp);
                }
            }
            break;

            case RIGHT_ORIENT: {
                startIndex = i + 1;
                endIndex = startIndex + rangeSize;
                endIndex = (endIndex < column) ? endIndex : column;
                for (index = startIndex; index < endIndex; index++) {
                    temp = mapInformationProvider.getTerrain(index, j);
                    if (!isTerrainInRangeValid(temp)) {
                        break;
                    }
                    temp.registerTerrainListener(this, orient);
                    listTerrainInRange.add(temp);
                }
            }
            break;

            default: {
                break;
            }
        }
    }

    private boolean isTerrainInRangeValid(Terrain terrain) {
        Wall wallOnTerrain = terrain.getWallOn();
        return Bullet.canDestroy(baseWeaponModel.id, wallOnTerrain) || Bullet.canFireOver(bulletHeight, wallOnTerrain);
    }

    @Override
    public void fire() {
        firingListener.onBotFired(this);
    }

    @Override
    protected boolean checkActingCondition(float delta) {
        if (WarMachineManager.isEnemiesFrenzy && teamID == ENEMIES_TEAM) {
            return false;
        }
        if (canFire) {
            if (currentRotateAction == null) {
                if ((currentReloadTime >= baseWeaponModel.reloadTime)) {
                    fire();
                    currentReloadTime = 0;
                }
            }
        } else if (timeToPerformRandomRotate != -1) {
            if (currentIdleTime >= timeToPerformRandomRotate) {
                setNextOrient(MathUtils.random(3) * 4);
                currentIdleTime = 0;
            } else {
                currentIdleTime += delta;
            }
        }
        if (currentReloadTime < baseWeaponModel.reloadTime) {
            currentReloadTime += delta;
        }
        return true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    protected void onStopRotate() {
        super.onStopRotate();
    }

    @Override
    protected void drawDecorateItem(Batch batch) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void destroy() {
        for (Terrain terrain : listTerrainInRange) {
            terrain.removeTerrainListener(this);
        }
        ((WarMachineManager) getStage()).removeTurret(this);
        super.destroy();
    }

    @Override
    public String toString() {
        return "Turret " + ((int) getX() / GameEntity.ITEM_SIZE) + " " + ((int) getY() / GameEntity.ITEM_SIZE)+" hp: "+getHitPoint();
    }
}
