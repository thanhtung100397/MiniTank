package com.tankzor.game.common_value;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.animation.Explosion;
import com.tankzor.game.game_object.manager.PathFindingProvider;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Mine;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.gamehud.Pane;

/**
 * Created by Admin on 3/19/2017.
 */

public class AssetLoader {
    private static final String LIGHT_TANK_IMAGES_FILE_NAME = "images/light_tank.atlas";
    private static final String HEAVY_TANK_IMAGES_FILE_NAME = "images/heavy_tank.atlas";
    private static final String KAMIKAZE_TANK_IMAGES_FILE_NAME = "images/kamikaze_tank.atlas";
    private static final String SIEGE_TANK_IMAGES_FILE_NAME = "images/siege_tank.atlas";
    private static final String TURRET_IMAGES_FILE_NAME = "images/turret.atlas";
    private static final String BULLET_IMAGES_FILE_NAME = "images/bullet.atlas";
    private static final String SMALL_NORMAL_EXPLOSION_IMAGES_FILE_NAME = "images/small_normal_exp.atlas";
    private static final String SMALL_PLASMA_EXPLOSION_IMAGES_FILE_NAME = "images/small_plasma_exp.atlas";
    private static final String BIG_NORMAL_EXPLOSION_IMAGES_FILE_NAME = "images/big_normal_exp.atlas";
    private static final String BIG_PLASMA_EXPLOSION_IMAGES_FILE_NAME = "images/big_plasma_exp.atlas";
    private static final String BIG_RED_EXPLOSION_IMAGES_FILE_NAME = "images/big_red_exp.atlas";
    private static final String TRACKS_IMAGES_FILE_NAME = "images/tracks.atlas";
    private static final String GREEN_HEAL_BAR_IMAGES_FILE_NAME = "images/heal_bar_green.atlas";
    private static final String RED_HEAL_BAR_FILE_NAME = "images/heal_bar_red.atlas";
    private static final String GREEN_HEAL_PANE_FILE_NAME = "images/green_pane.atlas";
    private static final String RED_HEAL_PANE_FILE_NAME = "images/red_pane.atlas";
    private static final String BLUE_HEAL_PANE_FILE_NAME = "images/blue_pane.atlas";
    private static final String ICON_FILE_NAME = "images/icon.atlas";
    private static final String ARTILLERY_FILE_NAME = "images/artillery.atlas";
    private static final String MINE_FILE_NAME = "images/mine.atlas";
    private static final String DYNAMITE_FILE_NAME = "images/dynamite.atlas";
    private static final String INFORMATION_PANEL_FILE_NAME = "images/information_panel.png";
    private static final String AIRCRAFT_IMAGE_NAME = "images/aircrafts.atlas";
    private static final String TARGET_MARK_IMAGE_NAME = "images/target_mark.atlas";
    private static final String CLOUD_IMAGES_FILE_NAME = "images/clouds.atlas";
    private static final String SUPPORT_ITEM_IMAGE_FILE_NAME = "images/support_item.atlas";
    private static final String RADAR_IMAGES_FILE_NAME = "images/radar_building.png";
    private static final String NUCLEAR_IMAGES_FILE_NAME = "images/nuclear_building.png";
    private static final String PALMS_IMAGE_FILE_NAME = "images/palms.atlas";
    private static final String FLAG_IMAGE_FILE_NAME = "images/flag.atlas";
    private static final String RINGS_IMAGE_FILE_NAME = "images/rings.atlas";

    private AssetManager assetManager;

    private TextureRegion[] lightTankTextureRegions;
    private TextureRegion[] heavyTankTextureRegions;
    private TextureRegion[] kamikazeTankTextureRegions;
    private TextureRegion[] artilleryTankTextureRegions;
    private TextureRegion[] turretTextureRegions;

    private TextureRegion[] bulletTextureRegions;

    private TextureRegion[] bigBlackSmokeTextureRegions;
    private TextureRegion[] smallBlackSmokeTextureRegions;

    private TextureRegion[] greenHealBarTextureRegions;
    private TextureRegion[] greenHealPaneTextureRegions;
    private TextureRegion[] redHealBarTextureRegions;
    private TextureRegion[] redHealPaneTextureRegions;
    private TextureRegion[] blueHealPaneTextureRegions;

    private TextureRegion[] artilleryTextureRegions;
    private TextureRegion[] mineTextureRegions;
    private TextureRegion[] dynamiteTextureRegions;
    private TextureRegion[] aircraftTextureRegions;

