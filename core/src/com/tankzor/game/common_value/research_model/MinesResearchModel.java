package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;

/**
 * Created by Admin on 1/24/2017.
 */

public class MinesResearchModel extends ResearchModel {
    public MinesResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Mines research";
        this.maxLevel = 2;
        this.starOfEachLevel = new int[]{3, 5};
        this.description = "Maximum level - 2\n\n" +
                "Level 1 - Unlock Mine TMD-5 (damage 5 HP) in workshop\n\n" +
                "Level 2 - Unlock Mine TMD-9 (damage 9 HP) in workshop\n\n"+
                "Mines can only hurt enemies unit";
    }

    @Override
    protected void doEffect() {
        if(currentLevel == 1){
            PlayerProfile.getInstance().getWeaponModel(AreaWeapon.MINE_TMD_5).unlocked = true;
        }else {
            PlayerProfile.getInstance().getWeaponModel(AreaWeapon.MINE_TMD_9).unlocked = true;
        }
    }

    @Override
    public int getID() {
        return ResearchModel.MINES_RESEARCH_ID;
    }
}
