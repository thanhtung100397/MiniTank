package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.game_object.manager.ExplosionManager;

/**
 * Created by Admin on 12/13/2016.
 */

public class RumbleEffect extends Actor {
    public static final float MAX_CAMERA_TRANSLATE_DISTANCE = 20.0f;
    private float duration;
    private float currentTime = 0;
    private float power;
    private float currentPower = 0;
    private OrthographicCamera camera;
    private float xCameraStart, yCameraStart;
    private float dXCamera, dYCamera;

    public RumbleEffect(OrthographicCamera camera) {
        this.camera = camera;
        this.dXCamera = 0;
        this.dYCamera = 0;
    }

    public void rumble(float power, float duration) {
        this.power = power;
        this.duration = duration;
        this.currentTime = 0;
        this.xCameraStart = camera.position.x;
        this.yCameraStart = camera.position.y;
    }

    @Override
    public void act(float delta) {
        if (currentTime <= duration) {
            currentPower = power * ((duration - currentTime) / duration);

            float x = (MathUtils.random(1.0f) - 0.5f) * 2 * currentPower;
            float y = (MathUtils.random(1.0f) - 0.5f) * 2 * currentPower;

            if (Math.abs(dXCamera - x) > MAX_CAMERA_TRANSLATE_DISTANCE){
                x *= -1;
            }
            if(Math.abs(dYCamera - y) > MAX_CAMERA_TRANSLATE_DISTANCE){
                y *= -1;
            }
            dXCamera -= x;
            dYCamera -= y;
            camera.translate(-x, -y);
            currentTime += delta;
        } else {
            this.camera.position.x = xCameraStart;
            this.camera.position.y = yCameraStart;
            this.dXCamera = 0;
            this.dYCamera = 0;
            remove();
        }
    }
}
