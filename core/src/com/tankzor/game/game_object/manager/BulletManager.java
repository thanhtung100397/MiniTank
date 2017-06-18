package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.movable_item.war_machine.OnWarMachineFiringListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.bot.BotArtilleryTank;
import com.tankzor.game.game_object.movable_item.war_machine.bot.BotKamikazeTank;
import com.tankzor.game.game_object.movable_item.war_machine.bot.BotTank;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Aircraft;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Artillery;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Dynamite;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Mine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Missile;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.game_object.support_item.BoostSpeedItem;
import com.tankzor.game.game_object.support_item.ForceField;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.game_object.support_item.TimeFrenzyItem;
import com.tankzor.game.gamehud.GameHUD;
import com.tankzor.game.gamehud.TargetMark;
import com.tankzor.game.gamehud.TouchScreenStage;

/**
 * Created by Admin on 11/16/2016.
 */

public class BulletManager extends Stage implements OnWarMachineFiringListener {
    private AirManager airManager;
    private GameHUD gameHUD;
    private TerrainManager terrainManager;
    private WarMachineManager warMachineManager;
    private LightingManager lightingManager;
    private ExplosionManager explosionManager;
    private AssetLoader assetLoader;

    private Pool<Bullet> bulletPool;

    public BulletManager(Viewport viewport, Batch batch, AssetLoader assetLoader) {
        super(viewport, batch);
        this.assetLoader = assetLoader;
        this.bulletPool = new Pool<Bullet>(16, 16) {
            @Override
            protected Bullet newObject() {
                return new Bullet(terrainManager, explosionManager, lightingManager, terrainManager, bulletPool);
            }
        };
//        getRoot().setCullingArea(((PlayerCamera) viewport.getCamera()).getCullingArea());
    }

    @Override
    public void clear() {
        super.clear();
        bulletPool.clear();
    }

    public void addBullet(Bullet... bullets) {
        int size = bullets.length;
        for (int i = 0; i < size; i++) {
            addActor(bullets[i]);
        }
    }

    public void setAirManager(AirManager airManager) {
        this.airManager = airManager;
    }

