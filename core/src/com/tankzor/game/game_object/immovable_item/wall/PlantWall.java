package com.tankzor.game.game_object.immovable_item.wall;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 1/4/2017.
 */

public class PlantWall extends Wall {
    public static final int BULLET_HEIGHT_PASS = Bullet.MEDIUM_BULLET;

    public PlantWall(int id,
                     Terrain parentTerrain,
                     WallStateListener wallStateListener,
                     OnEntityDestroyedListener itemDestroyedListener,
                     LightingManager lightingManager) {
        super(id, 1, parentTerrain, wallStateListener, itemDestroyedListener, lightingManager);
        setType(PLANT_TYPE);
    }

    @Override
    protected void initBody() {
//        float halfWidth = ITEM_SIZE / 2;
//        float halfHeight = ITEM_SIZE / 2;
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.StaticBody;
//        bodyDef.position.set(getX() + halfWidth, getY() + halfHeight);
//        body = lightingManager.getLightingWorld().createBody(bodyDef);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        CircleShape objectShape = new CircleShape();
//        objectShape.setRadius(0.35f * ITEM_SIZE);
//        fixtureDef.shape = objectShape;
//
//        body.createFixture(fixtureDef);
    }

    @Override
    public int takeDamage(Bullet bullet) {
        if(bullet.getBulletHeight() >= BULLET_HEIGHT_PASS){
            return bullet.getHitPoint();
        }
        int damage = bullet.getHitPoint();
        if (hitPoint > damage) {
            setHitPoint(hitPoint - damage);
            bullet.setHitPoint(0);
            return 0;
        }
        int resultDamage = damage - hitPoint;
        setHitPoint(0);
        bullet.setHitPoint(resultDamage);
        return resultDamage;
    }
}
