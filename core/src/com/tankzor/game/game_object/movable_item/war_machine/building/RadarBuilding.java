package com.tankzor.game.game_object.movable_item.war_machine.building;

import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;

/**
 * Created by Admin on 1/6/2017.
 */

public class RadarBuilding extends Building {

    public RadarBuilding(float x, float y,
                         int teamId,
                         int hitPoint,
                         OnDynamicDamagedEntityMovingListener entityMovingListener,
                         OnEntityDestroyedListener entityDestroyedListener,
                         LightingManager lightingManager,
                         MapInformationProvider mapInformationProvider,
                         AirManager airManager,
                         AssetLoader assetLoader) {
        super(x, y,
                teamId,
                hitPoint,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader);
        this.image = assetLoader.getRadarBuildingImage();
    }

    @Override
    public void destroy() {
        ((WarMachineManager) getStage()).removeHQBuilding(this);
        super.destroy();
    }
}
