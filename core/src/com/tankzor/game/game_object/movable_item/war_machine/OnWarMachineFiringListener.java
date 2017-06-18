package com.tankzor.game.game_object.movable_item.war_machine;

import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;

/**
 * Created by Admin on 11/24/2016.
 */

public interface OnWarMachineFiringListener {
    void onPlayerFired(PlayerWarMachine playerWarMachine);
    void onBotFired(WarMachine warMachine);
    void onFiredAreaWeapon(AreaWeapon areaWeapon);
}
