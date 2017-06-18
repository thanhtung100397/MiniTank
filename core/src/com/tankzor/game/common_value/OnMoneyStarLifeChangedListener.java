package com.tankzor.game.common_value;

/**
 * Created by Admin on 3/23/2017.
 */

public interface OnMoneyStarLifeChangedListener {
    void onMoneyChanged(int newMoney);
    void onStarChanged(int newStar);
    void onLifeChanged(int newLife);
}
