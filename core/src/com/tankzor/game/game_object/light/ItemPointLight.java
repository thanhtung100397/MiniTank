package com.tankzor.game.game_object.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.tankzor.game.game_object.manager.LightingManager;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Created by Admin on 3/7/2017.
 */

public class ItemPointLight extends PointLight implements Pool.Poolable{
    private Pool<ItemPointLight> pool;
    private float offsetX, offsetY;

    public ItemPointLight(LightingManager lightingManager,
                          int numberOfRay,
                          Color color,
                          float distance,
                          float x, float y) {
        super(lightingManager.getRayHandler(), numberOfRay, color, distance, x, y);
        this.pool = lightingManager.getPointLightPool();
    }

    public void setBodyOffset(float offsetX, float offsetY){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void updatePosition(float xBody, float yBody){
        setPosition(xBody + offsetX, yBody  +offsetY);
    }

    @Override
    public void add(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }

    @Override
    public void remove() {
        super.remove(false);
        pool.free(this);
    }

    @Override
    public void reset() {
        setActive(false);
        setDistance(0);
        setPosition(0, 0);
        attachToBody(null);
        setXray(false);
    }
}
