package com.tankzor.game.game_object.immovable_item;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.game_object.TerrainExplosionListener;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.movable_item.DynamicDamagedEntity;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Mine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Missile;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 11/13/2016.
 */

public class Terrain implements TerrainObservable {
    private static final int GRASS_TYPE = 0;
    private static final int DESERT_TYPE = 1;
    private static final int ROAD_TYPE = 2;
    private static final int BRICK_TYPE = 3;

    private float x, y;
    private int id;
    private int type;
    private boolean canCreateHole;
    private Wall wallOn;
    private Array<DynamicDamagedEntity> listItemOn;
    private Array<TerrainObserverHolder> listTerrainObserverHolders;
    private Array<EmptyTerrainListener> listEmptyTerrainListeners;
    private TerrainExplosionListener terrainExplosionListener;
    private SupportItem supportItemOn;
    private WarMachine warMachineOn;
    private WarMachine incomingWarMachineEnter;
    private Mine mine;
    private int speedBoostType;

    public Terrain(int id, float x, float y, TerrainExplosionListener terrainExplosionListener) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.type = findTileTypeById(this.id);
        this.canCreateHole = canTerrainHaveHole(this.id);
        listItemOn = new Array<DynamicDamagedEntity>(4);
        listTerrainObserverHolders = new Array<TerrainObserverHolder>();
        listEmptyTerrainListeners = new Array<EmptyTerrainListener>();
        this.terrainExplosionListener = terrainExplosionListener;
    }

    public int getSpeedBoostType() {
        return speedBoostType;
    }

    public void putWallOn(Wall wall) {//null to remove current wall
        this.wallOn = wall;
    }

    public void removeWallOn() {
        this.wallOn = null;
    }

    public void putSupportItemOn(SupportItem supportItem) {
        this.supportItemOn = supportItem;
    }

    public void removeSupportItemOn() {
        if (this.supportItemOn != null) {
            this.supportItemOn.remove();
        }
        this.supportItemOn = null;
    }

    public void registerIncomingWarMachineEnter(WarMachine incomingWarMachine) {
        if (this.incomingWarMachineEnter == null && incomingWarMachine != null) {
            this.incomingWarMachineEnter = incomingWarMachine;
        }
    }

    public void unRegisterIncomingWarMachineEnter(WarMachine incomingWarMachine) {
        if (this.incomingWarMachineEnter == incomingWarMachine) {
            this.incomingWarMachineEnter = null;
        }
    }

    public void putMineOn(Mine mine) {
        this.mine = mine;
    }

    public boolean hasMineOn() {
        return mine != null;
    }

    public void removeMineOn() {
        if (this.mine != null) {
            this.mine.remove();
        }
        this.mine = null;
    }

    public SupportItem getSupportItemOn() {
        return supportItemOn;
    }

    /**
     * Attach a DynamicDamagedEntity to this terrain.
     * @return true if attach the item successfully, false otherwise (item destroyed)
     */
    public boolean addItemOn(DynamicDamagedEntity item) {
        int hitPointResult = takeDamage(item);
        if (hitPointResult == 0) {
            return false;
        }
        if(checkItemCollision(item)){
            listItemOn.add(item);
            if (item == incomingWarMachineEnter) {
                warMachineOn = incomingWarMachineEnter;
                incomingWarMachineEnter = null;
                notifyItemEnteredToAllTerrainListeners((WarMachine) item);
            }
            return true;
        }
        return false;
    }

    public void removeItemOn(DynamicDamagedEntity item) {
        listItemOn.removeValue(item, true);
        if (item instanceof WarMachine) {
            warMachineOn = null;
            notifyItemExitedToAllTerrainListener((WarMachine) item);
            notifyEmptyTerrainListener();
        }
    }

    public int takeDamage(DynamicDamagedEntity item) {
        if (item instanceof Bullet && wallOn != null) {
            if (item instanceof Missile) {
                item.destroy();
                return 0;
            }

            int result = wallOn.takeDamage((Bullet) item);
            if (wallOn.getHitPoint() == 0) {
                wallOn.setExplosionType(-item.getExplosionType());
                wallOn.destroy();
                notifyEmptyTerrainListener();
            }
            return result;
        }
        return item.getHitPoint();
    }

    public int takeDamage(AreaWeapon areaWeapon) {
        if (wallOn != null) {
            wallOn.takeDamage(areaWeapon);
            if (wallOn.getHitPoint() == 0) {
                wallOn.destroyWithoutExplode();
            }
        } else {
            int itemOnCount = listItemOn.size;
            for (int i = itemOnCount - 1; i >= 0; i--) {//must loop from end to start
                if(listItemOn.size <= i){//TODO fix???
                    break;
                }
                listItemOn.get(i).takeDamage(areaWeapon.getHitPoint(), areaWeapon.isPlayerItem());
            }
        }
        removeMineOn();
        removeSupportItemOn();

        if (canCreateHole && MathUtils.random(0, 9) < Wall.EXPLOSION_HOLE_CHANCE) {
            createHole();
        }
        return 0;
    }

    public void removeAllObjectOn() {
        if (wallOn != null) {
            wallOn.setHitPoint(0);
            wallOn.destroyWithoutExplode();
            wallOn = null;
        }

        if (mine != null) {
            mine.destroy();
            mine = null;
        }

        if (supportItemOn != null) {
            supportItemOn.remove();
            supportItemOn = null;
        }
    }

    public boolean hasWallOn() {
        return wallOn != null;
    }

    /**
     * Check collision of enter item with all items was stored in this terrain.
     *
     * @return true if enterItem is NOT destroyed after all collision, false otherwise
     */
    public boolean checkItemCollision(DynamicDamagedEntity enterItem) {
        DynamicDamagedEntity currentCheckingCollisionItem;
        for (int i = listItemOn.size - 1; i >= 0; i--) {//must loop end-start
            if(listItemOn.size <= i){//TODO fix
                break;
            }
            currentCheckingCollisionItem = listItemOn.get(i);
            if ((enterItem.getTeamID() + currentCheckingCollisionItem.getTeamID() == 0) &&
                    enterItem.isCollisionWith(currentCheckingCollisionItem)) {
                int enterItemHitPointResult = currentCheckingCollisionItem.takeDamage(enterItem.getHitPoint(), enterItem.isPlayerItem());
                enterItem.setHitPoint(enterItemHitPointResult);
                if (enterItem.isPlayerItem() && currentCheckingCollisionItem instanceof WarMachine) {
                    terrainExplosionListener.getGameHud().showOtherHealDisplay((WarMachine) currentCheckingCollisionItem);
                }
                if (enterItemHitPointResult == 0) {
                    return false;
                }
                if(listItemOn.size == 0){
                    break;
                }
            }
        }
        return true;
    }

    public Wall getWallOn() {
        return wallOn;
    }

    public int getId() {
        return id;
    }

    @Override
    public void registerTerrainListener(TerrainListener terrainListener, int orientFromObserver) {
        listTerrainObserverHolders.add(new TerrainObserverHolder(terrainListener, orientFromObserver));
    }

    @Override
    public void notifyItemEnteredToAllTerrainListeners(WarMachine warMachine) {
        int size = listTerrainObserverHolders.size;
        for (int i = size - 1; i >= 0; i--) {
            TerrainObserverHolder holder = listTerrainObserverHolders.get(i);
            holder.terrainListener.onItemEntered(holder, warMachine);
        }
    }

    @Override
    public void notifyItemExitedToAllTerrainListener(WarMachine warMachine) {
        int size = listTerrainObserverHolders.size;
        for (int i = size - 1; i >= 0; i--) {
            TerrainObserverHolder holder = listTerrainObserverHolders.get(i);
            holder.terrainListener.onItemExited(holder, warMachine);
        }
    }

    @Override
    public void removeTerrainListener(TerrainListener terrainListener) {
        int size = listTerrainObserverHolders.size;
        for (int i = size - 1; i >= 0; i--) {
            if (listTerrainObserverHolders.get(i).terrainListener == terrainListener) {
                listTerrainObserverHolders.removeIndex(i);
                break;
            }
        }
    }

    @Override
    public void registerEmptyTerrainListener(EmptyTerrainListener emptyTerrainListener) {
        if (!hasWarMachineOn() && !hasIncomingWarMachineEnter() && !hasWallOn()) {
            emptyTerrainListener.onTerrainIsEmpty();
            return;
        }
        listEmptyTerrainListeners.add(emptyTerrainListener);
    }

    public void notifyEmptyTerrainListener() {
        if (listEmptyTerrainListeners.size != 0) {
            listEmptyTerrainListeners.removeIndex(0).onTerrainIsEmpty();
        }
    }

    @Override
    public void removeEmptyTerrainListener(EmptyTerrainListener emptyTerrainListener) {
        listEmptyTerrainListeners.removeValue(emptyTerrainListener, true);
    }

    public Mine getMine() {
        return mine;
    }

    public boolean hasWarMachineOn() {
        return warMachineOn != null;
    }

    public boolean hasIncomingWarMachineEnter() {
        return incomingWarMachineEnter != null;
    }

    public WarMachine getWarMachineOn() {
        return warMachineOn;
    }

    public boolean createHole() {
        if (canCreateHole) {
            this.id = getRandomHoleTileID(type);
            terrainExplosionListener.repaintTerrain(this);
            canCreateHole = false;
            return true;
        }
        return false;
    }

    public boolean canCreateHole() {
        return canCreateHole;
    }

    public static int getRandomHoleTileID(int terrainType) {
        switch (terrainType) {
            case Terrain.GRASS_TYPE: {
                return MathUtils.random(53, 55);
            }

            case Terrain.DESERT_TYPE: {
                return MathUtils.random(80, 82);
            }

            case Terrain.ROAD_TYPE: {
                return terrainType;
            }

            case Terrain.BRICK_TYPE: {
                return MathUtils.random(50, 52);
            }

            default: {
                return -1;
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static boolean canTerrainHaveHole(int id) {
        return (1 <= id && id <= 6) ||
                (34 <= id && id <= 39) ||
                (56 <= id && id <= 60);
    }

    public int findTileTypeById(int terrainId) {
        if ((1 <= terrainId && terrainId <= 10) ||
                (15 <= terrainId && terrainId <= 23) ||
                (53 <= terrainId && terrainId <= 55) ||
                (101 <= terrainId && terrainId <= 106)) {
            speedBoostType = MovableWarMachine.NO_BOOST;
            return Terrain.GRASS_TYPE;
        } else if ((11 <= terrainId && terrainId <= 14) ||
                (56 <= terrainId && terrainId <= 90)) {
            speedBoostType = MovableWarMachine.SLOW_DOWN;
            return Terrain.DESERT_TYPE;
        } else if ((24 <= terrainId && terrainId <= 33) ||
                (91 <= terrainId && terrainId <= 100)) {
            speedBoostType = MovableWarMachine.HIGH_SPEED;
            return Terrain.ROAD_TYPE;
        } else {
            speedBoostType = MovableWarMachine.HIGH_SPEED;
            return Terrain.BRICK_TYPE;
        }
    }

    public static class TerrainObserverHolder {
        public int orientFromObserver;
        public TerrainListener terrainListener;

        private TerrainObserverHolder(TerrainListener terrainListener, int orientFromObserver) {
            this.terrainListener = terrainListener;
            this.orientFromObserver = orientFromObserver;
        }
    }

}
