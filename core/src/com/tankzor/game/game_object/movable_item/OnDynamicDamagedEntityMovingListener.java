package com.tankzor.game.game_object.movable_item;

/**
 * Created by Admin on 11/16/2016.
 */

public interface OnDynamicDamagedEntityMovingListener {
    void onDynamicDamagedEntityEnter(DynamicDamagedEntity item, float x1, float y1, float x2, float y2);
    void onDynamicDamagedEntityExit(DynamicDamagedEntity item, float x1, float y1, float x2 , float y2);
}
