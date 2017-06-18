package com.tankzor.game.game_object.support_item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 12/24/2016.
 */

public class BoxItem extends SupportItem {
    private TextureRegion[] image;
    private int weaponType;
    private int capacity;
    private String message;

    public BoxItem(float x, float y, int weaponType, int capacity, String message, AssetLoader assetLoader) {
        super(x, y, null);
        this.weaponType = weaponType;
        this.capacity = capacity;
        this.message = message;
        image = assetLoader.getSupportItemImages(BOX_ITEM);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image[0], getX(), getY(), GameEntity.ITEM_SIZE, GameEntity.ITEM_SIZE);
    }

    @Override
    public void addEffectTo(MovableWarMachine item) {
        super.addEffectTo(item);
        if(weaponType != -1 && item instanceof PlayerWarMachine){
            ((PlayerWarMachine)item).getWeaponManager().updateWeaponItem(weaponType, capacity);
        }
        remove();
    }
}
