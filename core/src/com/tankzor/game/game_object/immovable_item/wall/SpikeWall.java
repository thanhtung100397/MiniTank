package com.tankzor.game.game_object.immovable_item.wall;

import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 1/4/2017.
 */

public class SpikeWall extends Wall {

    public SpikeWall(int id,
                     Terrain parentTerrain,
                     WallStateListener wallStateListener,
                     OnEntityDestroyedListener itemDestroyedListener,
                     LightingManager lightingManager) {
        super(id, -1, parentTerrain, wallStateListener, itemDestroyedListener, lightingManager);
        setType(SPIKE_TYPE);
    }

    @Override
    protected void initBody() {

    }

    @Override
    public int takeDamage(Bullet bullet) {
        return bullet.getHitPoint();
    }

    @Override
    public int takeDamage(int damage, boolean isPlayer) {
        setHitPoint(0);
        return 0;
    }
}
