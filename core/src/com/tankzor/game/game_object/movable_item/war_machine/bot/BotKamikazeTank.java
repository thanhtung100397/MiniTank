package com.tankzor.game.game_object.movable_item.war_machine.bot;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.RoundSet;
import com.tankzor.game.game_object.SpawnLocation;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.PathFindingProvider;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Bomb;

/**
 * Created by Admin on 12/13/2016.
 */

public class BotKamikazeTank extends MovableBotWarMachine {
    public static final float DISTANCE_ACTIVE_BOMB = 1.5f * ITEM_SIZE;
    public BotKamikazeTank(SpawnLocation spawnLocation,
                           int teamID,
                           int hitPoint,
                           float speed,
                           OnDynamicDamagedEntityMovingListener entityMovingListener,
                           OnEntityDestroyedListener entityDestroyedListener,
                           LightingManager lightingManager,
                           MapInformationProvider mapInformationProvider,
                           AirManager airManager,
                           AssetLoader assetLoader,
                           Array<GridPoint2> controlAreas,
                           int rangeSize,
                           PathFindingProvider pathFindingProvider) {
        super(spawnLocation,
                WarMachine.KAMIKAZE_TANK_TYPE,
                teamID,
                hitPoint,
                speed,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader,
                null,
                controlAreas,
                rangeSize,
                pathFindingProvider);
    }

    @Override
    protected void setUpMapFindingSystems(PathFindingProvider pathFindingProvider) {
        pathFindingMap = pathFindingProvider.getNavigationGrid();
        pathFinder = pathFindingProvider.getPathFinder();
    }

    private void activeBombAndDestroy(){
        entityDestroyedListener.onAreaWeaponDestroyed(new Bomb(this, getX(), getY(), 10, 3, false));
    }

    private boolean canActiveBomb(){
        if(currentAttackTarget != null){
            float dXToTarget = Math.abs(currentAttackTarget.getX() - getX());
            float dYToTarget = Math.abs(currentAttackTarget.getY() - getY());
            return dXToTarget <= DISTANCE_ACTIVE_BOMB && dYToTarget <= DISTANCE_ACTIVE_BOMB;
        }
        return false;
    }

    @Override
    protected void hasNoMovingActionLeft() {
        if(canActiveBomb()) {
            activeBombAndDestroy();
        }else {
            setupDefaultMoving();
        }
    }
}
