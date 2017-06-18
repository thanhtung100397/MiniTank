package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.manager.AirManager;

/**
 * Created by Admin on 12/10/2016.
 */

public class Smoke extends Actor {
    private float size;
    private AirManager airManager;
    private float widthMap, heightMap;
    private TextureRegion image;

    public Smoke(float x, float y, int type, AirManager airManager, AssetLoader assetLoader) {
        setX(x);
        setY(y);
        if (type == Explosion.SMALL_NORMAL_EXPLOSION) {
            this.image = assetLoader.getRandomSmallSmoke();
            this.size = Explosion.SMALL_EXPLOSION_SIZE;
        } else {
            this.image = assetLoader.getRandomBigSmoke();
            this.size = Explosion.BIG_EXPLOSION_SIZE;
        }
        this.airManager = airManager;
        this.widthMap = airManager.getTerrainManager().getWidthMap();
        this.heightMap = airManager.getTerrainManager().getHeightMap();
        addAction(new SmokeMovingAction());
    }

    public Smoke(float x, float y, int type, float duration, AirManager airManager, AssetLoader assetLoader) {
        setX(x);
        setY(y);
        if (type == Explosion.SMALL_NORMAL_EXPLOSION) {
            this.image = assetLoader.getRandomSmallSmoke();
            this.size = Explosion.SMALL_EXPLOSION_SIZE;
        } else {
            this.image = assetLoader.getRandomBigSmoke();
            this.size = Explosion.BIG_EXPLOSION_SIZE;
        }
        this.airManager = airManager;
        this.widthMap = airManager.getTerrainManager().getWidthMap();
        this.heightMap = airManager.getTerrainManager().getHeightMap();
        addAction(new SmokeMovingAction());
        addAction(new SmokeTimer(duration));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image, getX(), getY(), size, size);
    }

    private class SmokeMovingAction extends Action {

        @Override
        public boolean act(float delta) {
            float x = Smoke.this.getX() + airManager.getWindDx();
            float y = Smoke.this.getY() + airManager.getWindDy();
            Smoke.this.setX(x);
            Smoke.this.setY(y);
            if (x + size <= 0 || x >= widthMap || y + size <= 0 || y >= heightMap) {
                remove();
                return true;
            }
            return false;
        }
    }

    private class SmokeTimer extends Action {
        float currentTime;
        float duration;

        public SmokeTimer(float duration) {
            this.duration = duration - 1.0f;
            this.currentTime = 0;
        }

        @Override
        public boolean act(float delta) {
            if (currentTime >= duration) {
                clearActions();
                remove();
                return true;
            }
            currentTime += delta;
            return false;
        }
    }
}
