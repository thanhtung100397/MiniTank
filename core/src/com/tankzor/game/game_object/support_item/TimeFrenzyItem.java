package com.tankzor.game.game_object.support_item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 12/12/2016.
 */

public class TimeFrenzyItem extends SupportItem {
    public static final int DURATION = 30;
    public static final Color LIGHT_COLOR = new Color(0.75f, 0.75f, 0.5f, 0.6f);
    private SupportItemDurationListener durationListener;

    public TimeFrenzyItem(float x, float y,
                          SupportItemDurationListener durationListener,
                          LightingManager lightingManager,
                          AssetLoader assetLoader) {
        super(x, y, lightingManager);
        init(assetLoader.getSupportItemImages(TIME_FREEZE), BLINK_TIME);
        this.durationListener = durationListener;
        setLightColor(LIGHT_COLOR);
        setBlinkAnimation(true);
    }

    public TimeFrenzyItem(SupportItemDurationListener durationListener){
        super(0,0, null);
        this.durationListener = durationListener;
    }

    private void activeTimer() {
        super.activeDurationTimer(new DurationTimer(DURATION, durationListener){
            @Override
            protected void onTimeOut() {
                WarMachineManager.isEnemiesFrenzy = false;
                ((TerrainManager) getStage()).setActiveTimeFrenzyItem(null);
            }
        });
    }

    @Override
    public void addEffectTo(MovableWarMachine item) {
        super.addEffectTo(item);
        if (item instanceof PlayerWarMachine) {
            TerrainManager terrainManager = (TerrainManager) getStage();
            TimeFrenzyItem actingItem = ((TerrainManager) getStage()).getActiveTimeFrenzyItem();
            if (actingItem == null) {
                WarMachineManager.isEnemiesFrenzy = true;
                activeTimer();
                terrainManager.setActiveTimeFrenzyItem(this);
                if(light != null){
                    light.remove();
                    light = null;
                }
            } else {
                DurationTimer durationTimer = actingItem.getActiveDurationTimer();
                if(durationTimer != null){
                    durationTimer.setCurrentDuration(DURATION);
                }
                remove();
            }
        }
    }
}
