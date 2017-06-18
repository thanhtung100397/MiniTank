package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.animation.Cloud;
import com.tankzor.game.game_object.animation.Smoke;
import com.tankzor.game.game_object.immovable_item.PalmTree;

/**
 * Created by Admin on 12/4/2016.
 */

public class AirManager {
    public static boolean ENABLE_EFFECT = true;
    public static final float WIND_SPEED_MIN = 0.8f;
    public static final float TIME_TO_CHANGE_WIND_DIRECTION = 10.0f;
    public static final float TIME_TO_CREATE_CLOUD = 3.0f;

    private TerrainManager terrainManager;

    private Stage cloudManager;
    private Stage smokeManager;
    private Stage palmManager;

    private int maxNumberOfCloud = 3;
    private float currentCloudTime = 0;

    private float windDx = WIND_SPEED_MIN;
    private float windDy = WIND_SPEED_MIN;
    private float currentWindTime = 0;

    private float currentTime = 0;
    private AssetLoader assetLoader;

    public AirManager(Viewport viewport, Batch batch, AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        cloudManager = new Stage(viewport, batch);
        smokeManager = new Stage(viewport, batch);
        palmManager = new Stage(viewport, batch);
//        Rectangle cullingArea = ((PlayerCamera) viewport.getCamera()).getCullingArea();
//        cloudManager.getRoot().setCullingArea(cullingArea);
//        smokeManager.getRoot().setCullingArea(cullingArea);
//        palmManager.getRoot().setCullingArea(cullingArea);
    }

    public void setTerrainManager(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public void act(float delta) {
        cloudManager.act(delta);
        if(!ENABLE_EFFECT){
            return;
        }
        smokeManager.act(delta);
        currentTime += Gdx.graphics.getDeltaTime();
        if (currentTime - currentWindTime >= TIME_TO_CHANGE_WIND_DIRECTION) {
            changeWindDirection();
            currentWindTime = currentTime;
        }

        if (currentTime - currentCloudTime >= TIME_TO_CREATE_CLOUD) {
            if (cloudManager.getActors().size < maxNumberOfCloud) {
                createCloud();
            }
            currentCloudTime = currentTime;
        }
    }

    public void draw(){
        smokeManager.draw();
        palmManager.draw();
        cloudManager.draw();
    }

    public void dispose(){
        smokeManager.dispose();
        palmManager.dispose();
        cloudManager.dispose();
    }

    public void clear(){
        cloudManager.clear();
        palmManager.clear();
        smokeManager.clear();
    }

    public void removeAllCloudAndSmoke(){
        smokeManager.clear();
        cloudManager.clear();
    }

    public void changeWindDirection() {
        int sx = (MathUtils.random(0, 1) == 0) ? 1 : -1;
        int sy = (MathUtils.random(0, 1)  == 0) ? 1 : -1;
        int dx = (MathUtils.random(0, 1)  == 0) ? 1 : -1;
        int dy = (MathUtils.random(0, 1)  == 0) ? 1 : -1;
        float newWinDx = windDx + sx * 0.05f;
        float newWinDy = windDy + sy * 0.05f;
        if (newWinDx > WIND_SPEED_MIN) {
            windDx = dx * newWinDx;
        }

        if (newWinDy > WIND_SPEED_MIN) {
            windDy = dy * newWinDy;
        }
    }

    public void createCloud() {
        if(!ENABLE_EFFECT){
            return;
        }
        float xCloud;
        float yCloud;

        if (MathUtils.random(0, 1)  == 0) {
            if (windDx > 0) {
                xCloud = -Cloud.CLOUD_WIDTH;
                yCloud = MathUtils.random(terrainManager.getRowNumber() - 1) * GameEntity.ITEM_SIZE;
            } else {
                xCloud = 0;
                yCloud = MathUtils.random(terrainManager.getRowNumber() - 1) * GameEntity.ITEM_SIZE;
            }
        } else {
            xCloud = MathUtils.random(terrainManager.getColumnNumber() - 1) * GameEntity.ITEM_SIZE;
            if (windDy > 0) {//START FROM BOTTOM SIDE OF GAME MAP
                yCloud = -Cloud.CLOUD_WIDTH;
            } else {//START FROM TOP SIDE OF GAME MAP
                yCloud = 0;
            }
        }

        cloudManager.addActor(new Cloud(xCloud, yCloud, this, assetLoader));
    }

    public void createPalmTree(int i, int j){
        palmManager.addActor(new PalmTree(i, j, assetLoader));
    }

    public float getWindDx() {
        return windDx;
    }

    public float getWindDy() {
        return windDy;
    }

    public TerrainManager getTerrainManager() {
        return terrainManager;
    }

    public void addFlyingItem(GameEntity gameEntity) {
        cloudManager.addActor(gameEntity);
    }

    public void addSmoke(Smoke smoke){
        if(ENABLE_EFFECT) {
            smokeManager.addActor(smoke);
        }
    }

    public Stage getSmokeManager() {
        return smokeManager;
    }
}
