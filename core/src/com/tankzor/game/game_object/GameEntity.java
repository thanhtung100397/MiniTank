package com.tankzor.game.game_object;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
/**
 * Created by Admin on 11/6/2016.
 */

public abstract class GameEntity extends Actor {
    public static float ITEM_SIZE = 20.0f;

    protected int currentImageIndex = 0;
    protected TextureRegion[] images;

    public GameEntity(float x, float y, float width, float height) {
        setPosition(x, y);
        setSize(width, height);
    }

    @Override
    public void act(float delta) {
        Array<Action> listAction = getActions();
        for (int i = listAction.size - 1; i >= 0; i--) {
            if(listAction.get(i).act(delta)){
                listAction.removeIndex(i);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(images[currentImageIndex], getX(), getY(), getWidth(), getHeight());
    }
}
