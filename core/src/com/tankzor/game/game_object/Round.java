package com.tankzor.game.game_object;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Admin on 12/30/2016.
 */

public class Round {
    protected Array<SpawnLocation> listSpawnLocations;
    private RoundSet parent;

    public Round() {
        listSpawnLocations = new Array<SpawnLocation>();
    }

    public void setParent(RoundSet parent) {
        this.parent = parent;
    }

    public RoundSet getParent() {
        return parent;
    }

    public int size() {
        return listSpawnLocations.size;
    }

    public void addSpawnLocation(SpawnLocation spawnLocation) {
        if (spawnLocation.size() == 0) {
            return;
        }
        spawnLocation.setParent(this);
        listSpawnLocations.add(spawnLocation);
    }

    public void removeSpawnLocation(SpawnLocation spawnLocation){
        listSpawnLocations.removeValue(spawnLocation, true);
        spawnLocation.setParent(null);
        if(listSpawnLocations.size == 0){
            parent.removeFirstRound();
        }
    }

    public void spawnWarMachine() {
        for (int i = listSpawnLocations.size - 1; i >= 0; i--) {
            listSpawnLocations.get(i).spawnWarMachine();
        }
    }

    public int getTotalWarMachineCount() {
        int count = 0;
        for (SpawnLocation spawnLocation : listSpawnLocations) {
            count += spawnLocation.getCurrentWarMachineCount();
        }
        return count;
    }
}
