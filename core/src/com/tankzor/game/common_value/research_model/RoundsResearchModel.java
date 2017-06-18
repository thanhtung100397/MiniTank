package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;

/**
 * Created by Admin on 1/23/2017.
 */

public class RoundsResearchModel extends ResearchModel {
    public RoundsResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Rounds research";
        this.maxLevel = 8;
        this.starOfEachLevel = new int[]{5, 7, 8, 8, 9, 4, 4, 4};
        this.description = "Maximum level of research - 8\n\n" +
                "Level 1 - Unlock plasma round in workshop\n" +
                "Level 2 - Unlock double round in workshop\n" +
                "Level 3 - Unlock double plasma round in workshop\n" +
                "Level 4 - Unlock high-explosive round in workshop\n" +
                "Level 5 - Unlock armor-piercing round in workshop\n" +
                "Levels 6,7,8 - Reduce reload time for all type of round (-0.1 second for each level)\n\n" +
                "Rounds is less effective against turrets than explosive weapons (dynamites, missile)\n\n" +
                "The star cost for this research depends on the current level of research: 5, 7, 8, 8, 9, 4, 4, 4";

    }

    @Override
    protected void doEffect() {
        if (1 <= currentLevel && currentLevel <= 5) {
            PlayerProfile.getInstance().getWeaponModel(currentLevel + 1).unlocked = true;
        } else {
            WeaponModel weaponModel;
            PlayerProfile playerProfile = PlayerProfile.getInstance();
            for (int i = Bullet.NORMAL_BULLET; i <= Bullet.ARMOR_PIERCING_BULLET; i++) {
                weaponModel = playerProfile.getWeaponModel(i);
                weaponModel.reloadTime -= 0.1;
                weaponModel.value += i * 5;
            }
        }
    }

    @Override
    public int getID() {
        return ResearchModel.ROUNDS_RESEARCH_ID;
    }
}
