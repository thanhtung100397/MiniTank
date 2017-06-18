package com.tankzor.game.game_object.movable_item.war_machine.bot;

import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.TerrainListener;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 11/26/2016.
 */

public abstract class BotWarMachine extends WarMachine implements TerrainListener {
    protected boolean canFire;
    protected float timeToPerformRandomRotate;
    protected Array<Terrain.TerrainObserverHolder> queueOpenFireOrient;
    protected Terrain.TerrainObserverHolder currentTargetTerrain;

    public BotWarMachine(float x, float y,
                         int type,
                         int teamID,
                         int hitPoint,
                         OnDynamicDamagedEntityMovingListener entityMovingListener,
                         OnEntityDestroyedListener entityDestroyedListener,
                         LightingManager lightingManager,
                         MapInformationProvider mapInformationProvider,
                         AirManager airManager,
                         AssetLoader assetLoader) {
        super(x, y,
                type,
                teamID,
                hitPoint,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader);
        this.canFire = false;
        queueOpenFireOrient = new Array<Terrain.TerrainObserverHolder>();
    }

    @Override
    public void onItemEntered(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {
        if (warMachine.getTeamID() + teamID == 0) {
            queueOpenFireOrient.add(observerHolder);
            if (currentTargetTerrain == null) {
                canFire = true;
                currentTargetTerrain = observerHolder;
                setNextOrient(currentTargetTerrain.orientFromObserver);
            }
        }
    }

    @Override
    public void onItemExited(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {
        queueOpenFireOrient.removeValue(observerHolder, true);
        if (currentTargetTerrain == observerHolder) {
            if (queueOpenFireOrient.size == 0) {
                canFire = false;
                currentTargetTerrain = null;
            } else {
                currentTargetTerrain = queueOpenFireOrient.get(0);
                setNextOrient(currentTargetTerrain.orientFromObserver);
            }
        }
    }

    @Override
    protected boolean checkActingCondition(float delta) {
        return !(WarMachineManager.isEnemiesFrenzy && teamID == ENEMIES_TEAM);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void addMoveAction() {

    }
}
