package com.tankzor.game.game_object.game_mode;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.TerrainListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 3/28/2017.
 */

public class Flag extends Actor implements TerrainListener {
    public static final int BLUE_FLAG = 0;
    public static final int RED_FLAG = 1;
    public static final int GREEN_FLAG = 2;

    private TextureRegion image;
    private Terrain container;
    private OnFlagRemovedListener flagRemovedListener;

    public Flag(float x, float y, TextureRegion image, Terrain container) {
        setPosition(x, y);
        this.image = image;
        this.container = container;
        this.container.registerTerrainListener(this, -1);
    }

    public void setOnFlagRemovedListener(OnFlagRemovedListener flagRemovedListener) {
        this.flagRemovedListener = flagRemovedListener;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image, getX(), getY(), GameEntity.ITEM_SIZE, GameEntity.ITEM_SIZE);
    }

    @Override
    public void onItemEntered(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {
        if (warMachine instanceof PlayerWarMachine) {
            container.removeTerrainListener(this);
            destroy();
        }
    }

    public void destroy(){
        remove();
        if(flagRemovedListener != null){
            flagRemovedListener.onFlagRemoved();
        }
    }

    @Override
    public void onItemExited(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {

    }

    public static class FlagModel {
        public int i, j;
        public int type;

        public FlagModel(int i, int j, int type) {
            this.i = i;
            this.j = j;
            this.type = type;
        }
    }
}
