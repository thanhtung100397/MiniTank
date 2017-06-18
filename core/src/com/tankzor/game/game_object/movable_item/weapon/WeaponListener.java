package com.tankzor.game.game_object.movable_item.weapon;

/**
 * Created by Admin on 1/25/2017.
 */
public interface WeaponListener  {
    void onNoWeaponLeft();
    void onCurrentWeaponSwitch(WeaponManager.WeaponItem weaponItem);
}
