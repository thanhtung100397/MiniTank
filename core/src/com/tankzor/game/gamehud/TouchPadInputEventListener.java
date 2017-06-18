package com.tankzor.game.gamehud;

import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 11/16/2016.
 */

public interface TouchPadInputEventListener {
    void onFireButtonInputEvent(boolean isPress);
    void onTouchFlashLight();
    void onTouchSwitchWeaponsButton();
    void onTouchDPadEvent(int orient, boolean isPress);
    PlayerWarMachine getPlayerWarMachine();
    void onTouchGesturePad(MovingTouchPad movingTouchPad);
}
