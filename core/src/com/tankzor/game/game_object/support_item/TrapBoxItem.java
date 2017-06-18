package com.tankzor.game.game_object.support_item;

import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.manager.GroundWeaponManager;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Dynamite;

/**
 * Created by Admin on 4/25/2017.
 */

public class TrapBoxItem extends BoxItem {
    private Dynamite dynamite;
    private GroundWeaponManager groundWeaponManager;

    public TrapBoxItem(float x, float y,
                       Dynamite dynamite,
                       GroundWeaponManager groundWeaponManager,
                       String message,
                       AssetLoader assetLoader) {
        super(x, y, AreaWeapon.DYNAMITE_X70, 0, message, assetLoader);
        this.dynamite = dynamite;
        this.groundWeaponManager = groundWeaponManager;
    }

    @Override
    public void addEffectTo(MovableWarMachine item) {
        super.addEffectTo(item);
        groundWeaponManager.addDynamite(dynamite);
        remove();
    }
}
