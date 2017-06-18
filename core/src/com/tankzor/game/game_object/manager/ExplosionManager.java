package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.animation.Explosion;
import com.tankzor.game.game_object.animation.ExplosionArea;
import com.tankzor.game.game_object.animation.ExplosionGroup;
import com.tankzor.game.game_object.animation.OnExplosionFinishListener;
import com.tankzor.game.game_object.animation.RumbleEffect;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.building.Building;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

import java.util.List;

/**
 * Created by Admin on 11/24/2016.
 */

public class ExplosionManager extends Stage implements OnEntityDestroyedListener {
    public static boolean ENABLE_SMOKE = true;
    private TerrainManager terrainManager;
    private AirManager airManager;
    private RumbleEffect rumbleEffect;
    private LightingManager lightingManager;
    private AssetLoader assetLoader;

    public ExplosionManager(Viewport viewport, Batch batch, AssetLoader assetLoader) {
        super(viewport, batch);
        this.assetLoader = assetLoader;
        this.rumbleEffect = new RumbleEffect((OrthographicCamera) viewport.getCamera());
//        getRoot().setCullingArea(((PlayerCamera) viewport.getCamera()).getCullingArea());
    }

    private void createRumbleEffect(float power, float duration) {
//        rumbleEffect.rumble(power, duration);
//        addActor(rumbleEffect);
    }

    public Explosion createExplosion(DamagedEntity entity) {
        if (entity instanceof Bullet) {
            return new Explosion(entity.getX(), entity.getY(), entity.getExplosionType(), airManager, assetLoader, false);
        } else {
            boolean enableSmoke = ENABLE_SMOKE;
            switch (entity.getExplosionType()) {
                case Explosion.BIG_PLASMA_EXPLOSION:
                case Explosion.SMALL_PLASMA_EXPLOSION:
                case Explosion.BIG_RED_EXPLOSION:
                case Explosion.SMALL_RED_EXPLOSION: {
                    enableSmoke = false;
                }
                break;

                default:{
                    break;
                }
            }
            return new Explosion(entity.getX(), entity.getY(),entity.getExplosionType(), airManager, assetLoader, enableSmoke);
        }
    }

    public void setTerrainManager(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public void setAirManager(AirManager airManager) {
        this.airManager = airManager;
    }

    public AirManager getAirManager() {
        return airManager;
    }

    @Override
    public void onBulletDestroyed(Bullet bullet, OnExplosionFinishListener explosionFinishListener) {
        addActor(new ExplosionArea.ExplosionAreaBuilder(lightingManager, bullet.getLightColor())
                .addOnlyOneExplosion(createExplosion(bullet))
                .build(explosionFinishListener));
    }

    @Override
    public void onWallDestroyed(Wall wall, OnExplosionFinishListener explosionFinishListener) {
        addActor(new ExplosionArea.ExplosionAreaBuilder(lightingManager, null)
                .addOnlyOneExplosion(createExplosion(wall))
                .build(explosionFinishListener));
        createRumbleEffect(4.0f, 0.4f);
    }

    @Override
    public void onWarMachineDestroyed(WarMachine warMachine, OnExplosionFinishListener explosionFinishListener) {
        addActor(new ExplosionArea.ExplosionAreaBuilder(lightingManager, null)
                .addOnlyOneExplosion(createExplosion(warMachine))
                .build(explosionFinishListener));
        createRumbleEffect(4.0f, 0.4f);
    }

    @Override
    public void onBuildingDestroyed(Building building, OnExplosionFinishListener explosionFinishListener) {
        Terrain buildingTerrain[] = building.getArea();
        ExplosionGroup explosionGroup = new ExplosionGroup();
        for (int i = 0; i < buildingTerrain.length; i++) {
            explosionGroup.addExplosion(new Explosion(buildingTerrain[i].getX(), buildingTerrain[i].getY(),
                                                        Explosion.BIG_NORMAL_EXPLOSION,
                                                        airManager,
                                                        assetLoader,
                                                        true));
        }
        addActor(new ExplosionArea.ExplosionAreaBuilder(lightingManager, null)
                .addExplosionGroup(explosionGroup)
                .build(explosionFinishListener));
        createRumbleEffect(4.0f, 0.4f);
    }

    @Override
    public void onAreaWeaponDestroyed(AreaWeapon areaWeapon) {
        createExplosionArea(areaWeapon, null);
    }

    public void setLightingManager(LightingManager lightingManager) {
        this.lightingManager = lightingManager;
    }

    private void createExplosionArea(AreaWeapon areaWeapon, Color lightColor) {
        ExplosionArea.ExplosionAreaBuilder explosionAreaBuilder = new ExplosionArea.ExplosionAreaBuilder(lightingManager, lightColor);
        int level = 0;
        int maxLevel = areaWeapon.getLevel();
        float delayTime = 0;
        float additionDelayTime = maxLevel == 3 ? AreaWeapon.BIG_EXPLODE_AREA_DELAY_TIME : 0;

        while (level <= maxLevel) {
            explosionAreaBuilder.addExplosionGroup(createExplosionGroup(areaWeapon, level, delayTime));
            level++;
            delayTime += additionDelayTime;
        }
        addActor(explosionAreaBuilder.build(areaWeapon.getExplosionFinishListener()));
        createRumbleEffect(4.0f, 0.8f);
    }

    private ExplosionGroup createExplosionGroup(AreaWeapon areaWeapon, int level, float delayTime) {
        List<GridPoint2> locations = areaWeapon.getExplosionAreaIndexes(level);
        int type = areaWeapon.getLevelExplosionType(level);
        ExplosionGroup result = new ExplosionGroup(delayTime);
        for (GridPoint2 aPosition : locations) {
            Explosion explosion = new Explosion(aPosition.x * GameEntity.ITEM_SIZE,
                                                aPosition.y * GameEntity.ITEM_SIZE,
                                                type,
                                                airManager,
                                                assetLoader,
                                                ENABLE_SMOKE);
            explosion.enableDamageTerrain(terrainManager.getTerrain(aPosition.x, aPosition.y), areaWeapon);
            result.addExplosion(explosion);
        }
        return result;
    }
}
