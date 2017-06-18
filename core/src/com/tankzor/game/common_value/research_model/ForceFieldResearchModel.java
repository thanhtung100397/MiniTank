package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 1/24/2017.
 */

public class ForceFieldResearchModel extends ResearchModel {

    public ForceFieldResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Force field research";
        this.maxLevel = 4;
        this.starOfEachLevel = new int[]{5, 5, 5, 5};
        this.description = "Maximum level - 4\n\n" +
                "Level 1 - Unlock Force field in workshop\n\n" +
                "Level 2, 3 - Increase recover rate of force field\n\n" +
                "Level 4 - Also increase recover rate and increase maximum number of force field from 5 to 7 HP";
    }

    @Override
    protected void doEffect() {
        switch (currentLevel) {
            case 1: {
                PlayerProfile.getInstance().getWeaponModel(SupportItem.FORCE_FIELD).unlocked = true;
            }
            break;

            case 2:
            case 3: {
                PlayerProfile.getInstance().addForceFieldRecoverTime(-0.3f);
                PlayerProfile.getInstance().getWeaponModel(SupportItem.FORCE_FIELD).value += 20;
            }
            break;

            case 4:{
                PlayerProfile.getInstance().addForceFieldRecoverTime(-0.3f);
                PlayerProfile.getInstance().addForceFieldMaximumNumber(2);;
                PlayerProfile.getInstance().getWeaponModel(SupportItem.FORCE_FIELD).value += 50;
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public int getID() {
        return ResearchModel.FORCE_FIELD_RESEARCH_ID;
    }
}
