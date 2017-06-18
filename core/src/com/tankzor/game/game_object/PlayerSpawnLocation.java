package com.tankzor.game.game_object;

import com.badlogic.gdx.math.GridPoint2;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.gamehud.GameHUD;
import com.tankzor.game.network.Network;
import com.tankzor.game.ui.EndGameWidget;
import com.tankzor.game.ui.PlayerCamera;

/**
 * Created by Admin on 12/24/2016.
 */

public class PlayerSpawnLocation extends SpawnLocation {
    private PlayerWarMachine playerWarMachine;
    private GameHUD gameHUD;

    public PlayerSpawnLocation(GridPoint2 location,
                               WarMachineManager warMachineManager,
                               WeaponManager weaponManager) {
        super(location,
                DamagedEntity.ALLIES_TEAM,
                1,
                warMachineManager,
                true);
        TerrainManager terrainManager = warMachineManager.getTerrainManager();
        playerWarMachine = new PlayerWarMachine(this,
                                                terrainManager,
                                                warMachineManager.getExplosionManager(),
                                                warMachineManager.getLightingManager(),
                                                terrainManager,
                                                warMachineManager.getAirManager(),
                                                warMachineManager.getAssetLoader(),
                                                warMachineManager.getBulletManager(),
                                                warMachineManager.getTracksManager(),
                                                weaponManager);
        gameHUD = warMachineManager.getGameHUD();
        gameHUD.showPlayerHeal(playerWarMachine);
        playerWarMachine.setCamera((PlayerCamera) warMachineManager.getCamera());
        warMachineManager.setPlayerMachine(playerWarMachine);
    }

    @Override
    public void spawnWarMachine() {
        if (TerrainManager.additionRequirement == TerrainManager.ONE_LIFE || PlayerProfile.getInstance().getLife() == 0) {
            gameHUD.showEndGameWidget(EndGameWidget.LOSE_ID);
        }else {
            spawnTerrain.registerEmptyTerrainListener(this);
        }
    }

    @Override
    public void onTerrainIsEmpty() {
        if(playerWarMachine.getStage() == null){
            warMachineManager.addActor(playerWarMachine);
        }else {
            playerWarMachine.reborn();
        }
    }

    public PlayerWarMachine getPlayerWarMachine() {
        return playerWarMachine;
    }
}
