package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 12/12/2016.
 */

public class MachineSmoke {
    public static final float SMOKE_DELAY_TIME = 0.1f;
    public static final int SMOKE_COUNT = 5;

    private AirManager airManager;
    private boolean isStopped = true;
    private WarMachine warMachine;
    private Smoke smokes[];
    private int currentSmokeIndex = 0;
    private Action currentAction;

    public MachineSmoke(WarMachine warMachine, AirManager airManager, AssetLoader assetLoader) {
        this.airManager = airManager;
        this.warMachine = warMachine;
        smokes = new Smoke[SMOKE_COUNT];
        for (int i = 0; i < SMOKE_COUNT; i++) {
            smokes[i] = new Smoke(0, 0, Explosion.SMALL_NORMAL_EXPLOSION, airManager, assetLoader);
        }
    }

    public void start() {
        if (isStopped) {
            isStopped = false;
            currentSmokeIndex = 0;
            currentAction = new CreateSmokeAction();
        }
    }

    public void stop() {
        if (currentAction instanceof CreateSmokeAction) {
            currentAction = new RemoveSmokeAction(((CreateSmokeAction) currentAction).currentTime);
        }
    }

    public void clearSmoke() {
        isStopped = true;
        for (int i = smokes.length - 1; i >= 0; i--) {
            smokes[i].remove();
        }
    }

    public void act(float delta) {
        if (isStopped) {
            return;
        }
        currentAction.act(delta);
    }

    private class CreateSmokeAction extends Action {
        float currentTime = SMOKE_DELAY_TIME;

        @Override
        public boolean act(float delta) {
            if (currentTime < SMOKE_DELAY_TIME) {
                currentTime += delta;
            } else {
                smokes[currentSmokeIndex].setPosition(warMachine.getX() + (GameEntity.ITEM_SIZE - Explosion.SMALL_EXPLOSION_SIZE) / 2,
                        warMachine.getY() + (GameEntity.ITEM_SIZE - Explosion.SMALL_EXPLOSION_SIZE) / 2);
                if (smokes[currentSmokeIndex].getStage() == null) {
                    airManager.addSmoke(smokes[currentSmokeIndex]);
                }

                if (++currentSmokeIndex == SMOKE_COUNT) {
                    currentSmokeIndex = 0;
                }
                currentTime = 0;
            }
            return false;
        }
    }

    private class RemoveSmokeAction extends Action {
        float currentTime;

        RemoveSmokeAction(float currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public boolean act(float delta) {
            if (currentTime < SMOKE_DELAY_TIME) {
                currentTime += delta;
            } else {
                if (smokes[currentSmokeIndex].getStage() == null) {
                    isStopped = true;
                } else {
                    smokes[currentSmokeIndex].remove();
                    if (++currentSmokeIndex == SMOKE_COUNT) {
                        currentSmokeIndex = 0;
                    }
                    currentTime = 0;
                }
            }
            return false;
        }
    }
}