package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 1/24/2017.
 */

public class MissilesResearchModel extends ResearchModel {
    public MissilesResearchModel(int currentLevel){
        super(currentLevel);
        this.name = "Missiles research";
        this.maxLevel = 2;
        this.starOfEachLevel = new int[]{5, 5};
        this.description = "Maximum level - 2\n\n"+
                "Level 1 - Unlock Warhead in workshop\n\n" +
                "Level 2 - Unlock Homing Missile with damage of 32 HP (destroy anti-tank hedgehogs, and is the only type of weapon, which can destroy gray stone walls)";
    }

    @Override
    protected void doEffect() {
        if(currentLevel == 1){
            PlayerProfile.getInstance().getWeaponModel(Bullet.MISSILE_BULLET).unlocked = true;
        }else {
//            PlayerProfile.getInstance().getWeaponModel(AreaWeapon.HOMING_MISSILE).unlocked = true;
        }
    }

    @Override
    public int getID() {
        return ResearchModel.MISSILES_RESEARCH_ID;
    }
}
