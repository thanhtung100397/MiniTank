package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 11/24/2016.
 */

public class Explosion extends BaseAnimation {
    public static final int SMALL_NORMAL_EXPLOSION = Bullet.NORMAL_BULLET;
    public static final int SMALL_PLASMA_EXPLOSION = Bullet.PLASMA_BULLET;
    public static final int SMALL_RED_EXPLOSION = Bullet.ARMOR_PIERCING_BULLET;

    public static final int BIG_NORMAL_EXPLOSION = -SMALL_NORMAL_EXPLOSION;
    public static final int BIG_PLASMA_EXPLOSION = -SMALL_PLASMA_EXPLOSION;
    public static final int BIG_RED_EXPLOSION = -SMALL_RED_EXPLOSION;

    public static final float SMALL_EXPLOSION_SIZE = GameEntity.ITEM_SIZE / 1.6f;
    public static final float BIG_EXPLOSION_SIZE = GameEntity.ITEM_SIZE * 1.67f;

    public static final float DURATION = 0.8f;
    public static final float CREATE_SMOKE_DURATION = DURATION * 0.5f;

    private int type;
    private AirManager airManager;
    private AssetLoader assetLoader;

    public Explosion(float x, float y,
                     int type,
                     AirManager airManager,
                     AssetLoader assetLoader,
                     boolean hasSmoke) {
        init(assetLoader.getExplosionImages(type), DURATION);
        this.assetLoader = assetLoader;
        this.type = type;
        this.airManager = airManager;
        float size = findSize(type);
        setSize(size, size);
        setPosition(x + (GameEntity.ITEM_SIZE - size) / 2, y + (GameEntity.ITEM_SIZE - size) / 2);
        if (hasSmoke) {
            addAction(new CreateSmokeTimer(CREATE_SMOKE_DURATION));
        }
    }

    public void enableDamageTerrain(Terrain damageTerrain, AreaWeapon weaponCause) {
        addAction(new DamageAction(damageTerrain, weaponCause));
    }

    public static float findSize(int type) {
        switch (type) {
            case SMALL_NORMAL_EXPLOSION:
            case SMALL_PLASMA_EXPLOSION: {
                return SMALL_EXPLOSION_SIZE;
            }

            case BIG_NORMAL_EXPLOSION:
            case BIG_PLASMA_EXPLOSION:
            case BIG_RED_EXPLOSION: {
                return BIG_EXPLOSION_SIZE;
            }

            default: {
                return 0;
            }
        }
    }

    public class DamageAction extends Action {
        private Terrain damageTerrain;
        private AreaWeapon weaponCause;

        public DamageAction(Terrain damageTerrain, AreaWeapon weaponCause) {
            this.damageTerrain = damageTerrain;
            this.weaponCause = weaponCause;
        }

        @Override
        public boolean act(float delta) {
            if (damageTerrain != null) {
                if(weaponCause.isMassive()){
                    damageTerrain.removeAllObjectOn();
                }else {
                    damageTerrain.takeDamage(weaponCause);
                }
            }
            return true;
        }
    }

    private class CreateSmokeTimer extends Action {
        private float delayTime;
        private float timeElapse = 0;

        CreateSmokeTimer(float delayTime) {
            this.delayTime = delayTime;
        }

        @Override
        public boolean act(float delta) {
            if (timeElapse > delayTime) {
                airManager.addSmoke(new Smoke(getX(), getY(), type, airManager, assetLoader));
                return true;
            }
            timeElapse += delta;
            return false;
        }
    }
}
