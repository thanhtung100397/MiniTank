package com.tankzor.game.game_object.immovable_item.wall;

import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.LightingManager;

/**
 * Created by Admin on 1/4/2017.
 */

public class WallFactory {
    public static Wall createWall(int id,
                                  Terrain parent,
                                  WallStateListener wallStateListener,
                                  OnEntityDestroyedListener itemDestroyedListener,
                                  LightingManager lightingManager){
        switch (id){
            case 107:
            case 108:{
                return new BrickWall(id, 4, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 109:
            case 110:{
                return new BrickWall(id, 3, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 111:
            case 112:{
                return new BrickWall(id, 2, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 113:
            case 114:{
                return new BrickWall(id, 1, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 115:
            case 116:{
                return new GrayConcreteWall(id, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 117:{
                return new YellowConcreteWall(id, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 118:
            case 119:{
                return new SteelWall(id, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 120:
            case 121:
            case 122:
            case 123:
            case 124:{
                return new PlantWall(id, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            case 125:
            case 126:
            case 127:{
                return new SpikeWall(id, parent, wallStateListener, itemDestroyedListener, lightingManager);
            }

            default:{
                return null;
            }
        }
    }
}
