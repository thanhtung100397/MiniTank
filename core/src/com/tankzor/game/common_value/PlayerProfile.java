package com.tankzor.game.common_value;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.tankzor.game.common_value.research_model.ResearchModel;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 12/24/2016.
 */

public class PlayerProfile implements Json.Serializable {
    private static final String PLAYER_DATA_FOLDER_PATH = "data/data/com.tankzor.game/player_data/";
    private static final String DATA_FILE_NAME = "player_data.json";

    private static final String SPEED_ATTRIBUTE = "speed";
    private static final String BASE_HIT_POINT_ATTRIBUTE = "baseHitPoint";
    private static final String FORCE_FIELD_ATTRIBUTE = "forceField";
    private static final String MONEY_ATTRIBUTE = "money";
    private static final String STAR_ATTRIBUTE = "star";

    public static final String LEVEL_ATTRIBUTE = "level";
    public static final String ID_ATTRIBUTE = "id";

    private static final String ADDITIONAL_INTEREST_ATTRIBUTE = "additionalInterest";

    private static final String ARTILLERY_RANGE_ATTRIBUTE = "artilleryRange";
    private static final String ARTILLERY_SPEED_ATTRIBUTE = "artillerySpeed";

    private static final String FORCE_FIELD_RECOVER_TIME_ATTRIBUTE = "forceFieldRecoverTime";
    private static final String MAXIMUM_FORCE_FIELD_ATTRIBUTE = "maximumForceField";

    private static final String NUMBER_OF_BOMB_ATTRIBUTE = "numberOfBomb";

    private static final String ALLY_TANK_HIT_POINT_ATTRIBUTE = "allyTankHP";
    private static final String ALLY_TANK_TYPE_ATTRIBUTE = "allyTankType";
    private static final String ALLY_TANK_SPEED_ATTRIBUTE = "allyTankSpeed";
    private static final String ALLY_TANK_FORCE_FIELD_ATTRIBUTE = "allyTankForceField";

    private static final String ALLY_KAMIKAZE_TANK_HIT_POINT_ATTRIBUTE = "allyKTankHP";
    private static final String ALLY_KAMIKAZE_TANK_SPEED_ATTRIBUTE = "allyKTankSpeed";
    private static final String ALLY_KAMIKAZE_TANK_EXPLOSION_ATTRIBUTE = "allyKTankExplosion";
    private static final String ALLY_KAMIKAZE_TANK_DAMAGE_ATTRIBUTE = "allyKTankDamage";

    private static final String ALLY_ARTILLERY_TANK_HIT_POINT_ATTRIBUTE = "allyATankHP";
    private static final String ALLY_ARTILLERY_TANK_SPEED_ATTRIBUTE = "allyATankSpeed";
    private static final String ALLY_ARTILLERY_TANK_FORCE_FIELD_ATTRIBUTE = "allyATankForceField";

    private static final String R_FLASH_LIGHT_COLOR_ATTRIBUTE = "rFlash";
    private static final String G_FLASH_LIGHT_COLOR_ATTRIBUTE = "gFlash";
    private static final String B_FLASH_LIGHT_COLOR_ATTRIBUTE = "bFlash";

    private static final String WEAPON_MODELS = "weaponModels";
    private static final String RESEARCH_MODELS = "researchModels";

    private static PlayerProfile instance = new PlayerProfile();

    private float speed;
    private int baseHitPoint;
    private int forceField;
    private int star;
    private int money;

    private float additionalInterest;

    private int artilleryRange;
    private float artillerySpeed;

    private float forceFieldRecoverTime;
    private int maximumForceField;

    private int allyTankType;
    private int allyTankHitPoint;
    private float allyTankSpeed;
    private int allyTankForceField;

    private int allyKamikazeTankHitPoint;
    private float allyKamikazeTankSpeed;
    private int allyKamikazeTankExplosion;
    private int allyKamikazeTankDamage;

    private int allyArtilleryTankHitPoint;
    private float allyArtilleryTankSpeed;
    private int allyArtilleryTankForceField;

    private int numberOfBomb;

    private float rFlash;
    private float gFlash;
    private float bFlash;
    private Color flashLightColor;

