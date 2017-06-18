package com.tankzor.game.game_object;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.TerrainListener;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 6/7/2017.
 */

public class RemoteTrigger implements TerrainListener{
    private Array<Terrain> listTerrainsListening;

    public RemoteTrigger(Array<GridPoint2> listLocationsListening, MapInformationProvider mapInformationProvider) {
        listTerrainsListening = new Array<Terrain>(listLocationsListening.size);
        for (GridPoint2 location : listLocationsListening){
            Terrain terrain = mapInformationProvider.getTerrain(location.x, location.y);
            terrain.registerTerrainListener(this, -1);
            listTerrainsListening.add(terrain);
        }
    }


    @Override
    public void onItemEntered(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {

    }

    @Override
    public void onItemExited(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {

    }

    public void remove(){
        for (int i = 0; i < listTerrainsListening.size; i++) {
            listTerrainsListening.get(i).removeTerrainListener(this);
        }
    }
}
