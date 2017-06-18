package com.tankzor.game.game_object.immovable_item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;

/**
 * Created by Admin on 1/15/2017.
 */

public class PalmTree extends GameEntity {
    public static final float PALM_SIZE = ITEM_SIZE * 4.4f;

    public PalmTree(int i, int j, AssetLoader assetLoader) {
        super(i * ITEM_SIZE - (PALM_SIZE - ITEM_SIZE) / 2, j * ITEM_SIZE - (PALM_SIZE - ITEM_SIZE) / 2,
                PALM_SIZE, PALM_SIZE);
        images = assetLoader.getPalmImages();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getX();
        float y = getY();
        batch.draw(images[1], x + 40, y - 40, getWidth(), getHeight());
        batch.draw(images[0], x, y, getWidth(), getHeight());
    }
}
