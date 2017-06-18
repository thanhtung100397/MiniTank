package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.GameSounds;

/**
 * Created by Admin on 2/15/2017.
 */

public class ExplosionGroup extends Group {
    private Array<OnExplosionStartListener> explosionStartListeners;
    private Array<OnExplosionFinishListener> explosionFinishListeners;
    private Action currentAction;
    private float delayTime = 0;

    public ExplosionGroup(float delayTime) {
        this.delayTime = delayTime;
        currentAction = new ValidateAction();
        explosionStartListeners = new Array<OnExplosionStartListener>(1);
        explosionFinishListeners = new Array<OnExplosionFinishListener>(1);
    }

    public ExplosionGroup(){
        currentAction = new ValidateAction();
        explosionStartListeners = new Array<OnExplosionStartListener>(1);
        explosionFinishListeners = new Array<OnExplosionFinishListener>(1);
    }

    public void registerExplosionStartListener(OnExplosionStartListener explosionStartListener) {
        this.explosionStartListeners.add(explosionStartListener);
    }

    public void notifyExplosionStartToAllListener() {
        for (int i = 0; i < explosionStartListeners.size; i++) {
            explosionStartListeners.get(i).onExplosionStart();
        }
    }

    public void registerExplosionFinishListener(OnExplosionFinishListener explosionFinishListener) {
        this.explosionFinishListeners.add(explosionFinishListener);
    }

    public void notifyExplosionFinishToAllListener() {
        for (int i = 0; i < explosionFinishListeners.size; i++) {
            explosionFinishListeners.get(i).onExplosionFinish();
        }
    }

    public void addExplosion(Explosion explosion){
        addActor(explosion);
    }

    public Explosion getExplosion(int index) {
        return (Explosion) getChildren().get(index);
    }

    public float getDelayTime() {
        return delayTime;
    }

    @Override
    public void act(float delta) {
        currentAction.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            super.draw(batch, parentAlpha);
        }
    }

    private class ValidateAction extends Action {

        @Override
        public boolean act(float delta) {
            if(getChildren().size > 0){
                if(delayTime > 0){
                    currentAction = new DelayExplosionAction(delayTime);
                }else {
                    currentAction = new ExplosionAction();
                }
            }
            return true;
        }
    }

    private class DelayExplosionAction extends Action {
        float duration;
        float time = 0;

        DelayExplosionAction(float duration) {
            this.duration = duration;
            setVisible(false);
        }

        @Override
        public boolean act(float delta) {
            if (time < duration) {
                time += delta;
                return false;
            }
            if (!isVisible()) {
                setVisible(true);
                currentAction = new ExplosionAction();
            }
            return true;
        }
    }

    private class ExplosionAction extends Action {
        boolean isStarted = false;

        @Override
        public boolean act(float delta) {
            if (!isStarted) {
                GameSounds.getInstance().playSFX(GameSounds.EXPLOSION_SFX_ID);
                notifyExplosionStartToAllListener();
                isStarted = true;
            }

            Actor[] actors = getChildren().begin();
            for (int i = 0, n = getChildren().size; i < n; i++) {
                actors[i].act(delta);
            }
            getChildren().end();

            if (getChildren().size == 0) {
                notifyExplosionFinishToAllListener();
                remove();
                return true;
            }
            return false;
        }
    }
}
