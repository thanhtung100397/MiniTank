package com.tankzor.game.game_object.manager;

import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;

/**
 * Created by Admin on 5/22/2017.
 */

public interface MapInformationProvider {
    boolean checkMovable(int iDes, int jDes, int orient);
    float getWidthMap();
    float getHeightMap();
    int getColumnNumber();
    int getRowNumber();
    boolean takeItemIfPossible(MovableWarMachine movableMachine);
    boolean canTranslateCamera(float x, float y, int orient);
    GroundWeaponManager getGroundWeaponManager();
    Terrain getTerrain(int i, int j);
}
