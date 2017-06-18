package com.tankzor.game.game_object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.immovable_item.EmptyTerrainListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.bot.BotArtilleryTank;
import com.tankzor.game.game_object.movable_item.war_machine.bot.BotKamikazeTank;
import com.tankzor.game.game_object.movable_item.war_machine.bot.BotTank;
import com.tankzor.game.game_object.movable_item.war_machine.bot.MovableBotWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.support_item.ForceField;

/**
 * Created by Admin on 12/22/2016.
 */

public class SpawnLocation implements EmptyTerrainListener {
    public static final Color ALLIES_LIGHT_COLOR = new Color(0.0f, 0.5f, 0.0f, 0.8f);
    public static final Color ENEMIES_LIGHT_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.8f);

    public float x, y;
    public int teamId;
    private Array<GridPoint2> controlAreas;
    protected Array<WarMachineModel> listWarMachineModels;
    protected Terrain spawnTerrain;
    protected int maxSpawnCount;
    public int currentSpawnCount = 0;
    protected Timer spawnTimer;
    protected Round parent;
    protected float spawnTime = 0;
    protected MovableWarMachine supportWarMachine;
    protected WarMachineManager warMachineManager;

    public SpawnLocation(GridPoint2 location,
                         int teamId,
                         int maxSpawnCount,
                         WarMachineManager warMachineManager,
                         boolean enableLight) {
        this.x = location.x * GameEntity.ITEM_SIZE;
        this.y = location.y * GameEntity.ITEM_SIZE;
        this.maxSpawnCount = maxSpawnCount;
        this.warMachineManager = warMachineManager;
        this.spawnTerrain = warMachineManager.getTerrainManager().getTerrain(location.x, location.y);
        listWarMachineModels = new Array<WarMachineModel>();
        spawnTimer = new Timer();
        spawnTime = 0;
        this.teamId = teamId;
        if(!enableLight){
            return;
        }
        switch (teamId){
            case DamagedEntity.ALLIES_TEAM:{
                warMachineManager.getLightingManager().createItemPointLight(this.x, this.y, GameEntity.ITEM_SIZE / 2, GameEntity.ITEM_SIZE / 2, 5 * GameEntity.ITEM_SIZE, ALLIES_LIGHT_COLOR, true);
            }
            break;

            case DamagedEntity.ENEMIES_TEAM:{
                warMachineManager.getLightingManager().createItemPointLight(this.x, this.y, GameEntity.ITEM_SIZE / 2, GameEntity.ITEM_SIZE / 2, 5 * GameEntity.ITEM_SIZE, ENEMIES_LIGHT_COLOR, true);
            }
            break;

            default:{
                break;
            }
        }
    }

    public int size() {
        return listWarMachineModels.size;
    }

    public void setParent(Round parent) {
        this.parent = parent;
    }

    public void setSupportWarMachine(MovableWarMachine supportWarMachine) {
        this.supportWarMachine = supportWarMachine;
    }

    public void setControlAreas(Array<GridPoint2> controlAreas) {
        this.controlAreas = controlAreas;
    }

    public void setListWarMachineModels(Array<WarMachineModel> listWarMachineModels){
        this.listWarMachineModels = listWarMachineModels;
    }

    public void spawnWarMachine() {
        if(listWarMachineModels.size > 0){
            if(currentSpawnCount < maxSpawnCount){
                spawnTerrain.registerEmptyTerrainListener(this);
            }
        }else if(currentSpawnCount == 0){
            parent.removeSpawnLocation(this);
        }
    }

    private Array<MovableBotWarMachine> listWaitingWarMachines = new Array<MovableBotWarMachine>();
    private EmptyTerrainListener emptyTerrainListener = new EmptyTerrainListener() {
        @Override
        public void onTerrainIsEmpty() {
            warMachineManager.addMovableMachine(listWaitingWarMachines.removeIndex(0));
        }
    };

    public void spawnWarMachine(MovableBotWarMachine movableBotWarMachine) {
        listWaitingWarMachines.add(movableBotWarMachine);
        if(spawnTerrain.getWarMachineOn() instanceof MovableBotWarMachine){
            MovableBotWarMachine botWarMachine = (MovableBotWarMachine) spawnTerrain.getWarMachineOn();
            botWarMachine.getOutOfThisWay(-1);
            return;
        }
        spawnTerrain.registerEmptyTerrainListener(emptyTerrainListener);
    }

    public int getCurrentWarMachineCount() {
        int count = 0;
        for (WarMachineModel model : listWarMachineModels) {
            count += model.count;
        }
        return count;
    }

    @Override
    public void onTerrainIsEmpty() {
        if(listWarMachineModels.size == 0){
            return;
        }
        SpawnLocation.WarMachineModel currentModel = listWarMachineModels.get(0);
        MovableBotWarMachine result = createMovableWarMachine(currentModel);
        if (result != null) {
            ForceField forceField = result.getForceField();
            forceField.addMaximumNumber(currentModel.maxForceField);
            forceField.setBaseRecoverTime(currentModel.forceFieldRecoverTime);

            result.setBaseWeaponModel(currentModel.bulletModel);

            result.setSupportTarget(supportWarMachine);

            warMachineManager.addMovableMachine(result);
            if (--currentModel.count == 0) {
                listWarMachineModels.removeIndex(0);
            }

            if (++currentSpawnCount < maxSpawnCount) {
                spawnTerrain.registerEmptyTerrainListener(this);
            }
        }
    }

    protected MovableBotWarMachine createMovableWarMachine(WarMachineModel warMachineModel){
        MovableBotWarMachine result = null;
        TerrainManager terrainManager = warMachineManager.getTerrainManager();
        switch (warMachineModel.id) {
            case WarMachine.LIGHT_TANK_TYPE:
            case WarMachine.HEAVY_TANK_TYPE: {
                result = new BotTank(this,
                        warMachineModel.id,
                        teamId,
                        warMachineModel.hitPoint,
                        warMachineModel.speed,
                        terrainManager,
                        warMachineManager.getExplosionManager(),
                        terrainManager,
                        warMachineManager.getAirManager(),
                        warMachineManager.getLightingManager(),
                        warMachineManager.getAssetLoader(),
                        warMachineManager.getTracksManager(),
                        controlAreas,
                        warMachineModel.range,
                        terrainManager,
                        warMachineManager.getBulletManager());
                result.setValue(warMachineModel.money);
            }
            break;

            case WarMachine.KAMIKAZE_TANK_TYPE: {
                result = new BotKamikazeTank(this,
                        teamId,
                        warMachineModel.hitPoint,
                        warMachineModel.speed,
                        warMachineManager.getTerrainManager(),
                        warMachineManager.getExplosionManager(),
                        warMachineManager.getLightingManager(),
                        terrainManager,
                        warMachineManager.getAirManager(),
                        warMachineManager.getAssetLoader(),
                        controlAreas,
                        warMachineModel.range,
                        terrainManager);
                result.setValue(warMachineModel.money);
            }
            break;

            case WarMachine.ARTILLERY_TANK_TYPE: {
                result = new BotArtilleryTank(this,
                        teamId,
                        warMachineModel.hitPoint,
                        warMachineModel.speed,
                        warMachineManager.getTerrainManager(),
                        warMachineManager.getExplosionManager(),
                        terrainManager,
                        warMachineManager.getAirManager(),
                        warMachineManager.getLightingManager(),
                        warMachineManager.getAssetLoader(),
                        warMachineManager.getTracksManager(),
                        controlAreas,
                        warMachineModel.range,
                        terrainManager,
                        warMachineManager.getBulletManager());
                ((BotArtilleryTank) result).setArtilleryModel(warMachineModel.artilleryModel,
                        warMachineModel.artilleryRange,
                        warMachineModel.artillerySpeed);
                result.setValue(warMachineModel.money);
            }
            break;

            default: {
                break;
            }
        }
        return result;
    }

    public static class WarMachineModel {
        public int id;
        public float speed;
        public int hitPoint;
        public int count;
        public int range;
        public int money;
        public int maxForceField;
        public float forceFieldRecoverTime;
        public WeaponModel bulletModel;
        public WeaponModel artilleryModel;
        public int artilleryRange;
        public float artillerySpeed;

        public void setArtilleryModel(WeaponModel artilleryModel, int range, float speed) {
            this.artilleryModel = artilleryModel;
            this.artilleryRange = range;
            this.artillerySpeed = speed;
        }
    }
}
