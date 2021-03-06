package com.tankzor.game.game_object.immovable_item.wall;

import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Dynamite;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 1/4/2017.
 */

public class YellowConcreteWall extends Wall {
    public static final int BULLET_HEIGHT_PASS = Bullet.MEDIUM_BULLET;

    public YellowConcreteWall(int id,
                              Terrain parentTerrain,
                              WallStateListener wallStateListener,
                              OnEntityDestroyedListener itemDestroyedListener,
                              LightingManager lightingManager) {
        super(id, 1, parentTerrain, wallStateListener, itemDestroyedListener, lightingManager);
        setType(YELLOW_CONCRETE_TYPE);
    }

    @Override
    public int takeDamage(Bullet bullet) {
        if(bullet.getBulletHeight() >= BULLET_HEIGHT_PASS){
            return bullet.getHitPoint();
        }
        bullet.setHitPoint(0);
        return 0;
    }
}
