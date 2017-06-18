package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 1/24/2017.
 */

public class AllyTankResearchModel extends ResearchModel {

    public AllyTankResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Ally tank research";
        this.maxLevel = 10;
        this.starOfEachLevel = new int[]{6, 6, 6, 6, 6, 6, 6, 7, 7, 7};
        this.description = "Maximum level - 3\n\n" +
                "Level 1 - Unlock Ally Tank in workshop, light tank type with armor of 5 HP\n\n" +
                "Level 2, 3 - Increase +1 HP for your Ally Tank and its speed, also price\n\n" +
                "Level 4 - Upgrade your Ally tank to heavy type, with additional armor + 3HP\n\n" +
                "Level 5 - Unlock Ally Kamikaze Tank in workshop, low armor by very fast, which will destroy and make 5 x 5 explosion area when it's near enemies tank, cause damage 10 HP to enemies within epicenter of explosion.\n\n" +
                "Level 6, 7 - Increase +1 HP armor for your Kamikaze Tank, add +3 HP to damage of explosion, +1 cell to explosion radius and increase its speed, and also price\n\n" +
                "Level 8 - Unlock Ally Artillery Tank in workshop, medium armor but powerful firearm, it will fire Artillery when enemies in its range, use the same type of your artillery. If you haven't researched Artillery Research yet, it can NOT fire Artillery, so please research it before purchasing this Ally Tank\n\n" +
                "Level 9, 10 - +2 HP and +2 Force Field to your Ally Tank and Ally Artillery Tank, and increase price\n\n" +
                "Survival ally tanks will return to your weapon list when mission is over";
    }

    @Override
    protected void doEffect() {
        switch (currentLevel) {
            case 1: {
                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_TANK).unlocked = true;
            }
            break;

            case 2:
            case 3: {
                PlayerProfile.getInstance().addAllyTankHitPoint(1);
                PlayerProfile.getInstance().addAllyTankSpeed(-0.1f);
                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_TANK).value += 10;
            }
            break;

            case 4: {
                PlayerProfile.getInstance().setAllyTankType(WarMachine.HEAVY_TANK_TYPE);
                PlayerProfile.getInstance().addAllyTankHitPoint(3);
                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_TANK).value += 50;
            }
            break;

            case 5: {
                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_KAMIKAZE_TANK).unlocked = true;
            }
            break;

            case 6:
            case 7:{
                PlayerProfile.getInstance().addAllyKamikazeTankHitPoint(1);
                PlayerProfile.getInstance().addAllyKamikazeTankExplosion(1);
                PlayerProfile.getInstance().addAllyKamikazeTankDamage(3);
                PlayerProfile.getInstance().addAllyKamikazeTankSpeed(-0.1f);
                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_KAMIKAZE_TANK).value += 15;
            }
            break;

            case 8:{
                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_ARTILLERY_TANK).unlocked = true;
            }
            break;

            case 9:
            case 10:{
                PlayerProfile.getInstance().addAllyTankHitPoint(2);
                PlayerProfile.getInstance().addAllyTankForceField(2);
                PlayerProfile.getInstance().addAllyArtilleryTankHitPoint(2);
                PlayerProfile.getInstance().addAllyArtilleryTankForceField(2);

                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_TANK).value += 100;
                PlayerProfile.getInstance().getWeaponModel(SupportItem.ALLY_KAMIKAZE_TANK).value += 100;
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public int getID() {
        return ResearchModel.ALLY_TANK_RESEARCH_ID;
    }
}
