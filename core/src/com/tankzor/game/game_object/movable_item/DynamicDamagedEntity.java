package com.tankzor.game.game_object.movable_item;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;

/**
 * Created by Admin on 11/18/2016.
 */

public abstract class DynamicDamagedEntity extends DamagedEntity {
    protected float previousX, previousY;
    protected float moveDistanceX, moveDistanceY;
    protected float moveTimePerTerrainBox;
    protected OnDynamicDamagedEntityMovingListener entityMovingListener;
    protected MapInformationProvider mapInformationProvider;

    public DynamicDamagedEntity(float x, float y,
                                float width, float height,
                                int teamId,
                                int hitPoint,
                                OnDynamicDamagedEntityMovingListener entityMovingListener,
                                OnEntityDestroyedListener entityDestroyedListener,
                                LightingManager lightingManager,
                                MapInformationProvider mapInformationProvider) {
        super(x, y, width, height, teamId, hitPoint, entityDestroyedListener, lightingManager);
        this.previousX = x;
        this.previousY = y;
        this.entityMovingListener = entityMovingListener;
        this.mapInformationProvider = mapInformationProvider;
    }

    @Override
    protected void initBody() {
        float halfWidth = ITEM_SIZE / 2;
        float halfHeight = ITEM_SIZE / 2;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(getX() + halfWidth, getY() + halfHeight);
        body = lightingManager.getLightingWorld().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape objectShape = new PolygonShape();
        objectShape.setAsBox(halfWidth, halfHeight);
        fixtureDef.shape = objectShape;

        body.createFixture(fixtureDef);
    }

    public int getCurrentOrient() {
        return currentImageIndex;
    }

    public abstract void addMoveAction();

    public OnDynamicDamagedEntityMovingListener getEntityMovingListener() {
        return entityMovingListener;
    }

    public MapInformationProvider getMapInformationProvider() {
        return mapInformationProvider;
    }
}
