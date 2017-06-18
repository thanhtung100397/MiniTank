package com.tankzor.game.game_object;

import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.gamehud.GameHUD;

/**
 * Created by Admin on 11/18/2016.
 */

public interface TerrainExplosionListener {
    GameHUD getGameHud();
    void repaintTerrain(Terrain terrain);
}
