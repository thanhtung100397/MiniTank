package com.tankzor.game.game_object.movable_item.war_machine.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.SpawnLocation;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.light.ItemPointLight;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.util.QuadRectangle;

/**
 * Created by Admin on 1/5/2017.
 */

public abstract class Building extends WarMachine {
    public static final int RADAR_BUILDING = 0;
    public static final int NUCLEAR_BUILDING = 1;

    protected Terrain area[];
    protected TextureRegion image;
    private ItemPointLight light;

    public Building(float x, float y,
                    int teamID,
                    int hitPoint,
                    OnDynamicDamagedEntityMovingListener entityMovingListener,
                    OnEntityDestroyedListener entityDestroyedListener,
                    LightingManager lightingManager,
                    MapInformationProvider mapInformationProvider,
                    AirManager airManager,
                    AssetLoader assetLoader) {
        super(x, y,
                2 * ITEM_SIZE, 2 * ITEM_SIZE,
                teamID,
                hitPoint,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader);

        healBar.setScale(2.0f);
        healBar.translate(0, GameEntity.ITEM_SIZE);

        Color lightColor;
        if(teamID == ALLIES_TEAM) {
            lightColor = SpawnLocation.ALLIES_LIGHT_COLOR;
        }else {
            lightColor = SpawnLocation.ENEMIES_LIGHT_COLOR;
        }
        this.light = lightingManager.createItemPointLight(x, y,
                                                        GameEntity.ITEM_SIZE, GameEntity.ITEM_SIZE,
                                                        6 * GameEntity.ITEM_SIZE,
                                                        lightColor,
                                                        true);
    }

    @Override
    protected void createMachineSmoke() {

    }

    @Override
    protected void onAttachToGameWorld() {
        int i = (int) (getX() / ITEM_SIZE);
        int j = (int) (getY() / ITEM_SIZE);
        this.area = new Terrain[4];
        area[0] = mapInformationProvider.getTerrain(i, j);
        area[0].registerIncomingWarMachineEnter(this);
        area[0].addItemOn(this);
        area[1] = mapInformationProvider.getTerrain(i + 1, j);
        area[1].registerIncomingWarMachineEnter(this);
        area[1].addItemOn(this);
        area[2] = mapInformationProvider.getTerrain(i, j + 1);
        area[2].registerIncomingWarMachineEnter(this);
        area[2].addItemOn(this);
        area[3] = mapInformationProvider.getTerrain(i + 1, j + 1);
        area[3].registerIncomingWarMachineEnter(this);
        area[3].addItemOn(this);
    }

    @Override
    protected void initBody() {
        float halfWidth = ITEM_SIZE;
        float halfHeight = ITEM_SIZE;
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

    @Override
    public Rectangle getBound() {
        return bound;
    }

    public QuadRectangle getQuadBound(){
        return quadBound;
    }

    @Override
    public void addMoveAction() {

    }

    @Override
    public void fire() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image, getX(), getY(), getWidth(), getHeight());
        healBar.draw(batch);
    }

    public Terrain[] getArea() {
        return area;
    }

    @Override
    public void destroy() {
        for (Terrain terrain : area){
            terrain.removeItemOn(this);
        }
        entityDestroyedListener.onBuildingDestroyed(this, null);
        notifyWarMachineDestroyedToAllListener();
        stopMachineSmoke();
        healPaneTable.remove();
        forceField.getForceFieldPaneTable().remove();
        if(light != null){
            light.remove();
        }
        if(lightingManager != null && body != null) {
            lightingManager.getLightingWorld().destroyBody(body);
        }
        remove();
    }

    @Override
    public int takeDamage(int damage, boolean byPlayer) {
        return super.takeDamage(damage, byPlayer);
    }
}
