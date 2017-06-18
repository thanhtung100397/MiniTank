package com.tankzor.game.game_object;

import com.tankzor.game.game_object.animation.OnExplosionFinishListener;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.building.Building;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 11/24/2016.
 */

public interface OnEntityDestroyedListener {
    void onWallDestroyed(Wall wall, OnExplosionFinishListener explosionFinishListener);
    void onBulletDestroyed(Bullet bullet, OnExplosionFinishListener explosionFinishListener);
    void onWarMachineDestroyed(WarMachine warMachine, OnExplosionFinishListener explosionFinishListener);
    void onBuildingDestroyed(Building building, OnExplosionFinishListener explosionFinishListener);
    void onAreaWeaponDestroyed(AreaWeapon areaWeapon);
}
