package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.badlogic.gdx.math.GridPoint2;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.animation.Explosion;
import com.tankzor.game.game_object.animation.OnExplosionFinishListener;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12/1/2016.
 */

public abstract class AreaWeapon extends DamagedEntity {
    public static final float BIG_EXPLODE_AREA_DELAY_TIME = 0.25f;
    public static final int ARTILLERY = 8;
    public static final int MINE_TMD_5 = 9;
    public static final int MINE_TMD_9 = 10;
    public static final int DYNAMITE_X70 = 11;
    public static final int DYNAMITE_X100 = 12;
    public static final int DYNAMITE_X160 = 13;
    public static final int HOMING_MISSILE = 14;
    public static final int AIR_STRIKE = 15;

    protected WarMachine warMachine;
    protected int level;
    protected int damageLevel = Wall.SPIKE_TYPE;
    protected OnExplosionFinishListener explosionFinishListener;
    protected MapInformationProvider mapInformationProvider;
    private int i, j;
    private boolean isMassive;

    public AreaWeapon(WarMachine warMachine, int damage, int level) {
        super((int)(warMachine.getX() / ITEM_SIZE) * ITEM_SIZE,
                (int)(warMachine.getY() / ITEM_SIZE) * ITEM_SIZE,
                ITEM_SIZE, ITEM_SIZE,
                0,
                damage,
                warMachine.getEntityDestroyedListener(),
                warMachine.getLightingManager());
        this.level = level;
        this.isPlayerItem = warMachine instanceof PlayerWarMachine;
        this.warMachine = warMachine;
        this.mapInformationProvider = warMachine.getMapInformationProvider();
        this.i = (int) (getX() / GameEntity.ITEM_SIZE);
        this.j = (int) (getY() / GameEntity.ITEM_SIZE);
    }

    public AreaWeapon(float x, float y,
                      int damage,
                      int level,
                      OnEntityDestroyedListener entityDestroyedListener,
                      MapInformationProvider mapInformationProvider,
                      LightingManager lightingManager){
        super(x, y,
                ITEM_SIZE, ITEM_SIZE,
                0,
                damage,
                entityDestroyedListener,
                lightingManager);
        this.level = level;
        this.isPlayerItem = false;
        this.mapInformationProvider = mapInformationProvider;
        this.i = (int) (x / GameEntity.ITEM_SIZE);
        this.j = (int) (y / GameEntity.ITEM_SIZE);
    }

    public void setDamageLevel(int damageLevel) {
        this.damageLevel = damageLevel;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public void setMassive(boolean massive) {
        isMassive = massive;
    }

    public boolean isMassive() {
        return isMassive;
    }

    @Override
    protected void initBody() {

    }

    protected abstract void createAction();

    public int getLevel() {
        return level;
    }

    public List<GridPoint2> getExplosionAreaIndexes(int level) {
        List<GridPoint2> result = new ArrayList<GridPoint2>();
        switch (level) {
            case 0: {
                addGridPoint(result, i, j);
            }
            break;

            case 1: {
                addGridPoint(result, i, j + 1);
                addGridPoint(result, i + 1, j);
                addGridPoint(result, i, j - 1);
                addGridPoint(result, i - 1, j);
            }
            break;

            case 2: {
                addGridPoint(result, i, j + 2);
                addGridPoint(result, i + 1, j + 1);
                addGridPoint(result, i + 2, j);
                addGridPoint(result, i + 1, j - 1);
                addGridPoint(result, i, j - 2);
                addGridPoint(result, i - 1, j - 1);
                addGridPoint(result, i - 2, j);
                addGridPoint(result, i - 1, j + 1);
            }
            break;

            case 3: {
                addGridPoint(result, i, j + 3);
                addGridPoint(result, i + 1, j + 2);
                addGridPoint(result, i + 2, j + 1);
                addGridPoint(result, i + 3, j);
                addGridPoint(result, i + 2, j - 1);
                addGridPoint(result, i + 1, j - 2);
                addGridPoint(result, i, j - 3);
                addGridPoint(result, i - 1, j - 2);
                addGridPoint(result, i - 2, j - 1);
                addGridPoint(result, i - 3, j);
                addGridPoint(result, i - 2, j + 1);
                addGridPoint(result, i - 1, j + 2);
            }
            break;

//            case 4: {
//                addGridPoint(result, i, j + 4);
//                addGridPoint(result, i + 1, j + 3);
//                addGridPoint(result, i + 2, j + 2);
//                addGridPoint(result, i + 3, j + 1);
//                addGridPoint(result, i + 4, j);
//                addGridPoint(result, i + 3, j - 1);
//                addGridPoint(result, i + 2, j - 2);
//                addGridPoint(result, i + 1, j - 3);
//                addGridPoint(result, i, j - 4);
//                addGridPoint(result, i - 1, j - 3);
//                addGridPoint(result, i - 2, j - 2);
//                addGridPoint(result, i - 3, j - 1);
//                addGridPoint(result, i - 4, j);
//                addGridPoint(result, i - 3, j + 1);
//                addGridPoint(result, i - 2, j + 2);
//                addGridPoint(result, i - 1, j + 3);
//            }
//            break;

            default: {
                break;
            }
        }
        return result;
    }

    private void addGridPoint(List<GridPoint2> list, int i, int j) {
        if ((i < 0 || i > mapInformationProvider.getColumnNumber() - 1) ||
                (j < 0 || j > mapInformationProvider.getRowNumber() - 1)) {
            return;
        }
        list.add(new GridPoint2(i, j));
    }

    public abstract int getDamageByLevel(int level);

    //Just call it, Anything in its explosion area will be damaged!
    @Override
    public void destroy() {
        i = Math.round(getX() / ITEM_SIZE);
        j = Math.round(getY() / ITEM_SIZE);
        entityDestroyedListener.onAreaWeaponDestroyed(this);
        super.destroy();
    }

    public void setOnExplosionFinishListener(OnExplosionFinishListener explosionFinishListener) {
        this.explosionFinishListener = explosionFinishListener;
    }

    public OnExplosionFinishListener getExplosionFinishListener() {
        return explosionFinishListener;
    }

    public int getLevelExplosionType(int level){
        if(level >= this.level && this.level != 3){
            return Explosion.SMALL_NORMAL_EXPLOSION;
        }else {
            return Explosion.BIG_NORMAL_EXPLOSION;
        }
    }
}
