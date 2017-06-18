package com.tankzor.game.game_object.immovable_item.wall;

import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 1/4/2017.
 */

public class BrickWall extends Wall {
    public static final int BULLET_HEIGHT_PASS = Bullet.MEDIUM_BULLET;

    public BrickWall(int id,
                     int hitPoint,
                     Terrain parentTerrain,
                     WallStateListener wallStateListener,
                     OnEntityDestroyedListener itemDestroyedListener, LightingManager lightingManager) {
        super(id, hitPoint, parentTerrain, wallStateListener, itemDestroyedListener, lightingManager);
        setType(BRICK_TYPE);
    }

    @Override
    public int takeDamage(Bullet bullet) {
        if (bullet.getBulletHeight() >= BULLET_HEIGHT_PASS) {
            return bullet.getHitPoint();
        }
        int damage = bullet.getHitPoint();
        if (hitPoint > damage) {
            setHitPoint(hitPoint - damage);
            bullet.setHitPoint(0);
            return 0;
        }
        int resultDamage = damage - hitPoint;
        setHitPoint(0);
        bullet.setHitPoint(resultDamage);
        return resultDamage;
    }
}
