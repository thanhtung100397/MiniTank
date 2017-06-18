package com.tankzor.game.game_object.movable_item.war_machine.movable_machine;

import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 11/25/2016.
 */

public interface WarMachineStateListener {
    void onWarMachineStartMoving(WarMachine warMachine);
    void onWarMachineStopMoving(WarMachine warMachine);
    void onWarMachineDestroyed(WarMachine warMachine);
}
