package com.tankzor.game.game_object;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.tankzor.game.game_object.animation.Explosion;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.util.QuadRectangle;

/**
 * Created by Admin on 5/22/2017.
 */

public class DamagedEntity extends GameEntity {
    public static final int ALLIES_TEAM = 1;
    public static final int ENEMIES_TEAM = -1;

    protected int maxHitPoint;
    protected int hitPoint;
    protected int teamID;
    protected int explosionType;
    protected OnEntityDestroyedListener entityDestroyedListener;
    protected boolean isPlayerItem;
    protected Rectangle bound;
    protected QuadRectangle quadBound;
    protected Body body;
    protected LightingManager lightingManager;

    public DamagedEntity(float x, float y,
                         float width, float height,
                         int teamID,
                         int hitPoint,
                         OnEntityDestroyedListener entityDestroyedListener,
                         LightingManager lightingManager) {
        super(x, y, width, height);
        this.teamID = teamID;

        this.maxHitPoint = hitPoint;
        this.hitPoint = maxHitPoint;

        this.explosionType = Explosion.BIG_NORMAL_EXPLOSION;

        this.bound = new Rectangle(x, y, width, height);
        this.quadBound = new QuadRectangle(x, y, width, height);

        this.entityDestroyedListener = entityDestroyedListener;

        if(lightingManager == null){
            return;
        }
        this.lightingManager = lightingManager;

        initBody();
    }

    protected void initBody() {
        float halfWidth = getWidth() / 2;
        float halfHeight = getHeight() / 2;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(getX() + halfWidth, getY() + halfHeight);
        body = lightingManager.getLightingWorld().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape objectShape = new PolygonShape();
        objectShape.setAsBox(halfWidth, halfHeight);
        fixtureDef.shape = objectShape;

        body.createFixture(fixtureDef);
    }

    public int takeDamage(int damage, boolean isPlayer) {
        if (hitPoint > damage) {
            setHitPoint(hitPoint - damage);
            return 0;
        }
        int resultDamage = damage - hitPoint;
        setHitPoint(0);
        return resultDamage;
    }

    private void updateBodyPosition(){
        if(body != null) {
            body.setTransform(getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updateBodyPosition();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        updateBodyPosition();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        updateBodyPosition();
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        updateBodyPosition();
    }

    public Body getBody() {
        return body;
    }

    public void setExplosionType(int explosionType) {
        this.explosionType = explosionType;
    }

    public int getExplosionType() {
        return explosionType;
    }

    public Rectangle getBound() {
        return bound.setPosition(getX(), getY());
    }

    public QuadRectangle getQuadBound() {
        quadBound.x = getX();
        quadBound.y = getY();
        return quadBound;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public int getMaxHitPoint() {
        return maxHitPoint;
    }

    public boolean isCollisionWith(DamagedEntity damagedEntity) {
        return getBound().overlaps(damagedEntity.getBound());
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public int getTeamID() {
        return teamID;
    }

    public boolean isPlayerItem() {
        return isPlayerItem;
    }

    public LightingManager getLightingManager() {
        return lightingManager;
    }

    public OnEntityDestroyedListener getEntityDestroyedListener() {
        return entityDestroyedListener;
    }

    public void destroy() {
        if(lightingManager != null && body != null) {
            lightingManager.getLightingWorld().destroyBody(body);
        }
        remove();
    }
}