    private Array<WeaponModel> weaponModels;
    private Array<ResearchModel> researchModels;
    private OnMoneyStarLifeChangedListener moneyStarLifeChangedListener;
    private MissionRecorder missionRecorder = new MissionRecorder();

    public static PlayerProfile getInstance() {
        return instance;
    }

    private PlayerProfile() {
        readProfile();
    }

    public void prepareMissionRecorder(String missionName){
        missionRecorder.isComplete = false;
        missionRecorder.name = missionName;
        missionRecorder.money = 0;
        missionRecorder.star = 0;
        missionRecorder.startTime = TimeUtils.millis();
        missionRecorder.unitDestroyed = 0;
        missionRecorder.shotCount = 0;
        missionRecorder.takenDamage = 0;
        missionRecorder.travelDistance = 0;
    }

    public MissionRecorder getMissionRecorder() {
        return missionRecorder;
    }

    public MissionRecorder endMissionRecorder() {
        return missionRecorder;
    }

    private boolean isProfileExist(String name) {
        return Gdx.files.absolute(PLAYER_DATA_FOLDER_PATH + name).exists();
    }

    public void createNewProfile() {
        Gdx.files.internal("maps/" + DATA_FILE_NAME).copyTo(Gdx.files.absolute(PLAYER_DATA_FOLDER_PATH + DATA_FILE_NAME));
    }

    public void deleteExistProfile(){
        Gdx.files.absolute(PLAYER_DATA_FOLDER_PATH + DATA_FILE_NAME).delete();
    }

