package com.tankzor.game.common_value.research_model;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.tankzor.game.common_value.PlayerProfile;

/**
 * Created by Admin on 1/23/2017.
 */

public abstract class ResearchModel implements Json.Serializable{
    public static final int ADDITIONAL_INTEREST_RESEARCH_ID = 0;
    public static final int ROUNDS_RESEARCH_ID = 1;
    public static final int ARTILLERY_RESEARCH_ID = 2;
    public static final int MISSILES_RESEARCH_ID = 3;
    public static final int MINES_RESEARCH_ID = 4;
    public static final int DYNAMITE_RESEARCH_ID = 5;
    public static final int ARMOR_RESEARCH_ID = 6;
    public static final int FORCE_FIELD_RESEARCH_ID = 7;
    public static final int AIR_STRIKE_RESEARCH_ID = 8;
    public static final int ALLY_TANK_RESEARCH_ID = 9;

    public String name;
    public int currentLevel;
    public int maxLevel;
    public int[] starOfEachLevel;
    public String description;

    public ResearchModel(int currentLevel){
        this.currentLevel = currentLevel;
    }

    public void levelUp(){
        if(currentLevel == maxLevel) {
            return;
        }
        currentLevel++;
        doEffect();
    }

    protected abstract void doEffect();

    public abstract int getID();

    public boolean isMaximumLevel(){
        return currentLevel == maxLevel;
    }

    public int getStarOfCurrentLevel(){
        return starOfEachLevel[currentLevel];
    }

    @Override
    public void write(Json json) {
        json.writeValue(PlayerProfile.ID_ATTRIBUTE, getID());
        json.writeValue(PlayerProfile.LEVEL_ATTRIBUTE, currentLevel);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    public static ResearchModel createModel(int id, int currentLevel){
        switch (id){
            case ADDITIONAL_INTEREST_RESEARCH_ID:{
                return new AdditionalInterestResearchModel(currentLevel);
            }

            case ROUNDS_RESEARCH_ID:{
                return new RoundsResearchModel(currentLevel);
            }

            case ARTILLERY_RESEARCH_ID:{
                return new ArtilleryResearchModel(currentLevel);
            }

            case MISSILES_RESEARCH_ID:{
                return new MissilesResearchModel(currentLevel);
            }

            case MINES_RESEARCH_ID:{
                return new MinesResearchModel(currentLevel);
            }

            case DYNAMITE_RESEARCH_ID:{
                return new DynamiteResearchModel(currentLevel);
            }

            case ARMOR_RESEARCH_ID:{
                return new ArmorResearchModel(currentLevel);
            }

            case FORCE_FIELD_RESEARCH_ID:{
                return new ForceFieldResearchModel(currentLevel);
            }

            case AIR_STRIKE_RESEARCH_ID:{
                return new AirStrikeResearchModel(currentLevel);
            }

            case ALLY_TANK_RESEARCH_ID:{
                return new AllyTankResearchModel(currentLevel);
            }

            default:{
                break;
            }
        }
        return null;
    }
}
