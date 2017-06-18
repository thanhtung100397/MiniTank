package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 1/24/2017.
 */

public class AirStrikeResearchModel extends ResearchModel {

    public AirStrikeResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Air strike research";
        this.maxLevel = 3;
        this.starOfEachLevel = new int[]{6, 6, 6};
        this.description = "Maximum level - 3\n\n" +
                "Level 1 and the presence of Radar - Unlock Air Strike in workshop (damage 11 HP in epicenter of explosion)\n\n" +
                "Level 2 - Increase level of damage from every bomb to 18 HP and explosion area by 2 cells\n\n" +
                "Level 3 - Increase number of bombs from 5 to 10 and increase the power of explosion to 21 HP\n\n" +
                "Bomb explosion destroy anti-tank hedgehogs, yellow stone wall and mines";
    }

    @Override
    protected void doEffect() {
        switch (currentLevel){
            case 1:{
                if(PlayerProfile.getInstance().getWeaponModel(SupportItem.RADAR).capacity == 1) {
                    PlayerProfile.getInstance().getWeaponModel(AreaWeapon.AIR_STRIKE).unlocked = true;
                }
            }
            break;

            case 2:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.AIR_STRIKE).damage = 19;
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.AIR_STRIKE).explosion += 1;
            }
            break;

            case 3:{
                PlayerProfile.getInstance().addNumberOfBomb(5);
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.AIR_STRIKE).damage = 21;
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public int getID() {
        return ResearchModel.AIR_STRIKE_RESEARCH_ID;
    }
}
