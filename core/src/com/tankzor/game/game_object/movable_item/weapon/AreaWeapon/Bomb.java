package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 12/4/2016.
 */

public class Bomb extends AreaWeapon {

    public Bomb(WarMachine warMachine, float x, float y, int damage, int level, boolean isMassive) {
        super(warMachine, damage, level);
        setMassive(isMassive);
        setX(x);
        setY(y);
    }

    public Bomb(float x, float y,
                int damage,
                int level,
                OnEntityDestroyedListener entityDestroyedListener,
                MapInformationProvider mapInformationProvider,
                LightingManager lightingManager,
                boolean isMassive) {
        super(x, y, damage, level, entityDestroyedListener, mapInformationProvider, lightingManager);
        setMassive(isMassive);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

    }

    @Override
    protected void createAction() {

    }

    @Override
    public int getDamageByLevel(int level) {
        return hitPoint - level;
    }
}
