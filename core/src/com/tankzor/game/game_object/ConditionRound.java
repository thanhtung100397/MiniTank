package com.tankzor.game.game_object;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.TerrainListener;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 1/12/2017.
 */

public class ConditionRound extends Round implements TerrainListener{
    private Array<Terrain> terrainsListening;

    public ConditionRound(Array<GridPoint2> terrainListeningLocation, MapInformationProvider mapInformationProvider){
        this.terrainsListening = new Array<Terrain>();
        Terrain terrain;
        for (GridPoint2 location : terrainListeningLocation){
            terrain = mapInformationProvider.getTerrain(location.x, location.y);
            terrain.registerTerrainListener(this, -1);
            terrainsListening.add(terrain);
        }
    }

    @Override
    public void removeSpawnLocation(SpawnLocation spawnLocation) {
        listSpawnLocations.removeValue(spawnLocation, true);
        if(listSpawnLocations.size == 0){
            getParent().removeConditionRound(this);
        }
    }

    @Override
    public void onItemEntered(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {
        spawnWarMachine();
        for (Terrain terrain : terrainsListening) {
            terrain.removeTerrainListener(this);
        }
    }

    @Override
    public void onItemExited(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {

    }
}
