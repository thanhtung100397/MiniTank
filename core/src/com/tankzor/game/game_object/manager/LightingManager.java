package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.light.ItemPointLight;
import com.tankzor.game.ui.PlayerCamera;

import box2dLight.RayHandler;

/**
 * Created by Admin on 2/13/2017.
 */

public class LightingManager {
    public static final int DAY_MODE = 0;
    public static final int LATE_NIGHT_MODE = 1;
    public static final int RAY_NUMBER = 30;

    private PlayerCamera playerCamera;
    private World lightingWorld;
    private RayHandler rayHandler;
    private Pool<ItemPointLight> pointLightPool;
    private Array<Action> listActions;
    private int dayMode;

    public LightingManager(PlayerCamera playerCamera) {
        this.playerCamera = playerCamera;
        RayHandler.useDiffuseLight(false);
        this.pointLightPool = new Pool<ItemPointLight>(16, 16) {
            @Override
            protected ItemPointLight newObject() {
                return new ItemPointLight(LightingManager.this, RAY_NUMBER, null, 0, 0, 0);
            }
        };
        this.listActions = new Array<Action>(5);
    }

    public void initWorld() {
        lightingWorld = new World(new Vector2(0, 0), false);
        if (rayHandler == null) {
            rayHandler = new RayHandler(lightingWorld);
        } else {
            rayHandler.setWorld(lightingWorld);
        }
    }

    public ItemPointLight createItemPointLight(float xBottomRight, float yBottomRight,
                                               float offsetX, float offsetY,
                                               float radius,
                                               Color color,
                                               boolean enableXRay) {
        ItemPointLight result = pointLightPool.obtain();
        result.add(rayHandler);
        result.setBodyOffset(offsetX, offsetY);
        result.updatePosition(xBottomRight, yBottomRight);
        result.setDistance(radius);
        result.setColor(color);
        result.setXray(enableXRay);
        result.setActive(true);
        return result;
    }

    public ItemPointLight createItemPointLightAttachToObject(float radius,
                                                             Color color,
                                                             boolean enableXRay,
                                                             DamagedEntity attachedItem) {
        ItemPointLight result = pointLightPool.obtain();
        result.add(rayHandler);
        result.setDistance(radius);
        result.setColor(color);
        result.setXray(enableXRay);
        result.setActive(true);
        result.attachToBody(attachedItem.getBody());
        return result;
    }

    public void setDayMode(int dayMode) {
        this.dayMode = dayMode;
        switch (dayMode) {
            case DAY_MODE: {
                rayHandler.setAmbientLight(1);
            }
            break;

            case LATE_NIGHT_MODE: {
                rayHandler.setAmbientLight(0.1f);
            }
            break;

            default: {
                break;
            }
        }
    }

    public void addAction(Action action) {
        listActions.add(action);
    }

    public void act(float delta) {
        for (int i = listActions.size - 1; i >= 0; i--) {
            if (listActions.get(i).act(delta)) {
                listActions.removeIndex(i);
            }
        }
    }

    public void draw() {
        rayHandler.setCombinedMatrix(playerCamera);
        rayHandler.updateAndRender();
    }

    public void dispose() {
        if(lightingWorld != null) {
            lightingWorld.dispose();
        }
        if(rayHandler != null) {
            rayHandler.dispose();
        }
    }

    public void clear() {
        rayHandler.removeAll();
        pointLightPool.clear();
        listActions.clear();
    }

    public World getLightingWorld() {
        return lightingWorld;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public int getDayMode() {
        return dayMode;
    }

    public Pool<ItemPointLight> getPointLightPool() {
        return pointLightPool;
    }

    public static class SequenceAction extends Action {
        private Action[] sequenceActions;
        private int currentActionIndex;
        private Action currentAction;

        public SequenceAction(Action... actions) {
            this.sequenceActions = actions;
            this.currentActionIndex = 0;
            this.currentAction = sequenceActions[currentActionIndex];
        }

        @Override
        public boolean act(float delta) {
            if(currentAction.act(delta)){
                sequenceActions[currentActionIndex] = null;
                if(currentActionIndex == sequenceActions.length - 1){
                    return true;
                }
                currentAction = sequenceActions[++currentActionIndex];
            }
            return false;
        }
    }

    public static class DelayAction extends Action {
        private float duration;
        private float timeElapse = 0;

        public DelayAction(float duration) {
            this.duration = duration;
        }

        @Override
        public boolean act(float delta) {
            if (timeElapse > duration) {
                return true;
            }
            timeElapse += delta;
            return false;
        }
    }

    public static class LightFadeAction extends Action {
        private float duration;
        private float timeElapse;
        private ItemPointLight light;
        private Color lightColor;

        public LightFadeAction(ItemPointLight itemPointLight, float duration) {
            this.duration = duration;
            this.timeElapse = 0;
            this.light = itemPointLight;
            this.lightColor = itemPointLight.getColor();
        }

        @Override
        public boolean act(float delta) {
            if (timeElapse > duration) {
                onActionComplete(light);
                return true;
            }
            timeElapse += delta;
            lightColor.a *= (1 - timeElapse / duration);
            light.setColor(lightColor);
            return false;
        }

        protected void onActionComplete(ItemPointLight itemPointLight) {

        }
    }
}
