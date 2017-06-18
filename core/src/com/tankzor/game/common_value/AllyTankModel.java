package com.tankzor.game.common_value;

import com.badlogic.gdx.utils.Json;

/**
 * Created by Admin on 1/27/2017.
 */

public class AllyTankModel extends WeaponModel {
    public static final String HIT_POINT_ATTRIBUTE = "hitPoint";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String SPEED_ATTRIBUTE = "speed";
    public static final String FORCE_FIELD_ATTRIBUTE = "forceField";

    public int hp;
    public int type;
    public int speed;
    public int forceField;

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue(HIT_POINT_ATTRIBUTE, hp);
        json.writeValue(TYPE_ATTRIBUTE, type);
        json.writeValue(SPEED_ATTRIBUTE, speed);
        json.writeValue(FORCE_FIELD_ATTRIBUTE, forceField);
    }
}