    private TextureRegion[] flagTextureRegions;
    private TextureRegion[] cloudTextureRegions;
    private TextureRegion[] palmTreeTextureRegions;

    private TextureRegion[] supportItemTextureRegions;
    private TextureRegion[] ringsTextureRegions;

    private TextureRegion[] verticalTrackTextureRegions;
    private TextureRegion[] horizontalTrackTextureRegions;
    private TextureRegion[] downLeftTrackTextureRegions;
    private TextureRegion[] leftUpTrackTextureRegions;
    private TextureRegion[] upRightTrackTextureRegions;
    private TextureRegion[] rightDownTrackTextureRegions;

    public void loadAsset() {
        assetManager = new AssetManager();

        assetManager.load(LIGHT_TANK_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(HEAVY_TANK_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(KAMIKAZE_TANK_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(SIEGE_TANK_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(TURRET_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(BULLET_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(SMALL_NORMAL_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(SMALL_PLASMA_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(BIG_NORMAL_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(BIG_PLASMA_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(BIG_RED_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(TRACKS_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(GREEN_HEAL_BAR_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(RED_HEAL_BAR_FILE_NAME, TextureAtlas.class);
        assetManager.load(GREEN_HEAL_PANE_FILE_NAME, TextureAtlas.class);
        assetManager.load(RED_HEAL_PANE_FILE_NAME, TextureAtlas.class);
        assetManager.load(BLUE_HEAL_PANE_FILE_NAME, TextureAtlas.class);
        assetManager.load(ICON_FILE_NAME, TextureAtlas.class);
        assetManager.load(ARTILLERY_FILE_NAME, TextureAtlas.class);
        assetManager.load(MINE_FILE_NAME, TextureAtlas.class);
        assetManager.load(DYNAMITE_FILE_NAME, TextureAtlas.class);
        assetManager.load(INFORMATION_PANEL_FILE_NAME, Texture.class);
        assetManager.load(AIRCRAFT_IMAGE_NAME, TextureAtlas.class);
        assetManager.load(TARGET_MARK_IMAGE_NAME, TextureAtlas.class);
        assetManager.load(CLOUD_IMAGES_FILE_NAME, TextureAtlas.class);
        assetManager.load(SUPPORT_ITEM_IMAGE_FILE_NAME, TextureAtlas.class);
        assetManager.load(RADAR_IMAGES_FILE_NAME, Texture.class);
        assetManager.load(NUCLEAR_IMAGES_FILE_NAME, Texture.class);
        assetManager.load(PALMS_IMAGE_FILE_NAME, TextureAtlas.class);
        assetManager.load(FLAG_IMAGE_FILE_NAME, TextureAtlas.class);
        assetManager.load(RINGS_IMAGE_FILE_NAME, TextureAtlas.class);

        assetManager.finishLoading();

        initAsset();
    }

    private void initAsset() {
        initWarMachineAsset();
        initBulletAsset();
        initSmokeAsset();
        initFlagAsset();
        initHealBarAndPaneAsset();
        initAreaWeaponAsset();
        initEnvironmentAsset();
        initSupportItemAsset();
        initRingsAsset();
        initTrackAsset();
    }

    private void initTrackAsset() {
        TextureAtlas trackTextureAtlas = assetManager.get(TRACKS_IMAGES_FILE_NAME, TextureAtlas.class);
        horizontalTrackTextureRegions = initAsset(trackTextureAtlas, 0, 3);
        verticalTrackTextureRegions = initAsset(trackTextureAtlas, 4, 7);
        downLeftTrackTextureRegions = initAsset(trackTextureAtlas, 8, 10);
        leftUpTrackTextureRegions = initAsset(trackTextureAtlas, 11, 13);
        upRightTrackTextureRegions = initAsset(trackTextureAtlas, 14, 16);
        rightDownTrackTextureRegions = initAsset(trackTextureAtlas, 17, 19);
    }

    private void initRingsAsset() {
        ringsTextureRegions = initAsset(assetManager.get(RINGS_IMAGE_FILE_NAME, TextureAtlas.class), 0, 4);
    }

    private void initSupportItemAsset() {
        supportItemTextureRegions = initAsset(assetManager.get(SUPPORT_ITEM_IMAGE_FILE_NAME, TextureAtlas.class), 0, 19);
    }

    private void initEnvironmentAsset() {
        cloudTextureRegions = initAsset(assetManager.get(CLOUD_IMAGES_FILE_NAME, TextureAtlas.class), 0, 5);
        palmTreeTextureRegions = initAsset(assetManager.get(PALMS_IMAGE_FILE_NAME, TextureAtlas.class), 0, 4);
    }

    private void initAreaWeaponAsset() {
        artilleryTextureRegions = initAsset(assetManager.get(ARTILLERY_FILE_NAME, TextureAtlas.class), 0, 4);
        mineTextureRegions = initAsset(assetManager.get(MINE_FILE_NAME, TextureAtlas.class), 0, 2);
        dynamiteTextureRegions = initAsset(assetManager.get(DYNAMITE_FILE_NAME, TextureAtlas.class), 0, 1);
        aircraftTextureRegions = initAsset(assetManager.get(AIRCRAFT_IMAGE_NAME, TextureAtlas.class), 0, 1);
    }

    private void initHealBarAndPaneAsset() {
        greenHealBarTextureRegions = initAsset(assetManager.get(GREEN_HEAL_BAR_IMAGES_FILE_NAME, TextureAtlas.class), 0, 11);
        redHealBarTextureRegions = initAsset(assetManager.get(RED_HEAL_BAR_FILE_NAME, TextureAtlas.class), 0, 11);

        greenHealPaneTextureRegions = initAsset(assetManager.get(GREEN_HEAL_PANE_FILE_NAME, TextureAtlas.class), 0, 1);
        redHealPaneTextureRegions = initAsset(assetManager.get(RED_HEAL_PANE_FILE_NAME, TextureAtlas.class), 0, 1);
        blueHealPaneTextureRegions = initAsset(assetManager.get(BLUE_HEAL_PANE_FILE_NAME, TextureAtlas.class), 0, 1);
    }

    private void initBulletAsset() {
        bulletTextureRegions = initAsset(assetManager.get(BULLET_IMAGES_FILE_NAME, TextureAtlas.class), 0, 15);
    }

    private void initFlagAsset() {
        flagTextureRegions = initAsset(assetManager.get(FLAG_IMAGE_FILE_NAME, TextureAtlas.class), 0, 2);
    }

    private void initSmokeAsset() {
        smallBlackSmokeTextureRegions = initAsset(assetManager.get(SMALL_NORMAL_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class), 8, 11);
        bigBlackSmokeTextureRegions = initAsset(assetManager.get(BIG_NORMAL_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class), 18, 21);
    }

    private void initWarMachineAsset() {
        lightTankTextureRegions = new TextureRegion[16];
        heavyTankTextureRegions = new TextureRegion[16];
        kamikazeTankTextureRegions = new TextureRegion[16];
        artilleryTankTextureRegions = new TextureRegion[16];
        turretTextureRegions = new TextureRegion[16];
        TextureAtlas lightTankTextureAtlas = assetManager.get(LIGHT_TANK_IMAGES_FILE_NAME, TextureAtlas.class);
        TextureAtlas heavyTankTextureAtlas = assetManager.get(HEAVY_TANK_IMAGES_FILE_NAME, TextureAtlas.class);
        TextureAtlas kamikazeTankTextureAtlas = assetManager.get(KAMIKAZE_TANK_IMAGES_FILE_NAME, TextureAtlas.class);
        TextureAtlas artilleryTankTextureAtlas = assetManager.get(ARTILLERY_FILE_NAME, TextureAtlas.class);
        TextureAtlas turretTextureAtlas = assetManager.get(TURRET_IMAGES_FILE_NAME, TextureAtlas.class);
        for (int i = 0; i < 16; i++) {
            String regionName = i + "";
            lightTankTextureRegions[i] = lightTankTextureAtlas.findRegion(regionName);
            heavyTankTextureRegions[i] = heavyTankTextureAtlas.findRegion(regionName);
            kamikazeTankTextureRegions[i] = kamikazeTankTextureAtlas.findRegion(regionName);
            artilleryTankTextureRegions[i] = artilleryTankTextureAtlas.findRegion(regionName);
            turretTextureRegions[i] = turretTextureAtlas.findRegion(regionName);
        }
    }

    private static TextureRegion[] initAsset(TextureAtlas textureAtlas, int fromIndex, int toIndex) {
        TextureRegion[] result = new TextureRegion[toIndex - fromIndex + 1];
        int i = 0;
        for (int index = fromIndex; index <= toIndex; index++) {
            result[i++] = textureAtlas.findRegion(index + "");
        }
        return result;
    }


    public TextureRegion[] getWarMachineImages(int type) {
        switch (type) {
            case WarMachine.LIGHT_TANK_TYPE: {
                return lightTankTextureRegions;
            }

            case WarMachine.HEAVY_TANK_TYPE: {
                return heavyTankTextureRegions;
            }

            case WarMachine.KAMIKAZE_TANK_TYPE: {
                return kamikazeTankTextureRegions;
            }

            case WarMachine.ARTILLERY_TANK_TYPE: {
                return artilleryTankTextureRegions;
            }

            case WarMachine.TURRET_TYPE: {
                return turretTextureRegions;
            }

            default: {
                break;
            }
        }
        return null;
    }

    public TextureRegion[] getBulletImages(int type, int orient) {
        TextureRegion[] result = new TextureRegion[1];
        switch (type) {
            case Bullet.NORMAL_BULLET:
            case Bullet.DOUBLE_NORMAL_BULLET: {
                result[0] = bulletTextureRegions[orient / 4];
            }
            break;

            case Bullet.PLASMA_BULLET:
            case Bullet.DOUBLE_PLASMA_BULLET: {
                result[0] = bulletTextureRegions[orient / 4 + 4];
            }
            break;

            case Bullet.HIGH_EXPLOSIVE_BULLET:
            case Bullet.ARMOR_PIERCING_BULLET: {
                result[0] = bulletTextureRegions[orient / 4 + 8];
            }
            break;

            case Bullet.MISSILE_BULLET: {
                result[0] = bulletTextureRegions[orient / 4 + 12];
            }
            break;

            default:{
                Gdx.app.log("BulletError", "type: "+type+" orient: "+orient);
                break;
            }
        }
        return result;
    }

    public TextureRegion[] getExplosionImages(int type) {
        switch (type) {
            case Explosion.SMALL_NORMAL_EXPLOSION: {
                return initAsset(assetManager.get(SMALL_NORMAL_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class), 0, 16);
            }

            case Explosion.SMALL_PLASMA_EXPLOSION: {
                return initAsset(assetManager.get(SMALL_PLASMA_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class), 0, 12);
            }

            case Explosion.BIG_NORMAL_EXPLOSION: {
                return initAsset(assetManager.get(BIG_NORMAL_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class), 0, 27);
            }

            case Explosion.BIG_RED_EXPLOSION: {
                return initAsset(assetManager.get(BIG_RED_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class), 0, 11);
            }

            case Explosion.BIG_PLASMA_EXPLOSION: {
                return initAsset(assetManager.get(BIG_PLASMA_EXPLOSION_IMAGES_FILE_NAME, TextureAtlas.class), 0, 11);
            }

            default: {
                return null;
            }
        }
    }

    public TextureRegion getRandomBigSmoke() {
        return bigBlackSmokeTextureRegions[MathUtils.random(3)];
    }

    public TextureRegion getRandomSmallSmoke() {
        return smallBlackSmokeTextureRegions[MathUtils.random(3)];
    }

    public TextureRegion[] getTracksImages() {
        return initAsset(assetManager.get(TRACKS_IMAGES_FILE_NAME, TextureAtlas.class), 0, 19);
    }

    public TextureRegion[] getHealBar(int teamId) {
        switch (teamId) {
            case DamagedEntity.ALLIES_TEAM: {
                return greenHealBarTextureRegions;
            }

            case DamagedEntity.ENEMIES_TEAM: {
                return redHealBarTextureRegions;
            }

            default: {
                return null;
            }
        }
    }

    public TextureRegion[] getPane(int type) {
        switch (type) {
            case Pane.GREEN_PANE: {
                return greenHealPaneTextureRegions;
            }

            case Pane.RED_PANE: {
                return redHealPaneTextureRegions;
            }

            case Pane.BLUE_PANE: {
                return blueHealPaneTextureRegions;
            }

            default: {
                return null;
            }
        }
    }

    public TextureRegion getInformationPanelImage() {
        return new TextureRegion(assetManager.get(INFORMATION_PANEL_FILE_NAME, Texture.class));
    }

    public TextureRegion[] getArtilleryImages() {
        return artilleryTextureRegions;
    }

    public TextureRegion[] getMineImage(int type, int teamId) {
        TextureRegion[] result = new TextureRegion[1];
        if (teamId == DamagedEntity.ENEMIES_TEAM) {
            result[0] = mineTextureRegions[0];
        } else {
            if (type == Mine.MINE_TMD_5) {
                result[0] = mineTextureRegions[1];
            } else {
                result[0] = mineTextureRegions[2];
            }
        }
        return result;
    }

    public TextureRegion[] getDynamiteImage() {
        return dynamiteTextureRegions;
    }

    public TextureRegion[] getAircraftImages() {
        return aircraftTextureRegions;
    }

    public TextureRegion[] getTargetMarkImages() {
        return initAsset(assetManager.get(TARGET_MARK_IMAGE_NAME, TextureAtlas.class), 0, 1);
    }

    public TextureRegion[] getRandomCloudImages() {
        TextureRegion[] result = new TextureRegion[2];
        int index = MathUtils.random(2) * 2;
        result[0] = cloudTextureRegions[index];
        result[1] = cloudTextureRegions[index + 1];
        return result;
    }

    public TextureRegion[] getPalmImages() {
        TextureRegion[] result = new TextureRegion[2];
        result[0] = palmTreeTextureRegions[MathUtils.random(2)];
        result[1] = palmTreeTextureRegions[MathUtils.random(3, 4)];
        return result;
    }

    public TextureRegion[] getSupportItemImages(int id) {
        TextureRegion[] result = null;
        switch (id) {
            case SupportItem.BOOST_SPEED: {
                result = new TextureRegion[3];
                result[0] = supportItemTextureRegions[0];
                result[1] = supportItemTextureRegions[1];
                result[2] = supportItemTextureRegions[2];
            }
            break;

            case SupportItem.LIFE_ITEM: {
                result = new TextureRegion[3];
                result[0] = supportItemTextureRegions[3];
                result[1] = supportItemTextureRegions[4];
                result[2] = supportItemTextureRegions[5];
            }
            break;

            case SupportItem.TIME_FREEZE: {
                result = new TextureRegion[3];
                result[0] = supportItemTextureRegions[6];
                result[1] = supportItemTextureRegions[7];
                result[2] = supportItemTextureRegions[8];
            }
            break;

            case SupportItem.REPAIR_KIT: {
                result = new TextureRegion[3];
                result[0] = supportItemTextureRegions[9];
                result[1] = supportItemTextureRegions[10];
                result[2] = supportItemTextureRegions[11];
            }
            break;

            case SupportItem.STAR_ITEM: {
                result = new TextureRegion[3];
                result[0] = supportItemTextureRegions[12];
                result[1] = supportItemTextureRegions[13];
                result[2] = supportItemTextureRegions[14];
            }
            break;

            case SupportItem.MONEY_ITEM: {
                result = new TextureRegion[3];
                result[0] = supportItemTextureRegions[15];
                result[1] = supportItemTextureRegions[16];
                result[2] = supportItemTextureRegions[17];
            }
            break;

            case SupportItem.BOX_ITEM: {
                result = new TextureRegion[1];
                result[0] = supportItemTextureRegions[MathUtils.random(18, 19)];
            }
            break;

            default: {
                break;
            }
        }
        return result;
    }

    public TextureRegion getRadarBuildingImage() {
        return new TextureRegion(assetManager.get(RADAR_IMAGES_FILE_NAME, Texture.class));
    }

    public TextureRegion getNuclearBuildingImage() {
        return new TextureRegion(assetManager.get(NUCLEAR_IMAGES_FILE_NAME, Texture.class));
    }

    public TextureRegion getFlagImage(int type) {
        return flagTextureRegions[type];
    }

    public TextureRegion[] getRingsImage(int type) {
        TextureRegion[] result = new TextureRegion[3];
        result[0] = ringsTextureRegions[type];
        result[1] = ringsTextureRegions[3];
        result[2] = ringsTextureRegions[4];
        return result;
    }

    public TextureRegion[] getTrackTextureRegions(int currentOrient, int nextOrient){
        int code = Integer.parseInt(currentOrient+""+nextOrient);
        switch (code){
            case 0:
            case 8:
            case 88:
            case 80:{
                return verticalTrackTextureRegions;
            }

            case 44:
            case 412:
            case 1212:
            case 124:{
                return horizontalTrackTextureRegions;
            }

            case 4:
            case 128:{
                return downLeftTrackTextureRegions;
            }

            case 48:
            case 12:{
                return leftUpTrackTextureRegions;
            }

            case 812:
            case 40:{
                return upRightTrackTextureRegions;
            }

            case 120:
            case 84:{
                return rightDownTrackTextureRegions;
            }

            default:{
                Gdx.app.log("ERROR", "Track Image Null "+code);
                return new TextureRegion[1];
            }
        }
    }

    public void dispose() {
        if (assetManager != null) {
            assetManager.dispose();
        }
        assetManager = null;
    }
}
