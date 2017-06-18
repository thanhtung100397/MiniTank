package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.manager.AirManager;

/**
 * Created by Admin on 12/11/2016.
 */

public class Cloud extends Actor {
    public static final float CLOUD_WIDTH = GameEntity.ITEM_SIZE * 8.0f;
    public static final float CLOUD_HEIGHT = CLOUD_WIDTH / 1.5f;

    private AirManager airManager;
    private float widthMap, heightMap;
    private TextureRegion[] images;

    public Cloud(float x, float y, AirManager airManager, AssetLoader assetLoader) {
        setX(x);
        setY(y);
        this.images = assetLoader.getRandomCloudImages();
        this.airManager = airManager;
        this.widthMap = airManager.getTerrainManager().getWidthMap();
        this.heightMap = airManager.getTerrainManager().getHeightMap();
        addAction(new Cloud.CloudMovingAction());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getX();
        float y = getY();
        batch.draw(images[1], x + 100, y - 100, CLOUD_WIDTH, CLOUD_HEIGHT);
        batch.draw(images[0], x, y, CLOUD_WIDTH, CLOUD_HEIGHT);
    }

    private class CloudMovingAction extends Action {

        @Override
        public boolean act(float delta) {
            float x = getX() + airManager.getWindDx();
            float y = getY() + airManager.getWindDy();
            setX(x);
            setY(y);
            if (x + CLOUD_WIDTH <= 0 || x >= widthMap || y + CLOUD_HEIGHT <= 0 || y >= heightMap) {
                remove();
                return true;
            }
            return false;
        }
    }
}
