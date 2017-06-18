package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.animation.Explosion;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 11/29/2016.
 */

public class Mine extends AreaWeapon {

    public Mine(WarMachine warMachine,
                int type,
                int damage) {
        super(warMachine, damage, 0);
        initMine(type, warMachine.getTeamID(), warMachine.getAssetLoader());
    }

    public Mine(float x, float y,
                int teamID,
                int type,
                int damage,
                OnEntityDestroyedListener entityDestroyedListener,
                MapInformationProvider mapInformationProvider,
                AssetLoader assetLoader) {
        super(x, y, damage, 0, entityDestroyedListener, mapInformationProvider, null);
        initMine(type, teamID, assetLoader);
    }

    private void initMine(int type, int teamID, AssetLoader assetLoader) {
        this.teamID = teamID;
        images = assetLoader.getMineImage(type, teamID);
        mapInformationProvider.getTerrain((int) (getX() / ITEM_SIZE), (int) (getY() / ITEM_SIZE)).putMineOn(this);
        if (teamID == ENEMIES_TEAM) {
            setVisible(false);
        }
    }

    @Override
    protected void createAction() {

    }

    @Override
    public int getDamageByLevel(int level) {
        return hitPoint;
    }

    @Override
    public int getLevelExplosionType(int level) {
        return Explosion.BIG_NORMAL_EXPLOSION;
    }
}
