package com.tankzor.game.game_object.movable_item.war_machine.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Bomb;

/**
 * Created by Admin on 1/6/2017.
 */

public class NuclearBuilding extends Building {
    public static final int EXPLOSION_DAMAGE = 21;
    public static final int EXPLOSION_NUMBER = 4;
    public static final float EXPLOSION_DELAY_TIME = 1.0f;

    public NuclearBuilding(float x, float y,
                           int teamId,
                           int hitPoint,
                           OnDynamicDamagedEntityMovingListener entityMovingListener,
                           OnEntityDestroyedListener entityDestroyedListener,
                           LightingManager lightingManager,
                           MapInformationProvider mapInformationProvider,
                           AirManager airManager,
                           AssetLoader assetLoader) {
        super(x, y,
                teamId,
                hitPoint,
                entityMovingListener,
                entityDestroyedListener,
                lightingManager,
                mapInformationProvider,
                airManager,
                assetLoader);
        image = assetLoader.getNuclearBuildingImage();
    }

    @Override
    public void destroy() {
        setVisible(false);
        addAction(new ExplosionAction());
        for (Terrain terrain : area) {
            terrain.removeItemOn(this);
        }
    }

    private class ExplosionAction extends Action {
        private float currentExplosionTime = 0;
        private int currentExplosionNumber = 0;

        @Override
        public boolean act(float delta) {
            if (currentExplosionTime >= EXPLOSION_DELAY_TIME) {
                if (createExplosion()) {
                    remove();
                    return true;
                }
                Gdx.app.log("ABC","EXP");
                currentExplosionTime = 0;
            }
            currentExplosionTime += delta;
            return false;
        }

        private boolean createExplosion() {
            float xBomb = getX() + MathUtils.random(-1, 2) * GameEntity.ITEM_SIZE;
            float yBomb = getY() + MathUtils.random(-1, 2) * GameEntity.ITEM_SIZE;
            Bomb bomb = new Bomb(xBomb, yBomb,
                    EXPLOSION_DAMAGE,
                    3,
                    entityDestroyedListener,
                    mapInformationProvider,
                    getLightingManager(),
                    false);
            bomb.setDamageLevel(Wall.YELLOW_CONCRETE_TYPE);
            entityDestroyedListener.onAreaWeaponDestroyed(bomb);
            currentExplosionNumber++;
            return currentExplosionNumber == EXPLOSION_NUMBER;
        }
    }
}
