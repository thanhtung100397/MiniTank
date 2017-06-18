package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 1/24/2017.
 */

public class ArmorResearchModel extends ResearchModel {

    public ArmorResearchModel(int currentLevel){
        super(currentLevel);
        this.name = "Armor research";
        this.maxLevel = 3;
        this.starOfEachLevel = new int[]{5, 5, 5};
        this.description = "Maximum level - 3\n\n"+
                "Level 1 - Unlock Temporary Armor in workshop (which will be removed when your tank is destroyed)\n\n" +
                "Level 2 - Unlock Permanent Armor in workshop (which won't be removed when your tank is destroyed)\n\n" +
                "Level 3 - Makes every unit of Armor in workshop cheaper, reducing Armor and Force Field price by 30%, increase level of Temporary and Permanent Armor from 4 to 5, make Tank Repair and Repair Kit cheaper by 3 coins for every unit of HP";
    }

    @Override
    protected void doEffect() {
        switch (currentLevel){
            case 1:{
                PlayerProfile.getInstance().getWeaponModel(SupportItem.TEMPORARY_ARMOR).unlocked = true;
            }
            break;

            case 2:{
                PlayerProfile.getInstance().getWeaponModel(SupportItem.PERMANENT_ARMOR).unlocked = true;
            }
            break;

            case 3:{
                WeaponModel temporaryArmor = PlayerProfile.getInstance().getWeaponModel(SupportItem.TEMPORARY_ARMOR);
                temporaryArmor.maxCapacity = 5;
                temporaryArmor.value *= 0.7;
                temporaryArmor.upValue *= 0.7;
                WeaponModel permanentArmor = PlayerProfile.getInstance().getWeaponModel(SupportItem.PERMANENT_ARMOR);
                permanentArmor.maxCapacity = 5;
                permanentArmor.value *= 0.7;
                permanentArmor.upValue *= 0.7;

                WeaponModel forceField = PlayerProfile.getInstance().getWeaponModel(SupportItem.FORCE_FIELD);
                forceField.value *= 0.7;
                forceField.upValue *= 0.7;

                PlayerProfile.getInstance().getWeaponModel(SupportItem.REPAIR_TANK).value = 3;
                PlayerProfile.getInstance().getWeaponModel(SupportItem.REPAIR_ALLY_TANK).value = 3;
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public int getID() {
        return ResearchModel.ARMOR_RESEARCH_ID;
    }
}
