package com.tankzor.game.game_object.support_item;

/**
 * Created by Admin on 12/31/2016.
 */

public interface SupportItemDurationListener {
    void onActive(int duration);
    void onDurationChange(int duration);
    void onTimeOut();
}
