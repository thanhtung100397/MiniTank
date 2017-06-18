package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 12/28/2016.
 */

public class HomingMissile extends AreaWeapon {

    public HomingMissile(WarMachine warMachine, int damage, int level) {
        super(warMachine, damage, level);
    }

    @Override
    protected void createAction() {

    }

    @Override
    public int getDamageByLevel(int level) {
        return 0;
    }
}
