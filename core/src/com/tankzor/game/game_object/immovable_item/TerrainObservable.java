package com.tankzor.game.game_object.immovable_item;

import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 11/26/2016.
 */

public interface TerrainObservable {
    void registerTerrainListener(TerrainListener terrainListener, int orientFromObserver);
    void notifyItemEnteredToAllTerrainListeners(WarMachine warMachine);
    void notifyItemExitedToAllTerrainListener(WarMachine warMachine);
    void removeTerrainListener(TerrainListener terrainListener);

    void registerEmptyTerrainListener(EmptyTerrainListener emptyTerrainListener);
    void notifyEmptyTerrainListener();
    void removeEmptyTerrainListener(EmptyTerrainListener emptyTerrainListener);
}
