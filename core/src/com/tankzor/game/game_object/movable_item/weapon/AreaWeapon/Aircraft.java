package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;

/**
 * Created by Admin on 12/4/2016.
 */

public class Aircraft extends AreaWeapon {
    public static final float AIRCRAFT_SIZE = GameEntity.ITEM_SIZE * 3.5f;
    public static final float TIME_TO_PASS_A_TERRAIN_BOX = 0.75f;

    private int bombCapacity;
    private float xDropBomb;

    public Aircraft(float x, float y,
                    int damage,
                    int level,
                    int bombCapacity,
                    OnEntityDestroyedListener entityDestroyedListener,
                    MapInformationProvider mapInformationProvider,
                    LightingManager lightingManager,
                    AssetLoader assetLoader) {
        super(x, y, damage, level, entityDestroyedListener, mapInformationProvider, lightingManager);
        images = assetLoader.getAircraftImages();
        this.bombCapacity = bombCapacity;
        calculateLocation(x, y);
        this.xDropBomb = x - (bombCapacity / 2) * ITEM_SIZE;
        createAction();
    }

    public void calculateLocation(float x, float y) {
        if (x > y) {
            setX(x - y - AIRCRAFT_SIZE);
            setY(-AIRCRAFT_SIZE);
        } else if (y > x) {
            setY(y - x - AIRCRAFT_SIZE);
            setX(-AIRCRAFT_SIZE);
        } else {
            setX(-AIRCRAFT_SIZE);
            setY(-AIRCRAFT_SIZE);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(images[1], getX() + 10, getY(), AIRCRAFT_SIZE, AIRCRAFT_SIZE);
        batch.draw(images[0], getX(), getY(), AIRCRAFT_SIZE, AIRCRAFT_SIZE);
    }

    public void createAction() {
        addAction(new AircraftMovingAction());
    }

    private void dropBomb() {
        float xBomb = getX() + AIRCRAFT_SIZE / 2;
        float yBomb = getY() + AIRCRAFT_SIZE / 2;
        Bomb bomb = new Bomb(xBomb, yBomb,
                getHitPoint(),
                level,
                entityDestroyedListener,
                mapInformationProvider,
                lightingManager,
                isMassive());
        bomb.setDamageLevel(Wall.YELLOW_CONCRETE_TYPE);
        entityDestroyedListener.onAreaWeaponDestroyed(bomb);
        bombCapacity--;
    }

    private class AircraftMovingAction extends MoveToAction {
        private long soundID;

        public AircraftMovingAction() {
            float xStart = Aircraft.this.getX();
            float yStart = Aircraft.this.getY();
            float widthMap = mapInformationProvider.getWidthMap();
            float heightMap = mapInformationProvider.getHeightMap();
            float xEnd;
            float yEnd;
            if (xStart == -AIRCRAFT_SIZE && yStart == -AIRCRAFT_SIZE) {
                xEnd = heightMap;
                yEnd = heightMap;
            } else if (xStart == -AIRCRAFT_SIZE) {
                xEnd = heightMap - yStart - AIRCRAFT_SIZE;
                yEnd = heightMap;
            } else {//yStart < -AIRCRAFT_SIZE
                yEnd = widthMap - xStart - AIRCRAFT_SIZE;
                xEnd = widthMap;
            }
            setPosition(xEnd, yEnd);
            float duration = (xEnd - xStart) / ITEM_SIZE * TIME_TO_PASS_A_TERRAIN_BOX;
            setDuration(duration);
        }

        @Override
        protected void begin() {
            super.begin();
            soundID = GameSounds.getInstance().playBomberFlySFX();
        }

        @Override
        public boolean act(float delta) {
            if (super.act(delta)) {
                destroy();
                return true;
            }
            if (bombCapacity > 0 && xDropBomb - Aircraft.this.getX() <= 0) {
                dropBomb();
                xDropBomb += DamagedEntity.ITEM_SIZE;
            }
            return false;
        }

        @Override
        protected void end() {
            super.end();
            GameSounds.getInstance().stopBomberFlySFX(soundID);
        }
    }

    @Override
    public int getDamageByLevel(int level) {
        return 0;
    }

    @Override
    public void destroy() {
        remove();
    }
}
