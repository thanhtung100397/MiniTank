package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.tankzor.game.game_object.light.ItemPointLight;
import com.tankzor.game.game_object.manager.LightingManager;

/**
 * Created by Admin on 2/19/2017.
 */

public class ExplosionArea extends Group implements OnExplosionStartListener, OnExplosionFinishListener {
    private LightingManager lightingManager;
    private Color lightColor;
    private float finishTime;

    private ExplosionArea(LightingManager lightingManager, Color lightColor) {
        this.lightingManager = lightingManager;
        this.lightColor = lightColor;
    }

    @Override
    public void onExplosionStart() {
        Explosion firstExplosion = ((ExplosionGroup) getChildren().get(0)).getExplosion(0);
        if(lightingManager.getDayMode() != LightingManager.DAY_MODE) {
            ItemPointLight light = lightingManager.createItemPointLight(firstExplosion.getX(), firstExplosion.getY(),
                    firstExplosion.getWidth() / 2, firstExplosion.getWidth() / 2,
                    (getChildren().size + 1.5f) * firstExplosion.getWidth(),
                    lightColor,
                    true);
            LightingManager.DelayAction delayAction = new LightingManager.DelayAction(finishTime);
            LightingManager.LightFadeAction lightFadeAction = new LightingManager.LightFadeAction(light, 1.5f){
                @Override
                protected void onActionComplete(ItemPointLight itemPointLight) {
                    itemPointLight.remove();
                }
            };
            lightingManager.addAction(new LightingManager.SequenceAction(delayAction, lightFadeAction));
        }
    }

    private void setFinishTime(float finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public void onExplosionFinish() {
        remove();
    }

    public static class ExplosionAreaBuilder {
        ExplosionArea explosionArea;

        public ExplosionAreaBuilder(LightingManager lightingManager, Color lightColor) {
            explosionArea = new ExplosionArea(lightingManager, lightColor);
        }

        public ExplosionAreaBuilder addExplosionGroup(ExplosionGroup explosionGroup) {
            if(explosionGroup.getChildren().size > 0){
                explosionArea.addActor(explosionGroup);
            }
            return this;
        }

        public ExplosionAreaBuilder addOnlyOneExplosion(Explosion explosion) {
            ExplosionGroup explosionGroup = new ExplosionGroup();
            explosionGroup.addExplosion(explosion);
            explosionArea.addActor(explosionGroup);
            return this;
        }

        public ExplosionArea build(OnExplosionFinishListener explosionFinishListener) {
            SnapshotArray<Actor> listExplosions = explosionArea.getChildren();
            if(listExplosions.size > 0) {
                ExplosionGroup firstExplosionGroup = ((ExplosionGroup) listExplosions.get(0));

                firstExplosionGroup.registerExplosionStartListener(explosionArea);

                ExplosionGroup lastExplosionGroup = ((ExplosionGroup) listExplosions.get(listExplosions.size - 1));
                explosionArea.setFinishTime(lastExplosionGroup.getDelayTime() + Explosion.DURATION);
                lastExplosionGroup.registerExplosionFinishListener(explosionArea);
                if (explosionFinishListener != null) {
                    lastExplosionGroup.registerExplosionFinishListener(explosionFinishListener);
                }
            }
            return explosionArea;
        }
    }
}
