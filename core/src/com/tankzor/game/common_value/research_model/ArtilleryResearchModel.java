package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;

/**
 * Created by Admin on 1/23/2017.
 */

public class ArtilleryResearchModel extends ResearchModel {
    public ArtilleryResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Artillery research";
        this.maxLevel = 5;
        this.starOfEachLevel = new int[]{4, 4, 4, 5, 6};
        this.description = "Maximum level - 5\n\n"+
                "Level 1 - Unlock Artillery in workshop - a charge that flies over all the obstacles and walls. Explosion force at the ground zero - 9 HP\n\n"+
                "Level 2 - Increase explosion force to 11 HP\n\n" +
                "Level 3 - Increase shooting range by 1 cell\n\n" +
                "Level 4 - Increase explosion force to 14 HP, and speed up missile by 120%.\n\n" +
                "Level 5 - Increase area of explosion by 2 cell to 14 HP and force of explosion to 17 HP at the epicenter od explosion.\n\n" +
                "Artillery, like Dynamite, is more effective against turrets (for example, an explosion with the force 10 HP will inflict the same damage as 20 regular rounds)";
    }

    @Override
    protected void doEffect() {
        switch (currentLevel){
            case 1:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.ARTILLERY).unlocked = true;
            }
            break;

            case 2:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.ARTILLERY).damage = 11;
            }
            break;

            case 3:{
                PlayerProfile.getInstance().addArtilleryRange(1);
            }
            break;

            case 4:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.ARTILLERY).damage = 14;
                PlayerProfile.getInstance().addArtillerySpeed(-0.15f);
            }
            break;

            case 5:{
                PlayerProfile.getInstance().getWeaponModel(AreaWeapon.ARTILLERY).damage = 16;
                PlayerProfile.getInstance().addArtilleryExplosionLevel(1);
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public int getID() {
        return ResearchModel.ARTILLERY_RESEARCH_ID;
    }
}
