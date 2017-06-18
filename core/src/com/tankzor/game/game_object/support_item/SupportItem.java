package com.tankzor.game.game_object.support_item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.animation.BaseAnimation;
import com.tankzor.game.game_object.light.ItemPointLight;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;

/**
 * Created by Admin on 12/12/2016.
 */

public abstract class SupportItem extends BaseAnimation {
    public static final float BLINK_TIME = 1.0f;

    public static final int ALLY_TANK = 16;
    public static final int ALLY_KAMIKAZE_TANK = 17;
    public static final int ALLY_ARTILLERY_TANK = 18;

    public static final int FORCE_FIELD = 19;
    public static final int TEMPORARY_ARMOR = 23;
    public static final int PERMANENT_ARMOR = 24;

    public static final int REPAIR_KIT = 20;
    public static final int BOOST_SPEED = 21;
    public static final int TIME_FREEZE = 22;

    public static final int RADAR = 25;
    public static final int THERMOVISION = 26;
    public static final int UPGRADE_TANK = 27;
    public static final int REPAIR_TANK = 28;
    public static final int REPAIR_ALLY_TANK = 29;

    public static final int LIFE_ITEM = 30;
    public static final int MONEY_ITEM = 31;
    public static final int STAR_ITEM = 32;
    public static final int BOX_ITEM = 33;
    public static final int TRAP_BOX_ITEM = 34;

    protected ItemPointLight light;
    private BlinkAction blinkAction;
    private DurationTimer durationTimer;

    public SupportItem(float x, float y, LightingManager lightingManager) {
        setPosition(x, y);
        float size = GameEntity.ITEM_SIZE / 1.2f;
        setSize(size, size);
        if (lightingManager != null) {
            light = lightingManager.createItemPointLight(x, y, GameEntity.ITEM_SIZE / 2, GameEntity.ITEM_SIZE / 2, 250, null, true);
        }
    }

    public void setBlinkAnimation(boolean enable){
        if(enable){
            this.blinkAction = new BlinkAction();
            addAction(blinkAction);
        }else {
            removeAction(blinkAction);
            this.blinkAction = null;
        }
    }

    @Override
    protected void init(TextureRegion[] images, float duration) {
        this.images = images;
        setDuration(duration);
    }

    protected void activeDurationTimer(DurationTimer durationTimer){
        setVisible(false);
        setBlinkAnimation(false);
        this.durationTimer = durationTimer;
        addAction(this.durationTimer);
    }

    public DurationTimer getActiveDurationTimer() {
        return durationTimer;
    }

    public void setLightColor(Color color) {
        if (light != null) {
            light.setColor(color);
        }
    }

    public void addEffectTo(MovableWarMachine item) {
        GameSounds.getInstance().playSFX(GameSounds.PICK_ITEM_SFX_ID);
    }

    @Override
    public boolean remove() {
        if (light != null) {
            light.remove();
            light = null;
        }
        return super.remove();
    }

    protected class DurationTimer extends Action {
        private int currentDuration;
        private float secondCounter = 0;
        private SupportItemDurationListener durationListener;

        public DurationTimer(int currentDuration, SupportItemDurationListener durationListener) {
            this.currentDuration = currentDuration;
            this.durationListener = durationListener;
            this.durationListener.onActive(currentDuration);
        }

        @Override
        public boolean act(float delta) {
            secondCounter += delta;
            if (secondCounter >= 1.0f) {//1 s pass
                currentDuration -= 1;
                durationListener.onDurationChange(currentDuration);
                if (currentDuration == -1) {
                    durationListener.onTimeOut();
                    onTimeOut();
                    remove();
                    return true;
                }
                secondCounter -= 1.0f;
            }
            return false;
        }

        protected void onTimeOut() {

        }

        public void setCurrentDuration(int currentDuration){
            this.currentDuration = currentDuration;
            durationListener.onDurationChange(currentDuration);
        }
    }

    protected class BlinkAction extends AnimationTimer {
        int orient = 1;

        @Override
        public boolean act(float delta) {
            if (timeElapse >= frameTime) {
                if (currentImageIndex == images.length - 1) {
                    orient = -1;
                }else if(currentImageIndex == 0){
                    orient = 1;
                }
                timeElapse = 0;
                currentImageIndex += orient;
            }
            timeElapse += delta;
            return false;
        }
    }
}