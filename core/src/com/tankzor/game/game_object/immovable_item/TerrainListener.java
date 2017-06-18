package com.tankzor.game.game_object.immovable_item;

import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 11/26/2016.
 */

public interface TerrainListener {
    void onItemEntered(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine);
    void onItemExited(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine);
}