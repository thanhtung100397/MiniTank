package com.tankzor.game.game_object.movable_item.weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.animation.Explosion;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.light.ItemPointLight;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.DynamicDamagedEntity;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 11/13/2016.
 */

public class Bullet extends DynamicDamagedEntity implements Pool.Poolable {
    public static final float BULLET_SIZE = ITEM_SIZE / 2;//3?

    public static final float TIME_TO_PASS_A_TERRAIN_BOX = 0.20f;

    public static final int DOWN_ORIENT = WarMachine.DOWN_ORIENT;
    public static final int LEFT_ORIENT = WarMachine.LEFT_ORIENT;
    public static final int UP_ORIENT = WarMachine.UP_ORIENT;
    public static final int RIGHT_ORIENT = WarMachine.RIGHT_ORIENT;

    public static final Color NORMAL_LIGHT_COLOR = new Color(0.75f, 0.75f, 0.5f, 0.5f);
    public static final Color PLASMA_LIGHT_COLOR = new Color(0.0f, 0.0f, 1.0f, 0.5f);
    public static final Color HEAVY_BULLET_LIGHT_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f);

    public static final int NORMAL_BULLET = 1;//Must start by 1 NOT 0 (-> Explosion)
    public static final int PLASMA_BULLET = 2;
    public static final int DOUBLE_NORMAL_BULLET = 3;
    public static final int DOUBLE_PLASMA_BULLET = 4;
    public static final int HIGH_EXPLOSIVE_BULLET = 5;
    public static final int ARMOR_PIERCING_BULLET = 6;
    public static final int MISSILE_BULLET = 7;

    public static final int LOW_BULLET = 0;//bullet can't pass wall
    public static final int MEDIUM_BULLET = 1;//bullet can pass plant, brick and yellow concrete wall
    public static final int HIGH_BULLET = 2;//bullet can pass plant, brick and yellow/ gray concrete wall

    public static final float LIGHT_RADIUS = 80.0f;

    protected float px, py;
    protected float moveStepDistanceX, moveStepDistanceY;
    private int bulletType;
    private int bulletHeight;//for bullet height type (turret can fire high-bullets)
    private ItemPointLight light;
    private Pool<Bullet> bulletPool;

    public Bullet(OnDynamicDamagedEntityMovingListener entityMovingListener,
                  OnEntityDestroyedListener entityDestroyedListener,
                  LightingManager lightingManager,
                  MapInformationProvider mapInformationProvider,
                  Pool<Bullet> bulletPool) {
        super(0, 0,
                ITEM_SIZE, ITEM_SIZE,
                0,
                0,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider);
        this.bulletPool = bulletPool;
        this.bound.setSize(BULLET_SIZE, BULLET_SIZE);
        addMoveAction();//should be called only one
//        setDebug(true);
    }

    protected void initBullet(float x, float y,
                              WarMachine warMachine,
                              int type,
                              int damage) {
        this.setPosition(x, y);
        this.teamID = warMachine.getTeamID();
        this.currentImageIndex = warMachine.getCurrentOrient();
        this.isPlayerItem = warMachine instanceof PlayerWarMachine;
        this.bulletType = type;
        this.images = warMachine.getAssetLoader().getBulletImages(bulletType, warMachine.getCurrentOrient());
        initSpeedExplosionAndLight(damage);
        calculateMovingDistance(getCurrentOrient());
        ((BulletMovingAction) getActions().get(0)).prepareAction();

        bulletHeight = warMachine.getBulletHeight();
    }

    @Override
    public void reset() {
        this.images = null;
    }

    @Override
    protected void initBody() {

    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        shapes.rect(getX() + (ITEM_SIZE - BULLET_SIZE) / 2, getY() + (ITEM_SIZE - BULLET_SIZE) / 2, BULLET_SIZE, BULLET_SIZE);
    }

    public int getBulletHeight() {
        return bulletHeight;
    }

    @Override
    public void setHitPoint(int hitPoint) {
        super.setHitPoint(hitPoint);
        if (hitPoint == 0) {
            destroy();
        }
    }

    private void initSpeedExplosionAndLight(int damage) {
        setHitPoint(damage);
        Color lightColor = null;
        switch (bulletType) {
            case NORMAL_BULLET: {
                moveTimePerTerrainBox = TIME_TO_PASS_A_TERRAIN_BOX;
                explosionType = Explosion.SMALL_NORMAL_EXPLOSION;
                lightColor = NORMAL_LIGHT_COLOR;
            }
            break;

            case PLASMA_BULLET: {
                moveTimePerTerrainBox = TIME_TO_PASS_A_TERRAIN_BOX / 2.0f;
                explosionType = Explosion.SMALL_PLASMA_EXPLOSION;
                lightColor = PLASMA_LIGHT_COLOR;
            }
            break;

            case HIGH_EXPLOSIVE_BULLET: {
                moveTimePerTerrainBox = TIME_TO_PASS_A_TERRAIN_BOX;
                explosionType = Explosion.BIG_NORMAL_EXPLOSION;
                lightColor = HEAVY_BULLET_LIGHT_COLOR;
            }
            break;

            case ARMOR_PIERCING_BULLET: {
                moveTimePerTerrainBox = TIME_TO_PASS_A_TERRAIN_BOX;
                explosionType = Explosion.SMALL_NORMAL_EXPLOSION;//TODO lack images!!!
                lightColor = HEAVY_BULLET_LIGHT_COLOR;
            }
            break;

            case MISSILE_BULLET: {
                moveTimePerTerrainBox = TIME_TO_PASS_A_TERRAIN_BOX;
                explosionType = Explosion.BIG_NORMAL_EXPLOSION;
            }
            break;

            default: {
                break;
            }
        }

        if (lightingManager.getDayMode() != LightingManager.DAY_MODE) {
            light = lightingManager.createItemPointLight(getX(), getY(),
                    ITEM_SIZE / 2, ITEM_SIZE / 2,
                    LIGHT_RADIUS, lightColor, false);
        }
    }

    private void calculateMovingDistance(int orient) {
        this.moveDistanceX = 0;
        this.moveDistanceY = 0;
        switch (orient) {
            case UP_ORIENT: {
                px = 0;
                py = 0;

                moveDistanceX = 0;
                moveDistanceY = mapInformationProvider.getHeightMap() - getY();

                moveStepDistanceX = 0;
                moveStepDistanceY = BULLET_SIZE;
            }
            break;

            case DOWN_ORIENT: {
                px = BULLET_SIZE;
                py = BULLET_SIZE;

                moveDistanceX = 0;
                moveDistanceY = -getY() - BULLET_SIZE;

                moveStepDistanceX = 0;
                moveStepDistanceY = -BULLET_SIZE;
            }
            break;

            case LEFT_ORIENT: {
                px = BULLET_SIZE;
                py = BULLET_SIZE;

                moveDistanceX = -getX() - BULLET_SIZE;
                moveDistanceY = 0;

                moveStepDistanceX = -BULLET_SIZE;
                moveStepDistanceY = 0;
            }
            break;

            case RIGHT_ORIENT: {
                px = 0;
                py = 0;

                moveDistanceX = mapInformationProvider.getWidthMap() - getX();
                moveDistanceY = 0;

                moveStepDistanceX = BULLET_SIZE;
                moveStepDistanceY = 0;
            }
            break;

            default: {
                break;
            }
        }
    }

    public static boolean canFireOver(int bulletHeight, Wall wall) {
        if (wall == null) {
            return true;
        }
        switch (bulletHeight) {
            case LOW_BULLET: {
                return false;
            }

            case MEDIUM_BULLET: {
                return wall.getType() < Wall.GRAY_CONCRETE_TYPE;
            }

            case HIGH_BULLET: {
                return wall.getType() < Wall.STEEL_TYPE;
            }

            default: {
                return false;
            }
        }
    }

    public static boolean canDestroy(int bulletType, Wall wall) {
        if (wall == null) {
            return true;
        }
        switch (bulletType) {
            case NORMAL_BULLET:
            case DOUBLE_NORMAL_BULLET:
            case PLASMA_BULLET:
            case DOUBLE_PLASMA_BULLET:
            case HIGH_EXPLOSIVE_BULLET: {
                return wall.getType() < Wall.SPIKE_TYPE;
            }

            case ARMOR_PIERCING_BULLET:
            case MISSILE_BULLET: {
                int type = wall.getType();
                return type < Wall.SPIKE_TYPE || type == Wall.YELLOW_CONCRETE_TYPE;
            }

            default: {
                return false;
            }
        }
    }

    @Override
    public void addMoveAction() {
        addAction(new BulletMovingAction());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateLight();
    }

    public ItemPointLight getLight() {
        return light;
    }

    public Color getLightColor() {
        if (light == null) {
            return null;
        }
        return light.getColor();
    }

    protected void updateLight() {
        if (light != null) {
            light.updatePosition(getX(), getY());
        }
    }

    protected void removeLight() {
        if (light != null) {
            light.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(images[0], getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public Rectangle getBound() {
        return bound.setPosition(getX() + (ITEM_SIZE - BULLET_SIZE) / 2, getY() + (ITEM_SIZE - BULLET_SIZE) / 2);
    }

    public float getRealX() {
        return getX() + (ITEM_SIZE - BULLET_SIZE) / 2 + px;
    }

    public float getRealY() {
        return getY() + (ITEM_SIZE - BULLET_SIZE) / 2 + py;
    }

    private class BulletMovingAction extends MoveToAction {

        BulletMovingAction(){

        }

        void prepareAction() {
            restart();
            setPosition(Bullet.this.getX() + moveDistanceX, Bullet.this.getY() + moveDistanceY);
            if (moveDistanceX != 0) {
                setDuration(Math.abs(moveDistanceX / ITEM_SIZE * moveTimePerTerrainBox));
            } else {
                setDuration(Math.abs(moveDistanceY / ITEM_SIZE * moveTimePerTerrainBox));
            }
            previousX = getRealX();
            previousY = getRealY();
        }

        @Override
        public boolean act(float delta) {
            if (super.act(delta)) {
                destroy();
                return false;
            }

            entityMovingListener.onDynamicDamagedEntityExit(Bullet.this, previousX, previousY, previousX + moveStepDistanceX, previousY + moveStepDistanceY);
            previousX = getRealX();
            previousY = getRealY();
            entityMovingListener.onDynamicDamagedEntityEnter(Bullet.this, previousX, previousY, previousX + moveStepDistanceX, previousY + moveStepDistanceY);
            return false;
        }
    }

    @Override
    public void destroy() {
        float x = getRealX();
        float y = getRealY();
        entityMovingListener.onDynamicDamagedEntityExit(this, x, y, x + moveStepDistanceX, y + moveStepDistanceY);
        entityDestroyedListener.onBulletDestroyed(this, null);
        removeLight();
        super.destroy();
        bulletPool.free(this);
    }

    public static class BulletBuilder {
        Bullet arrBullet[];

        public BulletBuilder(WarMachine warMachine, int type, int damage, Pool<Bullet> bulletPool, AssetLoader assetLoader) {
            if (type == DOUBLE_NORMAL_BULLET || type == DOUBLE_PLASMA_BULLET) {
                arrBullet = new Bullet[2];
                float dx = BULLET_SIZE / 3;
                switch (warMachine.getCurrentOrient()) {
                    case WarMachine.UP_ORIENT:
                    case WarMachine.DOWN_ORIENT: {
                        arrBullet[0] = bulletPool.obtain();
                        arrBullet[0].initBullet(warMachine.getX() - dx, warMachine.getY(), warMachine, type / 2, damage / 2);

                        arrBullet[1] = bulletPool.obtain();
                        arrBullet[1].initBullet(warMachine.getX() + dx, warMachine.getY(), warMachine, type / 2, damage / 2);
                    }
                    break;

                    case WarMachine.RIGHT_ORIENT:
                    case WarMachine.LEFT_ORIENT: {

                        arrBullet[0] = bulletPool.obtain();
                        arrBullet[0].initBullet(warMachine.getX(), warMachine.getY() - dx, warMachine, type / 2, damage / 2);

                        arrBullet[1] = bulletPool.obtain();
                        arrBullet[1].initBullet(warMachine.getX(), warMachine.getY() + dx, warMachine, type / 2, damage / 2);
                    }
                    break;

                    default: {
                        break;
                    }
                }
            } else {
                arrBullet = new Bullet[1];
                arrBullet[0] = bulletPool.obtain();
                arrBullet[0].initBullet(warMachine.getX(), warMachine.getY(), warMachine, type, damage);
            }
        }

        public Bullet[] build() {
            return arrBullet;
        }
    }
}
