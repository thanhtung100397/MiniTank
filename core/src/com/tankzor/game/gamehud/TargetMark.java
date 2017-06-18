package com.tankzor.game.gamehud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;

/**
 * Created by Admin on 12/4/2016.
 */

public class TargetMark extends Actor {
    public static final int TARGET_MARK_GREEN = 0;
    public static final int TARGET_MARK_RED = 1;
    private static final float MARK_SIZE = GameEntity.ITEM_SIZE;

    private TextureRegion[] images;
    private int currentType;

    public TargetMark(AssetLoader assetLoader) {
        setX(0);
        setY(0);
        images = assetLoader.getTargetMarkImages();
    }

    public boolean setLocation(float x, float y) {//return true if mark's position does not changed anymore
        if (getX() == x && getY() == y) {
            return true;
        }
        setPosition(x, y);
        return false;
    }

    public void translate(float dx, float dy) {
        setPosition(getX() + dx, getY() + dy);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(images[currentType], getX(), getY(), MARK_SIZE, MARK_SIZE);
    }

    public void setType(int type) {
        if (type == TARGET_MARK_GREEN || type == TARGET_MARK_RED) {
            currentType = type;
        }
    }
}