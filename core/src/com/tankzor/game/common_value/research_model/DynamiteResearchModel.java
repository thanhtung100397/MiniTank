package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;

/**
 * Created by Admin on 1/24/2017.
 */

public class DynamiteResearchModel extends ResearchModel {

    public DynamiteResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Dynamite research";
        this.maxLevel = 3;
        this.starOfEachLevel = new int[]{4, 4, 4};
        this.description = "Maximum level - 3\n\n" +
                "Level 1 - Unlock Dynamite x70 (damage 7 HP) in workshop\n\n" +
                "Level 2 - Unlock Dynamite x100 (damage 10 HP) in workshop\n\n" +
                "Level 3 - Unlock Dynamite x160 (damage 19 HP) in workshop, which has bigger explosion area\n\n" +
                "All type of dynamite can destroy anti-tank hedgehogs, yellow stone walls and mines. Also dynamite is effective against to turret (for example, an explosion with the force 10 HP will inflict the same damage as 20 regular rounds)";
    }

    @Override
    protected void doEffect() {
        switch (currentLevel){
            case 1:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.DYNAMITE_X70).unlocked = true;
            }
            break;

            case 2:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.DYNAMITE_X100).unlocked = true;
            }
            break;

            case 3:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.DYNAMITE_X160).unlocked = true;
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public int getID() {
        return ResearchModel.DYNAMITE_RESEARCH_ID;
    }
}
