package com.tankzor.game.common_value;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Admin on 1/23/2017.
 */

public class WeaponModel implements Json.Serializable{
    public static final String ID_ATTRIBUTE = "id";
    public static final String UNLOCKED_ATTRIBUTE = "unlocked";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String PURCHASE_COUNT_ATTRIBUTE = "purchaseCount";
    public static final String RELOAD_TIME_ATTRIBUTE = "reloadTime";
    public static final String DAMAGE_ATTRIBUTE = "damage";
    public static final String EXPLOSION_ATTRIBUTE = "explosion";
    public static final String CAPACITY_ATTRIBUTE = "capacity";
    public static final String MAX_CAPACITY_ATTRIBUTE = "maxCapacity";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String DESCRIPTION_ATTRIBUTE = "description";
    public static final String UP_VALUE = "upValue";

    public int id;
    public int value;
    public boolean unlocked;
    public int purchaseCount;
    public float reloadTime;
    public int damage;
    public int explosion;
    public int capacity;
    public int maxCapacity;
    public int upValue;
    public String name;
    public String description;

    @Override
    public void write(Json json) {
        json.writeValue(ID_ATTRIBUTE, id);
        json.writeValue(VALUE_ATTRIBUTE, value);
        json.writeValue(UNLOCKED_ATTRIBUTE, unlocked);
        json.writeValue(PURCHASE_COUNT_ATTRIBUTE, purchaseCount);
        json.writeValue(RELOAD_TIME_ATTRIBUTE, reloadTime);
        json.writeValue(DAMAGE_ATTRIBUTE, damage);
        json.writeValue(EXPLOSION_ATTRIBUTE, explosion);
        json.writeValue(CAPACITY_ATTRIBUTE, capacity);
        json.writeValue(MAX_CAPACITY_ATTRIBUTE, maxCapacity);
        json.writeValue(NAME_ATTRIBUTE, name);
        json.writeValue(DESCRIPTION_ATTRIBUTE, description);
        json.writeValue(UP_VALUE, upValue);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}
