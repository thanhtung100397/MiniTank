package com.tankzor.game.game_object.immovable_item.wall;

/**
 * Created by Admin on 11/23/2016.
 */

public interface WallStateListener {
    void repaintWall(Wall wall);
    void onWallDestroyed(Wall wall);
}
