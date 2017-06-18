package com.tankzor.game.common_value.research_model;

import com.tankzor.game.common_value.PlayerProfile;

/**
 * Created by Admin on 1/23/2017.
 */

public class AdditionalInterestResearchModel extends ResearchModel {
    public AdditionalInterestResearchModel(int currentLevel) {
        super(currentLevel);
        this.name = "Additional interest";
        this.maxLevel = 2;
        this.starOfEachLevel = new int[]{9, 12};
        this.description = "In the end of each level you get additional interest to your eared coins (by default 5%), which you can increase by researching the extra interest.\n\nMaximum level - 15%";
    }

    @Override
    protected void doEffect() {
        PlayerProfile.getInstance().addAdditionalInterest((currentLevel + 1) * 0.05f);
    }

    @Override
    public int getID() {
        return ResearchModel.ADDITIONAL_INTEREST_RESEARCH_ID;
    }
}
