package com.tankzor.game.game_object.decorations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;

/**
 * Created by Admin on 4/25/2017.
 */

public class Ring {
    public static final int PLAYER_RING = 0;
    public static final int ENEMY_RING = 1;
    public static final int ALLY_RING = 2;

    public static final float RING_SIZE = 1.2f * GameEntity.ITEM_SIZE;
    public static final float OFFSET = (RING_SIZE - GameEntity.ITEM_SIZE) / 2;

    private TextureRegion[] images;
    private int currentImageIndex = 0;
    private boolean isVisible;
    private Array<Action> listActions;
    private boolean isChangeRingImageActionActivated;
    private RingFlickerListener ringFlickerListener;

    public Ring(int type, AssetLoader assetLoader) {
        images = assetLoader.getRingsImage(type);
        listActions = new Array<Action>(2);
        isVisible = true;
    }

    public void setRingFlickerListener(RingFlickerListener ringFlickerListener) {
        this.ringFlickerListener = ringFlickerListener;
    }

    public void setNormalMode(){
        this.currentImageIndex = 0;
    }

    public void setForceFieldMode(){
        this.currentImageIndex = 1;
    }

    public void flicker(float duration) {
        listActions.add(new FlickerAction(duration));
    }

    public void changeRingImage(){
        if(isChangeRingImageActionActivated){
            return;
        }
        listActions.add(new ChangeRingImageAction());
    }

    public void act(float delta) {
        for (int i = 0; i < listActions.size; i++) {
            listActions.get(i).act(delta);
        }
    }

    public void draw(Batch batch, float xTank, float yTank) {
        if (isVisible) {
            batch.draw(images[currentImageIndex], xTank - OFFSET, yTank - OFFSET, RING_SIZE, RING_SIZE);
        }
    }

    private void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    private class FlickerAction extends Action {
        private final float FLICKER_TIME = 0.2f;
        private float duration;
        private float timeElapsed;

        public FlickerAction(float duration) {
            isChangeRingImageActionActivated = true;
            this.duration = duration;
            this.timeElapsed = 0;
            if(ringFlickerListener != null){
                ringFlickerListener.onStartFlicker();
            }
        }

        @Override
        public boolean act(float delta) {
            if (timeElapsed > duration) {
                setVisible(true);
                if(ringFlickerListener != null){
                    ringFlickerListener.onFinishFlicker();
                }
                return true;
            }
            timeElapsed += delta;
            setVisible(((int) (timeElapsed / FLICKER_TIME)) % 2 == 1);
            return false;
        }
    }

    private class ChangeRingImageAction extends Action {
        private final float DURATION = 0.4f;
        private float timeElapsed = 0;

        @Override
        public boolean act(float delta) {
            if(timeElapsed > DURATION){
                currentImageIndex = 1;
                isChangeRingImageActionActivated = false;
                return true;
            }
            timeElapsed += delta;
            currentImageIndex = 2;
            return false;
        }
    }
}