    public void readProfile() {
        if (!isProfileExist(DATA_FILE_NAME)) {
            createNewProfile();
        }

        JsonValue rootValue = new JsonReader().parse(Gdx.files.absolute(PLAYER_DATA_FOLDER_PATH + DATA_FILE_NAME));
        this.speed = rootValue.getFloat(SPEED_ATTRIBUTE);
        this.baseHitPoint = rootValue.getInt(BASE_HIT_POINT_ATTRIBUTE);
        this.forceField = rootValue.getInt(FORCE_FIELD_ATTRIBUTE);
        this.money = rootValue.getInt(MONEY_ATTRIBUTE);
        this.star = rootValue.getInt(STAR_ATTRIBUTE);

        this.additionalInterest = rootValue.getFloat(ADDITIONAL_INTEREST_ATTRIBUTE);

        this.artilleryRange = rootValue.getInt(ARTILLERY_RANGE_ATTRIBUTE);
        this.artillerySpeed = rootValue.getFloat(ARTILLERY_SPEED_ATTRIBUTE);

        this.forceFieldRecoverTime = rootValue.getFloat(FORCE_FIELD_RECOVER_TIME_ATTRIBUTE);
        this.maximumForceField = rootValue.getInt(MAXIMUM_FORCE_FIELD_ATTRIBUTE);

        this.numberOfBomb = rootValue.getInt(NUMBER_OF_BOMB_ATTRIBUTE);

        this.allyTankType = rootValue.getInt(ALLY_TANK_TYPE_ATTRIBUTE);
        this.allyTankHitPoint = rootValue.getInt(ALLY_TANK_HIT_POINT_ATTRIBUTE);
        this.allyTankSpeed = rootValue.getFloat(ALLY_TANK_SPEED_ATTRIBUTE);
        this.allyTankForceField = rootValue.getInt(ALLY_TANK_FORCE_FIELD_ATTRIBUTE);

        this.allyKamikazeTankHitPoint = rootValue.getInt(ALLY_KAMIKAZE_TANK_HIT_POINT_ATTRIBUTE);
        this.allyKamikazeTankSpeed = rootValue.getFloat(ALLY_KAMIKAZE_TANK_SPEED_ATTRIBUTE);
        this.allyKamikazeTankExplosion = rootValue.getInt(ALLY_KAMIKAZE_TANK_EXPLOSION_ATTRIBUTE);
        this.allyKamikazeTankDamage = rootValue.getInt(ALLY_KAMIKAZE_TANK_DAMAGE_ATTRIBUTE);

        this.allyArtilleryTankHitPoint = rootValue.getInt(ALLY_ARTILLERY_TANK_HIT_POINT_ATTRIBUTE);
        this.allyArtilleryTankSpeed = rootValue.getFloat(ALLY_ARTILLERY_TANK_SPEED_ATTRIBUTE);
        this.allyArtilleryTankForceField = rootValue.getInt(ALLY_ARTILLERY_TANK_FORCE_FIELD_ATTRIBUTE);

        flashLightColor = new Color();
        setFlashLightColor(rootValue.getFloat(R_FLASH_LIGHT_COLOR_ATTRIBUTE),
                            rootValue.getFloat(G_FLASH_LIGHT_COLOR_ATTRIBUTE),
                            rootValue.getFloat(B_FLASH_LIGHT_COLOR_ATTRIBUTE));

        researchModels = new Array<ResearchModel>(10);
        JsonValue researchValues = rootValue.get(RESEARCH_MODELS);
        for (JsonValue value : researchValues.iterator()) {
            researchModels.add(ResearchModel.createModel(value.getInt(ID_ATTRIBUTE),
                                                        value.getInt(LEVEL_ATTRIBUTE)));
        }

        weaponModels = new Array<WeaponModel>(30);
        JsonValue weaponValue = rootValue.get(WEAPON_MODELS);
        for (JsonValue value : weaponValue.iterator()) {
            WeaponModel weaponModel = new WeaponModel();
            weaponModel.id = value.getInt(WeaponModel.ID_ATTRIBUTE);
            weaponModel.unlocked = value.getBoolean(WeaponModel.UNLOCKED_ATTRIBUTE);
            weaponModel.value = value.getInt(WeaponModel.VALUE_ATTRIBUTE);
            weaponModel.purchaseCount = value.getInt(WeaponModel.PURCHASE_COUNT_ATTRIBUTE);
            weaponModel.reloadTime = value.getFloat(WeaponModel.RELOAD_TIME_ATTRIBUTE);
            weaponModel.damage = value.getInt(WeaponModel.DAMAGE_ATTRIBUTE);
            weaponModel.explosion = value.getInt(WeaponModel.EXPLOSION_ATTRIBUTE);
            weaponModel.capacity = value.getInt(WeaponModel.CAPACITY_ATTRIBUTE);
            weaponModel.maxCapacity = value.getInt(WeaponModel.MAX_CAPACITY_ATTRIBUTE);
            weaponModel.name = value.getString(WeaponModel.NAME_ATTRIBUTE);
            weaponModel.description = value.getString(WeaponModel.DESCRIPTION_ATTRIBUTE);
            weaponModel.upValue = value.getInt(WeaponModel.UP_VALUE);
            weaponModels.add(weaponModel);
        }
    }

    public Array<WeaponModel> getListWeaponModels() {
        return weaponModels;
    }

    public WeaponModel getWeaponModel(int id) {
        return weaponModels.get(id - 1);
    }

    public ResearchModel getResearchModel(int id){
        return researchModels.get(id);
    }

    public int getTankType() {
        return getWeaponModel(SupportItem.UPGRADE_TANK).capacity;
    }

    public float getSpeed() {
        return speed;
    }

    public int getBaseHitPoint() {
        return baseHitPoint;
    }

    public int getTotalHitPoint() {
        return baseHitPoint + getWeaponModel(SupportItem.TEMPORARY_ARMOR).capacity + getWeaponModel(SupportItem.PERMANENT_ARMOR).capacity;
    }

    public int getLife() {
        return getWeaponModel(SupportItem.LIFE_ITEM).capacity;
    }

    public int getStar() {
        return star;
    }

    public int getMoney() {
        return money;
    }

    public int getForceField() {
        return forceField;
    }

    public float getAdditionalInterest() {
        return additionalInterest;
    }

    public int getArtilleryRange() {
        return artilleryRange;
    }

    public float getArtillerySpeed() {
        return artillerySpeed;
    }

    public float getForceFieldRecoverTime() {
        return forceFieldRecoverTime;
    }

    public int getMaximumNumberOfForceField() {
        return maximumForceField;
    }

    public int getNumberOfBomb() {
        return numberOfBomb;
    }

