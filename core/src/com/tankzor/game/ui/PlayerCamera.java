package com.tankzor.game.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 1/2/2017.
 */

public class PlayerCamera extends OrthographicCamera {
    private PlayerWarMachine playerWarMachine;
    private Rectangle cullingArea;
//    private float halfWidthCulling, halfHeightCulling;
    private float halfItemSize;

    public PlayerCamera(int width, int height) {
        super(width, height);
        cullingArea = new Rectangle(-GameEntity.ITEM_SIZE * 2, -GameEntity.ITEM_SIZE * 2, width + GameEntity.ITEM_SIZE * 2, height + GameEntity.ITEM_SIZE * 2);
//        this.halfWidthCulling = cullingArea.getWidth() / 2;
//        this.halfHeightCulling = cullingArea.getHeight() / 2;
        this.halfItemSize = GameEntity.ITEM_SIZE / 2;
    }

    public void setPlayerWarMachine(PlayerWarMachine playerWarMachine) {
        this.playerWarMachine = playerWarMachine;
    }

    public void updatePosition() {
        if (playerWarMachine == null || playerWarMachine.isDestroyed()) {
            return;
        }
        float dx = playerWarMachine.getX() + halfItemSize - position.x;
        float dy = playerWarMachine.getY() + halfItemSize - position.y;
        position.x += dx;
        position.y += dy;
        cullingArea.x += dx;
        cullingArea.y += dy;
    }

    public Rectangle getCullingArea() {
        return cullingArea;
    }

    public boolean isInsideCullingArea(DamagedEntity entity){
        return cullingArea.overlaps(entity.getBound());
    }

    public PlayerWarMachine getPlayerWarMachine() {
        return playerWarMachine;
    }
}
