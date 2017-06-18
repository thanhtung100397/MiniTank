package com.tankzor.game.game_object.movable_item.weapon;

/**
 * Created by Admin on 3/21/2017.
 */

public interface OnWeaponCapacityListener {
    void onWeaponChangeCapacity(int newCapacity);
    void onWeaponRemoved();
}
