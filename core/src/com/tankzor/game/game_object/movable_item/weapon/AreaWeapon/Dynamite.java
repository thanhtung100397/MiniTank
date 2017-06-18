package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 12/2/2016.
 */

public class Dynamite extends AreaWeapon {
    public static final float DELAY_TIME = 4.0f;
    private float delayTime;

    public Dynamite(WarMachine warMachine, int damage, int level) {
        super(warMachine, damage, level);
        images = warMachine.getAssetLoader().getDynamiteImage();
        this.delayTime = DELAY_TIME;
        setDamageLevel(Wall.YELLOW_CONCRETE_TYPE);
        createAction();
    }

    public Dynamite(float x, float y,
                    int damage,
                    int level,
                    float delay,
                    OnEntityDestroyedListener entityDestroyedListener,
                    MapInformationProvider mapInformationProvider,
                    LightingManager lightingManager,
                    AssetLoader assetLoader){
        super(x, y, damage, level, entityDestroyedListener, mapInformationProvider, lightingManager);
        this.delayTime = delay;
        images = assetLoader.getDynamiteImage();
        createAction();
    }

    @Override
    protected void createAction() {
        addAction(new DynamiteAction(delayTime));
    }

    private class DynamiteAction extends DelayAction{
        private long soundID;

        private DynamiteAction(float delayTime){
            setDuration(delayTime);
            soundID = GameSounds.getInstance().playBipSFX();
        }

        @Override
        protected boolean delegate(float delta) {
            if(super.delegate(delta)){
                GameSounds.getInstance().stopBipSFX(soundID);
                destroy();
                return true;
            }
            return false;
        }
    }

    @Override
    public int getDamageByLevel(int level) {
        return hitPoint - level;
    }
}