    public void setTerrainManager(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public void setWarMachineManager(WarMachineManager warMachineManager) {
        this.warMachineManager = warMachineManager;
    }

    public void setGameHUD(GameHUD gameHUD) {
        this.gameHUD = gameHUD;
    }

    public void setExplosionManager(ExplosionManager explosionManager) {
        this.explosionManager = explosionManager;
    }

    @Override
    public void onPlayerFired(PlayerWarMachine playerWarMachine) {
        WeaponManager weaponManager = playerWarMachine.getWeaponManager();
        WeaponManager.WeaponItem currentWeaponItem = weaponManager.getCurrentWeaponItem();
        WeaponModel weaponModel = currentWeaponItem.weaponModel;

        switch (weaponModel.id) {
            case Bullet.NORMAL_BULLET:
            case Bullet.PLASMA_BULLET:
            case Bullet.DOUBLE_NORMAL_BULLET:
            case Bullet.DOUBLE_PLASMA_BULLET:
            case Bullet.HIGH_EXPLOSIVE_BULLET:
            case Bullet.ARMOR_PIERCING_BULLET: {
                GameSounds.getInstance().playSFX(GameSounds.FIRE_SFX_ID);
                addBullet(new Bullet.BulletBuilder(playerWarMachine,
                                                    weaponModel.id,
                                                    weaponModel.damage,
                                                    bulletPool,
                                                    assetLoader).build());
                currentWeaponItem.decrease(1);
                PlayerProfile.getInstance().getMissionRecorder().shotCount += 1;
            }
            break;

            case Bullet.MISSILE_BULLET: {
                GameSounds.getInstance().playSFX(GameSounds.MISSILE_LAUNCH_SFX_ID);
                addBullet(new Missile(playerWarMachine,
                                        weaponModel.damage,
                                        weaponModel.explosion));
                currentWeaponItem.decrease(1);
                PlayerProfile.getInstance().getMissionRecorder().shotCount += 1;
            }
            break;

            case AreaWeapon.ARTILLERY: {
                GameSounds.getInstance().playSFX(GameSounds.ARTILLERY_LAUNCH_SFX_ID);
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                Artillery artillery = new Artillery(playerWarMachine,
                                                    weaponModel.damage,
                                                    weaponModel.explosion,
                                                    playerProfile.getArtillerySpeed(),
                                                    playerProfile.getArtilleryRange());
                addActor(artillery);
                currentWeaponItem.decrease(1);
                PlayerProfile.getInstance().getMissionRecorder().shotCount += 1;
            }
            break;

            case AreaWeapon.MINE_TMD_5:
            case AreaWeapon.MINE_TMD_9: {
                int iMine = (int) (playerWarMachine.getX() / GameEntity.ITEM_SIZE);
                int jMine = (int) (playerWarMachine.getY() / GameEntity.ITEM_SIZE);
                Terrain terrain = terrainManager.getTerrain(iMine, jMine);
                if (terrain.hasMineOn()) {
                    return;
                }
                terrainManager.getGroundWeaponManager()
                        .addMine(new Mine(playerWarMachine, weaponModel.id, weaponModel.damage));
                currentWeaponItem.decrease(1);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case AreaWeapon.DYNAMITE_X70:
            case AreaWeapon.DYNAMITE_X100:
            case AreaWeapon.DYNAMITE_X160: {
                Dynamite dynamite = new Dynamite(playerWarMachine, weaponModel.damage, weaponModel.explosion);
                terrainManager.getGroundWeaponManager().addDynamite(dynamite);
                currentWeaponItem.decrease(1);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case AreaWeapon.HOMING_MISSILE: {

            }
            break;

            case AreaWeapon.AIR_STRIKE: {
                warMachineManager.onFireButtonInputEvent(false);
                TouchScreenStage touchScreenStage = gameHUD.getTouchScreenStage();
                if (!touchScreenStage.isFocus()) {
                    touchScreenStage.getTargetMark().setType(TargetMark.TARGET_MARK_RED);
                    touchScreenStage.setWeaponItem(currentWeaponItem);
                    gameHUD.switchTouchScreen(true);
                }
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case SupportItem.REPAIR_KIT: {
                warMachineManager.onFireButtonInputEvent(false);
                TouchScreenStage touchScreenStage = gameHUD.getTouchScreenStage();
                if (!touchScreenStage.isFocus()) {
                    touchScreenStage.getTargetMark().setType(TargetMark.TARGET_MARK_GREEN);
                    touchScreenStage.setWeaponItem(currentWeaponItem);
                    gameHUD.switchTouchScreen(true);
                }
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case SupportItem.BOOST_SPEED: {
                BoostSpeedItem boostSpeedItem = new BoostSpeedItem(gameHUD.getPlayerWeaponMenu().getDisplayActiveItemPane().getSpeedCoolDownDisplay());
                terrainManager.addActor(boostSpeedItem);
                boostSpeedItem.addEffectTo(playerWarMachine);
                currentWeaponItem.decrease(1);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case SupportItem.TIME_FREEZE: {
                TimeFrenzyItem timeFrenzyItem = new TimeFrenzyItem(gameHUD.getPlayerWeaponMenu().getDisplayActiveItemPane().getFreezeCoolDownDisplay());
                terrainManager.addActor(timeFrenzyItem);
                timeFrenzyItem.addEffectTo(playerWarMachine);
                currentWeaponItem.decrease(1);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case SupportItem.FORCE_FIELD: {
                ForceField forceField = playerWarMachine.getForceField();
                int maxForceField = forceField.getMaximumNumber();
                int maximumForceField = PlayerProfile.getInstance().getMaximumNumberOfForceField();
                int delta = maximumForceField - maxForceField;
                if (delta == 0) {
                    return;
                }
                int consumeCount;
                if (weaponModel.capacity < delta) {
                    consumeCount = weaponModel.capacity;
                } else {
                    consumeCount = delta;
                }
                forceField.addMaximumNumber(consumeCount);
                PlayerProfile.getInstance().addForceField(consumeCount);
                currentWeaponItem.decrease(consumeCount);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case SupportItem.ALLY_TANK: {
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                BotTank botTank = new BotTank(playerWarMachine.getSpawnLocation(),
                                                playerProfile.getAllyTankType(),
                                                DamagedEntity.ALLIES_TEAM,
                                                playerProfile.getAllyTankHitPoint(),
                                                playerProfile.getAllyTankSpeed(),
                                                terrainManager,
                                                terrainManager.getExplosionManager(),
                                                terrainManager,
                                                warMachineManager.getAirManager(),
                                                warMachineManager.getLightingManager(),
                                                warMachineManager.getAssetLoader(),
                                                warMachineManager.getTracksManager(),
                                                null,
                                                15,
                                                terrainManager,
                                                warMachineManager.getBulletManager());
                botTank.getForceField().addMaximumNumber(playerProfile.getAllyTankForceField());
                botTank.setSupportTarget(playerWarMachine);
                botTank.setBaseWeaponModel(weaponManager.getBestBulletModel());
                playerWarMachine.getSpawnLocation().spawnWarMachine(botTank);
                currentWeaponItem.decrease(1);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case SupportItem.ALLY_KAMIKAZE_TANK: {
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                BotKamikazeTank botKamikazeTank = new BotKamikazeTank(playerWarMachine.getSpawnLocation(),
                                                                        DamagedEntity.ALLIES_TEAM,
                                                                        playerProfile.getAllyKamikazeTankHitPoint(),
                                                                        playerProfile.getAllyKamikazeTankSpeed(),
                                                                        warMachineManager.getTerrainManager(),
                                                                        warMachineManager.getExplosionManager(),
                                                                        warMachineManager.getLightingManager(),
                                                                        terrainManager,
                                                                        warMachineManager.getAirManager(),
                                                                        warMachineManager.getAssetLoader(),
                                                                        null,
                                                                        15,
                                                                        terrainManager);
                WeaponModel wm = new WeaponModel();
                weaponModel.explosion = playerProfile.getAllyKamikazeTankExplosion();
                weaponModel.damage = playerProfile.getAllyKamikazeTankDamage();
                botKamikazeTank.setBaseWeaponModel(wm);
                botKamikazeTank.setSupportTarget(playerWarMachine);
                playerWarMachine.getSpawnLocation().spawnWarMachine(botKamikazeTank);
                currentWeaponItem.decrease(1);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            case SupportItem.ALLY_ARTILLERY_TANK: {
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                BotArtilleryTank botArtilleryTank = new BotArtilleryTank(playerWarMachine.getSpawnLocation(),
                        DamagedEntity.ALLIES_TEAM,
                        playerProfile.getAllyArtilleryTankHitPoint(),
                        playerProfile.getAllyArtilleryTankSpeed(),
                        warMachineManager.getTerrainManager(),
                        warMachineManager.getExplosionManager(),
                        terrainManager,
                        warMachineManager.getAirManager(),
                        warMachineManager.getLightingManager(),
                        warMachineManager.getAssetLoader(),
                        warMachineManager.getTracksManager(),
                        null,
                        15,
                        terrainManager,
                        warMachineManager.getBulletManager());
                botArtilleryTank.getForceField().addMaximumNumber(playerProfile.getAllyArtilleryTankForceField());
                botArtilleryTank.setBaseWeaponModel(weaponManager.getBestBulletModel());
                botArtilleryTank.setSupportTarget(playerWarMachine);
                WeaponModel playerArtilleryModel = playerProfile.getWeaponModel(AreaWeapon.ARTILLERY);
                if (playerArtilleryModel.unlocked) {
                    botArtilleryTank.setArtilleryModel(playerArtilleryModel,
                            playerProfile.getArtilleryRange(),
                            playerProfile.getArtillerySpeed());
                }
                playerWarMachine.getSpawnLocation().spawnWarMachine(botArtilleryTank);
                currentWeaponItem.decrease(1);
                warMachineManager.getPlayerWarMachine().fireButtonPress = false;
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void onBotFired(WarMachine warMachine) {
        WeaponModel weaponModel = warMachine.getBaseWeaponModel();
        switch (weaponModel.id) {
            case Bullet.NORMAL_BULLET:
            case Bullet.PLASMA_BULLET:
            case Bullet.DOUBLE_NORMAL_BULLET:
            case Bullet.DOUBLE_PLASMA_BULLET:
            case Bullet.HIGH_EXPLOSIVE_BULLET:
            case Bullet.ARMOR_PIERCING_BULLET: {
                GameSounds.getInstance().playSFX(GameSounds.FIRE_SFX_ID);
                addBullet(new Bullet.BulletBuilder(warMachine,
                                                    weaponModel.id,
                                                    weaponModel.damage,
                                                    bulletPool,
                                                    assetLoader).build());
            }
            break;

            case Bullet.MISSILE_BULLET: {
                GameSounds.getInstance().playSFX(GameSounds.MISSILE_LAUNCH_SFX_ID);
                addBullet(new Missile(warMachine, weaponModel.damage, weaponModel.explosion));
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void onFiredAreaWeapon(AreaWeapon areaWeapon) {
        if (areaWeapon instanceof Artillery) {
            GameSounds.getInstance().playSFX(GameSounds.ARTILLERY_LAUNCH_SFX_ID);
            addActor(areaWeapon);
        } else if (areaWeapon instanceof Aircraft) {
            airManager.addFlyingItem(areaWeapon);
        }
    }

    public void setLightingManager(LightingManager lightingManager) {
        this.lightingManager = lightingManager;
    }
}
