package com.tankzor.game.game_object.immovable_item.wall;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 11/13/2016.
 */

public abstract class Wall extends DamagedEntity {
    public static final int BRICK_TYPE = 0;
    public static final int PLANT_TYPE = 1;
    public static final int SPIKE_TYPE = 2;
    public static final int YELLOW_CONCRETE_TYPE = 3;
    public static final int GRAY_CONCRETE_TYPE = 4;
    public static final int STEEL_TYPE = 5;

    public static final int EXPLOSION_HOLE_CHANCE = 4;

    private int id;
    private int type;
    private WallStateListener wallStateListener;

    public Wall(int id,
                int hitPoint,
                Terrain parentTerrain,
                WallStateListener wallStateListener,
                OnEntityDestroyedListener entityDestroyedListener,
                LightingManager lightingManager) {
        super(parentTerrain.getX(), parentTerrain.getY(),
                ITEM_SIZE, ITEM_SIZE,
                0,
                hitPoint,
                entityDestroyedListener,
                lightingManager);
        this.id = id;
        this.wallStateListener = wallStateListener;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public abstract int takeDamage(Bullet bullet);

    public void takeDamage(AreaWeapon areaWeapon){
        if(areaWeapon.getDamageLevel() >= getType()){
            takeDamage(areaWeapon.getHitPoint(), areaWeapon.isPlayerItem());
        }
    }

    public void setHitPoint(int newValue) {
        if (newValue < 0) {
            return;
        }
        this.hitPoint = newValue;
        this.id = getTileIdByHitPoint(type, hitPoint);
        wallStateListener.repaintWall(this);
    }

    @Override
    public void destroy() {
        wallStateListener.onWallDestroyed(this);
        entityDestroyedListener.onWallDestroyed(this, null);
        if(lightingManager != null && body != null) {
            lightingManager.getLightingWorld().destroyBody(body);
        }
    }

    public void destroyWithoutExplode(){
        wallStateListener.onWallDestroyed(this);
        if(lightingManager != null && body != null) {
            lightingManager.getLightingWorld().destroyBody(body);
        }
    }

    public static int getTileIdByHitPoint(int type, int hitPoint) {
        if (type == BRICK_TYPE && hitPoint > 0) {
            int startId = 15 - 2 * hitPoint + 100;
            return MathUtils.random(startId, startId + 1);
        }
        return 0;
    }
}