    public int getAllyTankType() {
        return allyTankType;
    }

    public int getAllyTankHitPoint() {
        return allyTankHitPoint;
    }

    public float getAllyTankSpeed() {
        return allyTankSpeed;
    }

    public int getAllyTankForceField() {
        return allyTankForceField;
    }

    public int getAllyKamikazeTankHitPoint() {
        return allyKamikazeTankHitPoint;
    }

    public float getAllyKamikazeTankSpeed() {
        return allyKamikazeTankSpeed;
    }

    public int getAllyKamikazeTankExplosion() {
        return allyKamikazeTankExplosion;
    }

    public int getAllyKamikazeTankDamage() {
        return allyKamikazeTankDamage;
    }

    public int getAllyArtilleryTankHitPoint() {
        return allyArtilleryTankHitPoint;
    }

    public float getAllyArtilleryTankSpeed() {
        return allyArtilleryTankSpeed;
    }

    public int getAllyArtilleryTankForceField() {
        return allyArtilleryTankForceField;
    }

    public Color getFlashLightColor() {
        return flashLightColor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void addHP(int hp) {
        this.baseHitPoint += hp;
    }

    public void addAdditionalInterest(float value) {
        this.additionalInterest += value;
    }

    public void addArtilleryRange(int cell) {
        this.artilleryRange += cell;
    }

    public void addArtillerySpeed(float artillerySpeed) {
        this.artillerySpeed += artillerySpeed;
    }

    public void addArtilleryExplosionLevel(float additionLevel) {
        getWeaponModel(AreaWeapon.ARTILLERY).explosion += additionLevel;
    }

    public void addForceFieldRecoverTime(float value) {
        this.forceFieldRecoverTime += value;
    }

    public void addForceFieldMaximumNumber(float number) {
        this.maximumForceField += number;
    }

    public void addLife(int life) {//return life left
        WeaponModel lifeModel = getWeaponModel(SupportItem.LIFE_ITEM);
        lifeModel.capacity += life;
        if(moneyStarLifeChangedListener != null){
            moneyStarLifeChangedListener.onLifeChanged(lifeModel.capacity);
        }
    }

    public void addStar(int star) {
        missionRecorder.star += star;
        this.star += star;
        if(moneyStarLifeChangedListener != null){
            moneyStarLifeChangedListener.onStarChanged(this.star);
        }
    }

    public void addMoney(int money) {
        missionRecorder.money += money;
        this.money += money;
        if(moneyStarLifeChangedListener != null){
            moneyStarLifeChangedListener.onMoneyChanged(this.money);
        }
    }

    public void addForceField(int value) {
        this.forceField += value;
    }

    public void addNumberOfBomb(int value) {
        this.numberOfBomb += value;
    }

    public void setAllyTankType(int allyTankType) {
        this.allyTankType = allyTankType;
    }

    public void addAllyTankHitPoint(int hp) {
        this.allyTankHitPoint += hp;
    }

    public void addAllyTankSpeed(float value) {
        this.allyTankSpeed += value;
    }

    public void addAllyTankForceField(int forceField) {
        this.allyTankForceField += forceField;
    }

    public void addAllyKamikazeTankHitPoint(int hp) {
        this.allyKamikazeTankHitPoint += hp;
    }

    public void addAllyKamikazeTankSpeed(float value) {
        this.allyKamikazeTankSpeed += value;
    }

    public void addAllyKamikazeTankExplosion(int value) {
        this.allyKamikazeTankExplosion += value;
    }

    public void addAllyKamikazeTankDamage(int damage) {
        this.allyKamikazeTankDamage += damage;
    }

    public void addAllyArtilleryTankHitPoint(int hp) {
        this.allyArtilleryTankHitPoint += hp;
    }

    public void addAllyArtilleryTankSpeed(float value) {
        this.allyArtilleryTankSpeed += value;
    }

    public void addAllyArtilleryTankForceField(int forceField) {
        this.allyArtilleryTankForceField += forceField;
    }

    public void updateWeapon(int id, int value){
        WeaponModel weaponModel = getWeaponModel(id);
        weaponModel.capacity += value;
        weaponModel.value += value * weaponModel.upValue;
    }

    public void setMoneyStarLifeChangedListener(OnMoneyStarLifeChangedListener moneyStarLifeChangedListener) {
        this.moneyStarLifeChangedListener = moneyStarLifeChangedListener;
        if(moneyStarLifeChangedListener == null){
            return;
        }
        moneyStarLifeChangedListener.onMoneyChanged(money);
        moneyStarLifeChangedListener.onStarChanged(star);
        moneyStarLifeChangedListener.onLifeChanged(getLife());
    }

    public void setFlashLightColor(float r, float g, float b) {
        if(r < 0 || g < 0 || b < 0){
            return;
        }
        this.rFlash = r;
        this.gFlash = g;
        this.bFlash = b;
        this.flashLightColor.set(r, g, b, 0.75f);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void savePlayerData() {
        Json json = new Json();
        json.setElementType(PlayerProfile.class, WEAPON_MODELS, WeaponModel.class);
        json.setElementType(PlayerProfile.class, RESEARCH_MODELS, ResearchModel.class);
        Gdx.files.absolute(PLAYER_DATA_FOLDER_PATH + DATA_FILE_NAME).writeString(json.prettyPrint(this), false);
    }

    @Override
    public void write(Json json) {
        json.writeValue(SPEED_ATTRIBUTE, speed);
        json.writeValue(BASE_HIT_POINT_ATTRIBUTE, baseHitPoint);
        json.writeValue(FORCE_FIELD_ATTRIBUTE, forceField);
        json.writeValue(STAR_ATTRIBUTE, star);
        json.writeValue(MONEY_ATTRIBUTE, money);

        json.writeValue(ADDITIONAL_INTEREST_ATTRIBUTE, additionalInterest);

        json.writeValue(ARTILLERY_RANGE_ATTRIBUTE, artilleryRange);
        json.writeValue(ARTILLERY_SPEED_ATTRIBUTE, artillerySpeed);

        json.writeValue(FORCE_FIELD_RECOVER_TIME_ATTRIBUTE, forceFieldRecoverTime);
        json.writeValue(MAXIMUM_FORCE_FIELD_ATTRIBUTE, maximumForceField);

        json.writeValue(NUMBER_OF_BOMB_ATTRIBUTE, numberOfBomb);

        json.writeValue(ALLY_TANK_TYPE_ATTRIBUTE, allyTankType);
        json.writeValue(ALLY_TANK_HIT_POINT_ATTRIBUTE, allyTankHitPoint);
        json.writeValue(ALLY_TANK_SPEED_ATTRIBUTE, allyTankSpeed);
        json.writeValue(ALLY_TANK_FORCE_FIELD_ATTRIBUTE, allyTankForceField);

        json.writeValue(ALLY_KAMIKAZE_TANK_HIT_POINT_ATTRIBUTE, allyKamikazeTankHitPoint);
        json.writeValue(ALLY_KAMIKAZE_TANK_SPEED_ATTRIBUTE, allyKamikazeTankSpeed);
        json.writeValue(ALLY_KAMIKAZE_TANK_EXPLOSION_ATTRIBUTE, allyKamikazeTankExplosion);
        json.writeValue(ALLY_KAMIKAZE_TANK_DAMAGE_ATTRIBUTE, allyKamikazeTankDamage);

        json.writeValue(ALLY_ARTILLERY_TANK_HIT_POINT_ATTRIBUTE, allyArtilleryTankHitPoint);
        json.writeValue(ALLY_ARTILLERY_TANK_SPEED_ATTRIBUTE, allyArtilleryTankSpeed);
        json.writeValue(ALLY_ARTILLERY_TANK_FORCE_FIELD_ATTRIBUTE, allyArtilleryTankForceField);

        json.writeValue(R_FLASH_LIGHT_COLOR_ATTRIBUTE, rFlash);
        json.writeValue(G_FLASH_LIGHT_COLOR_ATTRIBUTE, gFlash);
        json.writeValue(B_FLASH_LIGHT_COLOR_ATTRIBUTE, bFlash);

        json.writeValue(RESEARCH_MODELS, researchModels);
        json.writeValue(WEAPON_MODELS, weaponModels);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}
