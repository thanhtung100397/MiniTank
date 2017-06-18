package com.tankzor.game.gamehud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;

/**
 * Created by Admin on 11/25/2016.
 */
public class HealBar {
    public static final float DEFAULT_HEAL_BAR_WIDTH = GameEntity.ITEM_SIZE;
    public static final float DEFAULT_HEAL_BAR_HEIGHT = GameEntity.ITEM_SIZE /  6;

    public static final float HEAL_BAR_FRAME_COUNT = 12;

    private TextureRegion[] images;
    private int currentFrameIndex;
    private DamagedEntity parent;
    private float width, height;
    private float dx, dy;

    public HealBar(DamagedEntity parent, AssetLoader assetLoader){
        this.parent = parent;
        images = assetLoader.getHealBar(parent.getTeamID());
        width = DEFAULT_HEAL_BAR_WIDTH;
        height = DEFAULT_HEAL_BAR_HEIGHT;
        dx = 0;
        dy = GameEntity.ITEM_SIZE;
        update();
    }

    public void setScale(float ratio){
        this.width *= ratio;
        this.height *= ratio;
    }

    public void translate(float dx, float dy){
        this.dx += dx;
        this.dy += dy;
    }

    public void update(){
        currentFrameIndex = Math.round(HEAL_BAR_FRAME_COUNT / parent.getMaxHitPoint() * parent.getHitPoint());
        if(currentFrameIndex > 0){
            currentFrameIndex--;
        }
    }

    public void draw(Batch batch) {
        batch.draw(images[currentFrameIndex], parent.getX() + dx, parent.getY() + dy, width, height);
    }
}
