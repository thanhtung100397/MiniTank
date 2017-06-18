package com.tankzor.game.game_object.support_item;

import com.badlogic.gdx.graphics.Color;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;

/**
 * Created by Admin on 12/12/2016.
 */

public class BoostSpeedItem extends SupportItem {
    public static final int DURATION = 30;
    public static final Color LIGHT_COLOR = new Color(0.75f, 0.75f, 0.5f, 0.6f);

    private MovableWarMachine movableMachine;
    private SupportItemDurationListener durationListener;

    public BoostSpeedItem(float x, float y,
                          SupportItemDurationListener durationListener,
                          LightingManager lightingManager,
                          AssetLoader assetLoader) {
        super(x, y, lightingManager);
        init(assetLoader.getSupportItemImages(BOOST_SPEED), BLINK_TIME);
        this.durationListener = durationListener;
        setLightColor(LIGHT_COLOR);
        setBlinkAnimation(true);
    }

    public BoostSpeedItem(SupportItemDurationListener durationListener){
        super(0,0, null);
        this.durationListener = durationListener;
    }

    private void activeTimer() {
        super.activeDurationTimer(new DurationTimer(DURATION, durationListener){
            @Override
            protected void onTimeOut() {
                movableMachine.boostSpeed(MovableWarMachine.SLOW_DOWN);
                ((TerrainManager) getStage()).setActiveBoostSpeedItem(null);
            }
        });
    }

    @Override
    public void addEffectTo(MovableWarMachine item) {
        super.addEffectTo(item);
        TerrainManager terrainManager = (TerrainManager) getStage();
        BoostSpeedItem actingItem = terrainManager.getActiveBoostSpeedItem();
        if(actingItem == null) {
            this.movableMachine = item;
            item.boostSpeed(MovableWarMachine.HIGH_SPEED);
            activeTimer();
            terrainManager.setActiveBoostSpeedItem(this);
            if(light != null){
                light.remove();
                light = null;
            }
        }else {
            DurationTimer durationTimer = actingItem.getActiveDurationTimer();
            if(durationTimer != null){
                durationTimer.setCurrentDuration(DURATION);
            }
            remove();
        }
    }
}
