package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.animation.Explosion;
import com.tankzor.game.game_object.animation.Smoke;
import com.tankzor.game.game_object.light.ItemPointLight;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 1/27/2017.
 */

public class Missile extends Bullet {
    public static final float CREATE_SMOKE_DELAY_TIME = 0.15f;

    private int level;
    private AirManager airManager;
    private float smokeTime = 0;
    private float dXSmoke;
    private float dYSmoke;
    private AssetLoader assetLoader;
    private float headDx, headDy;

    public Missile(WarMachine warMachine,
                   int damage,
                   int level) {
        super(warMachine.getEntityMovingListener(),
                warMachine.getEntityDestroyedListener(),
                warMachine.getLightingManager(),
                warMachine.getMapInformationProvider(),
                null);
        initBullet(warMachine.getX(), warMachine.getY(), warMachine, Bullet.MISSILE_BULLET, damage);

        this.level = level;
        this.airManager = warMachine.getAirManager();
        this.assetLoader = warMachine.getAssetLoader();

        ItemPointLight light = getLight();
        switch (warMachine.getCurrentOrient()) {
            case UP_ORIENT: {
                dXSmoke = (ITEM_SIZE - Explosion.SMALL_EXPLOSION_SIZE) / 2;
                dYSmoke = 0;
                headDx = 0;
                headDy = BULLET_SIZE;
                if(light != null){
                    light.setBodyOffset(ITEM_SIZE / 2, 0);
                }
            }
            break;

            case DOWN_ORIENT: {
                dXSmoke = (ITEM_SIZE - Explosion.SMALL_EXPLOSION_SIZE) / 2;
                dYSmoke = ITEM_SIZE;
                headDx = 0;
                headDy = -BULLET_SIZE;
                if(light != null) {
                    light.setBodyOffset(ITEM_SIZE / 2, ITEM_SIZE);
                }
            }
            break;

            case LEFT_ORIENT: {
                dXSmoke = ITEM_SIZE;
                dYSmoke = (ITEM_SIZE - Explosion.SMALL_EXPLOSION_SIZE) / 2;
                headDx = -BULLET_SIZE;
                headDy = 0;
                if(light != null) {
                    light.setBodyOffset(ITEM_SIZE, ITEM_SIZE / 2);
                }
            }
            break;

            case RIGHT_ORIENT: {
                dXSmoke = 0;
                dYSmoke = (ITEM_SIZE - Explosion.SMALL_EXPLOSION_SIZE) / 2;
                headDx = BULLET_SIZE;
                headDy = 0;
                if(light != null) {
                    light.setBodyOffset(0, ITEM_SIZE / 2);
                }
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void destroy() {
        float x = getRealX();
        float y = getRealY();
        entityMovingListener.onDynamicDamagedEntityExit(this, x, y, x + moveStepDistanceX, y + moveStepDistanceY);
        entityDestroyedListener.onAreaWeaponDestroyed(new Bomb(x + headDx, y + headDy,
                                                                hitPoint,
                                                                level,
                                                                entityDestroyedListener,
                                                                mapInformationProvider,
                                                                lightingManager,
                                                                false));
        removeLight();
        remove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (smokeTime >= CREATE_SMOKE_DELAY_TIME) {
            createSmoke();
            smokeTime = 0;
        } else {
            smokeTime += delta;
        }
    }

    public void createSmoke() {
        airManager.addSmoke(new Smoke(getX() + dXSmoke, getY() + dYSmoke, Explosion.SMALL_NORMAL_EXPLOSION, 4.0f, airManager, assetLoader));
    }

    public int getLevel() {
        return level;
    }

//    private class MissileVerticalMovingAction extends Action {
//        float v;
//        float time;
//        float sPrevious;
//        float sLimit;
//        float a = 217;
//        float xMissile, yMissile;
//        float orient;
//
//        public MissileVerticalMovingAction(int orient) {
//            v = 0;
//            time = 0;
//            sPrevious = 0;
//            sLimit = 3 * GameEntity.ITEM_SIZE;
//            xMissile = Missile.this.getX();
//            yMissile = Missile.this.getY();
//            if(orient == UP_ORIENT){
//                orient = 1;
//            }else {
//                orient = -1;
//            }
//        }
//
//        @Override
//        public boolean act(float delta) {
//            if (sPrevious >= sLimit) {
//                a = 0;
//            }
//
//            yMissile += orient * (v * delta + a * delta * time + 0.5f * a * delta * delta);
//            Missile.this.setPosition(xMissile, yMissile);
//            time += delta;
//            return false;
//        }
//    }
}
