package com.tankzor.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.ConditionRound;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.RemoteTrigger;
import com.tankzor.game.game_object.Round;
import com.tankzor.game.game_object.RoundSet;
import com.tankzor.game.game_object.SpawnLocation;
import com.tankzor.game.game_object.game_mode.Flag;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.BulletManager;
import com.tankzor.game.game_object.manager.ExplosionManager;
import com.tankzor.game.game_object.manager.FlagManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.bot.Turret;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Aircraft;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Bomb;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Dynamite;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Mine;
import com.tankzor.game.game_object.support_item.BoostSpeedItem;
import com.tankzor.game.game_object.support_item.BoxItem;
import com.tankzor.game.game_object.support_item.LifeItem;
import com.tankzor.game.game_object.support_item.MoneyItem;
import com.tankzor.game.game_object.support_item.RepairItem;
import com.tankzor.game.game_object.support_item.StarItem;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.game_object.support_item.TimeFrenzyItem;
import com.tankzor.game.game_object.support_item.TrapBoxItem;
import com.tankzor.game.ui.ListMissionScreen;

import java.io.IOException;

/**
 * Created by Admin on 12/22/2016.
 */

public class XmlMapReader {
    private static final String REMOTE = "remote";
    private static final String ALLIES = "allies";
    private static final String PLAYER = "player";
    private static final String ENEMIES = "enemies";
    private static final String TURRETS = "turrets";
    private static final String BUILDINGS = "buildings";
    private static final String ITEM = "item";
    private static final String WIN_CONDITION = "win";
    private static final String DAY_MODE = "dayMode";

    private static final String ROUND = "round";
    private static final String CONDITION_ROUND = "conditionRound";
    private static final String BUILDING = "building";
    private static final String EXPLOSION = "explosion";
    private static final String AIRCRAFT = "aircraft";
    private static final String PALM = "palm";
    private static final String BULLET = "bullet";
    private static final String ARTILLERY = "artillery";
    private static final String MINE = "mine";

    private static final String FLAG = "flag";

    private static final String LEVEL_ATTRIBUTE = "level";
    private static final String DELAY_ATTRIBUTE = "delay";
    private static final String DAMAGE_ATTRIBUTE = "damage";
    private static final String RELOAD_TIME_ATTRIBUTE = "reloadTime";
    private static final String BOMB_ATTRIBUTE = "bomb";

    private static final String NAME_ATTRIBUTE = "name";
    private static final String HINT_ATTRIBUTE = "hint";
    private static final String OBJECTIVE_ATTRIBUTE = "objective";

    private static final String WEAPON_BOX = "weaponBox";

    private static final String SPAWN_LOCATION = "spawnLocation";
    private static final String I_ATTRIBUTE = "i";
    private static final String J_ATTRIBUTE = "j";

    private static final String MAX_SPAWN_COUNT_ATTRIBUTE = "maxSpawnCount";
    private static final String COUNT_ATTRIBUTE = "count";
    private static final String IS_SUPPORTER_ATTRIBUTE = "isSupporter";
    private static final String ENABLE_LIGHT_ATTRIBUTE = "enableLight";

    private static final String CONTROL_AREA = "area";
    private static final String LOCATION_ITEM = "location";

    private static final String WAR_MACHINE = "warMachine";
    private static final String FORCE_FIELD = "forceField";
    private static final String MACHINE_ITEM = "machine";
    private static final String TYPE_ATTRIBUTE = "type";
    private static final String OPTION_ATTRIBUTE = "option";
    private static final String HP_ATTRIBUTE = "hp";
    private static final String SPEED_ATTRIBUTE = "speed";
    private static final String MONEY_ATTRIBUTE = "money";
    private static final String BULLET_TYPE_ATTRIBUTE = "bulletType";
    private static final String RANGE_ATTRIBUTE = "range";
    private static final String FORCE_FIELD_RECOVER_TIME_ATTRIBUTE = "regenTime";

    private static final String ORIENT_ATTRIBUTE = "orient";
    private static final String MESSAGE_ATTRIBUTE = "message";

    private WarMachineManager warMachineManager;
    private TerrainManager terrainManager;
    private ExplosionManager explosionManager;
    private BulletManager bulletManager;
    private LightingManager lightingManager;
    private AirManager airManager;
    private XmlReader mapReader;
    private AssetLoader assetLoader;

    public XmlMapReader(TerrainManager terrainManager,
                        ExplosionManager explosionManager,
                        WarMachineManager warMachineManager,
                        BulletManager bulletManager,
                        LightingManager lightingManager,
                        AirManager airManager,
                        AssetLoader assetLoader) {
        this.terrainManager = terrainManager;
        this.explosionManager = explosionManager;
        this.warMachineManager = warMachineManager;
        this.bulletManager = bulletManager;
        this.lightingManager = lightingManager;
        this.airManager = airManager;
        this.assetLoader = assetLoader;
        this.mapReader = new XmlReader();
    }

    public static Array<ListMissionScreen.MissionItem> readListMissionXml() {
        Array<ListMissionScreen.MissionItem> result = null;
        XmlReader reader = new XmlReader();
        try {
            XmlReader.Element root = reader.parse(Gdx.files.internal("maps/list_mission.xml"));
            Array<XmlReader.Element> elements = root.getChildrenByName("item");
            int size = elements.size;
            result = new Array<ListMissionScreen.MissionItem>();
            for (int i = 0; i < size; i++) {
                result.add(new ListMissionScreen
                        .MissionItem(elements.get(i).getAttribute(NAME_ATTRIBUTE),
                        elements.get(i).getAttribute(HINT_ATTRIBUTE),
                        elements.get(i).getAttribute(OBJECTIVE_ATTRIBUTE)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void parseOnlineMatchXml(String filename, int index){
        try {
            XmlReader.Element root = mapReader.parse(Gdx.files.internal(filename));

            lightingManager.setDayMode(getDayMode(root));

            Array<XmlReader.Element> elements = root.getChildByName(PLAYER).getChildrenByName(SPAWN_LOCATION);
            warMachineManager.initPlayerSpawnLocation(getLocation(elements.get(index)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseCampaignXml(String fileName) {
        try {
            XmlReader.Element root = mapReader.parse(Gdx.files.internal(fileName));

            lightingManager.setDayMode(getDayMode(root));

            createGameMode(root);

            createPalmsTree(root);

            createFlags(root.getChildByName(WIN_CONDITION));

            XmlReader.Element remoteElement = root.getChildByName(REMOTE);
            if (remoteElement != null) {
                setupRemoteExplosion(remoteElement);
                setupRemoteAirStrike(remoteElement);
            }

            XmlReader.Element enemiesElement = root.getChildByName(ENEMIES);
            if (enemiesElement != null) {
                createRoundSet(enemiesElement, warMachineManager.getEnemySpawnRoundSet(), DamagedEntity.ENEMIES_TEAM);
                createTurret(enemiesElement, DamagedEntity.ENEMIES_TEAM);
                createBuildings(enemiesElement, DamagedEntity.ENEMIES_TEAM);
                createMines(enemiesElement, DamagedEntity.ENEMIES_TEAM);
            }
            warMachineManager.calculateCurrentEnemiesLeft();

            warMachineManager.initPlayerSpawnLocation(getLocation(root.getChildByName(PLAYER).getChildByName(SPAWN_LOCATION)));

            XmlReader.Element alliesElement = root.getChildByName(ALLIES);
            if (alliesElement != null) {
                createRoundSet(alliesElement, warMachineManager.getAlliesSpawnRoundSet(), DamagedEntity.ALLIES_TEAM);
                createTurret(alliesElement, DamagedEntity.ALLIES_TEAM);
                createBuildings(alliesElement, DamagedEntity.ALLIES_TEAM);
                createMines(alliesElement, DamagedEntity.ALLIES_TEAM);
            }

            createItem(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getDayMode(XmlReader.Element rootElement) {
        return Integer.parseInt(rootElement.getChildByName(DAY_MODE).getAttribute(TYPE_ATTRIBUTE));
    }

    private void createMines(XmlReader.Element parent, int teamId) {
        Array<XmlReader.Element> listMineElements = parent.getChildrenByName(MINE);
        for (XmlReader.Element mineElement : listMineElements) {
            GridPoint2 location = getLocation(mineElement);
            int mineType = Integer.parseInt(mineElement.getAttribute(TYPE_ATTRIBUTE));
            int mineDamage = Integer.parseInt(mineElement.getAttribute(DAMAGE_ATTRIBUTE));
            terrainManager.getGroundWeaponManager().addMine(new Mine(location.x * GameEntity.ITEM_SIZE,
                    location.y * GameEntity.ITEM_SIZE,
                    teamId,
                    mineType,
                    mineDamage,
                    explosionManager,
                    terrainManager,
                    assetLoader));
        }
    }

    private void setupRemoteAirStrike(XmlReader.Element parent) {
        Array<XmlReader.Element> explosionElements = parent.getChildrenByName(AIRCRAFT);
        for (XmlReader.Element child : explosionElements) {
            final GridPoint2 location = getLocation(child);
            final int level = Integer.parseInt(child.getAttribute(LEVEL_ATTRIBUTE));
            final int damage = Integer.parseInt(child.getAttribute(DAMAGE_ATTRIBUTE));
            final int bombCapacity = Integer.parseInt(child.getAttribute(BOMB_ATTRIBUTE));
            final Array<GridPoint2> listLocationListening = getLocations(child);
            new RemoteTrigger(listLocationListening, terrainManager) {
                @Override
                public void onItemEntered(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {
                    terrainManager.getAirManager()
                            .addFlyingItem(new Aircraft(location.x * GameEntity.ITEM_SIZE, location.y * GameEntity.ITEM_SIZE,
                                                        damage,
                                                        level,
                                                        bombCapacity,
                                                        explosionManager,
                                                        terrainManager,
                                                        lightingManager,
                                                        assetLoader));
                    remove();
                }
            };
        }
    }

    private void setupRemoteExplosion(XmlReader.Element parent) {
        Array<XmlReader.Element> explosionElements = parent.getChildrenByName(EXPLOSION);
        for (XmlReader.Element child : explosionElements) {
            final GridPoint2 location = getLocation(child);
            final int level = Integer.parseInt(child.getAttribute(LEVEL_ATTRIBUTE));
            final int damage = Integer.parseInt(child.getAttribute(DAMAGE_ATTRIBUTE));
            final boolean isMassive = Integer.parseInt(child.getAttribute(TYPE_ATTRIBUTE)) == 1;
            final Array<GridPoint2> listLocationListening = getLocations(child);
            new RemoteTrigger(listLocationListening, terrainManager){
                @Override
                public void onItemEntered(Terrain.TerrainObserverHolder observerHolder, WarMachine warMachine) {
                    terrainManager.getExplosionManager()
                            .onAreaWeaponDestroyed(new Bomb(location.x * GameEntity.ITEM_SIZE, location.y * GameEntity.ITEM_SIZE,
                                                            damage,
                                                            level,
                                                            explosionManager,
                                                            terrainManager,
                                                            lightingManager,
                                                            isMassive));
                    remove();
                }
            };
        }
    }

    private void createTurret(XmlReader.Element parent, int teamID) {
        XmlReader.Element turretsElement = parent.getChildByName(TURRETS);
        if (turretsElement == null) {
            return;
        }
        Array<XmlReader.Element> turretElements = turretsElement.getChildrenByName(MACHINE_ITEM);
        for (int i = 0; i < turretElements.size; i++) {
            XmlReader.Element turretElement = turretElements.get(i);
            GridPoint2 location = getLocation(turretElement);
            int orient = Integer.parseInt(turretElement.getAttribute(ORIENT_ATTRIBUTE));
            int bulletHeight = Integer.parseInt(turretElement.getAttribute(TYPE_ATTRIBUTE));
            int hitPoint = Integer.parseInt(turretElement.getAttribute(HP_ATTRIBUTE));
            WeaponModel weaponModel = getBulletModel(turretElement);
            int rangeSize = Integer.parseInt(turretElement.getAttribute(RANGE_ATTRIBUTE));
            warMachineManager.addTurret(new Turret(location.x * GameEntity.ITEM_SIZE,
                                                    location.y * GameEntity.ITEM_SIZE,
                                                    teamID,
                                                    hitPoint,
                                                    terrainManager,
                                                    explosionManager,
                                                    lightingManager,
                                                    terrainManager,
                                                    airManager,
                                                    assetLoader,
                                                    bulletManager,
                                                    rangeSize,
                                                    bulletHeight,
                                                    orient,
                                                    weaponModel));
        }
    }

    private void createBuildings(XmlReader.Element parent, int teamId) {
        XmlReader.Element buildingsElement = parent.getChildByName(BUILDINGS);
        if (buildingsElement == null) {
            return;
        }
        Array<XmlReader.Element> buildingElements = buildingsElement.getChildrenByName(BUILDING);
        for (XmlReader.Element child : buildingElements) {
            int type = Integer.parseInt(child.getAttribute(TYPE_ATTRIBUTE));
            GridPoint2 location = getLocation(child);
            int hitPoint = Integer.parseInt(child.getAttribute(HP_ATTRIBUTE));
            warMachineManager.addBuilding(type, location, hitPoint, teamId);
        }
    }

    private void createFlags(XmlReader.Element parent){
        Array<XmlReader.Element> listFlagElements = parent.getChildrenByName(FLAG);
        Array<Flag.FlagModel> listFlagModels = new Array<Flag.FlagModel>();
        for (XmlReader.Element anFlagElement : listFlagElements) {
            listFlagModels.add(new Flag.FlagModel(Integer.parseInt(anFlagElement.getAttribute(I_ATTRIBUTE)),
                    Integer.parseInt(anFlagElement.getAttribute(J_ATTRIBUTE)),
                    Integer.parseInt(anFlagElement.getAttribute(TYPE_ATTRIBUTE))));
        }
        FlagManager flagManager = terrainManager.getFlagManager();
        for (int i = 0; i < listFlagModels.size; i++) {
            Flag.FlagModel model = listFlagModels.get(i);
            flagManager.addFlagOnTerrain(model.i, model.j, model.type, true);
        }
    }

    private void createGameMode(XmlReader.Element root) {
        XmlReader.Element element = root.getChildByName(WIN_CONDITION);
        int mode = Integer.parseInt(element.getAttribute(TYPE_ATTRIBUTE));
        switch (mode) {
            case TerrainManager.CAPTURE_FLAG: {
                terrainManager.activeCaptureFlagGameMode();

            }
            break;

            case TerrainManager.DEATH_MATCH: {
                terrainManager.activeDeathMatchGameMode();
            }
            break;

            case TerrainManager.ATK_DEF_HQ: {
                terrainManager.activeDefenceHQGameMode();
            }

            default: {
                break;
            }
        }
        terrainManager.setAdditionRequirement(Integer.parseInt(element.getAttribute(OPTION_ATTRIBUTE)));
    }

    private void createItem(XmlReader.Element root) {
        Array<XmlReader.Element> listItemElement = root.getChildrenByName(ITEM);
        for (XmlReader.Element anItemElement : listItemElement) {
            solveItem(anItemElement);
        }
    }

    private void solveItem(XmlReader.Element item) {
        int type = Integer.parseInt(item.getAttribute(TYPE_ATTRIBUTE));
        float x = Integer.parseInt(item.getAttribute(I_ATTRIBUTE)) * GameEntity.ITEM_SIZE;
        float y = Integer.parseInt(item.getAttribute(J_ATTRIBUTE)) * GameEntity.ITEM_SIZE;
        boolean enableLight = Integer.parseInt(item.getAttribute(ENABLE_LIGHT_ATTRIBUTE)) == 1;
        switch (type) {
            case SupportItem.REPAIR_KIT: {
                terrainManager.addSupportItem(new RepairItem(x, y, enableLight ? lightingManager : null, assetLoader));
            }
            break;

            case SupportItem.LIFE_ITEM: {
                terrainManager.addSupportItem(new LifeItem(x, y, enableLight ? lightingManager : null, assetLoader));
            }
            break;

            case SupportItem.TIME_FREEZE: {
                terrainManager.addSupportItem(new TimeFrenzyItem(x, y, terrainManager.getGameHUD()
                        .getPlayerWeaponMenu().getDisplayActiveItemPane().getFreezeCoolDownDisplay(),
                        enableLight ? lightingManager : null,
                        assetLoader));
            }
            break;

            case SupportItem.MONEY_ITEM: {
                terrainManager.addSupportItem(new MoneyItem(x, y, enableLight ? lightingManager : null, assetLoader));
            }
            break;

            case SupportItem.BOOST_SPEED: {
                terrainManager.addSupportItem(new BoostSpeedItem(x, y, terrainManager.getGameHUD()
                        .getPlayerWeaponMenu().getDisplayActiveItemPane().getSpeedCoolDownDisplay(),
                        enableLight ? lightingManager : null,
                        assetLoader));
            }
            break;

            case SupportItem.STAR_ITEM: {
                terrainManager.addSupportItem(new StarItem(x, y, enableLight ? lightingManager : null, assetLoader));
            }
            break;

            case SupportItem.BOX_ITEM: {
                String message = item.getAttribute(MESSAGE_ATTRIBUTE);
                XmlReader.Element weaponItem = item.getChildByName(WEAPON_BOX);
                int weaponType;
                int capacity;
                if (weaponItem != null) {
                    weaponType = Integer.parseInt(weaponItem.getAttribute(TYPE_ATTRIBUTE));
                    capacity = Integer.parseInt(weaponItem.getAttribute(COUNT_ATTRIBUTE));
                } else {
                    weaponType = -1;
                    capacity = 0;
                }
                terrainManager.addSupportItem(new BoxItem(x, y, weaponType, capacity, message, assetLoader));
            }
            break;

            case SupportItem.TRAP_BOX_ITEM: {
                String message = item.getAttribute(MESSAGE_ATTRIBUTE);
                int damage = Integer.parseInt(item.getAttribute(DAMAGE_ATTRIBUTE));
                int level = Integer.parseInt(item.getAttribute(LEVEL_ATTRIBUTE));
                float delay = Float.parseFloat(item.getAttribute(DELAY_ATTRIBUTE));
                Dynamite dynamite = new Dynamite(x, y, damage, level, delay, explosionManager, terrainManager, lightingManager, assetLoader);
                terrainManager.addSupportItem(new TrapBoxItem(x, y, dynamite, terrainManager.getGroundWeaponManager(), message, assetLoader));
            }
            break;

            default: {
                break;
            }
        }
    }

    private void createRoundSet(XmlReader.Element parent, RoundSet storeRoundSet, int teamId) {
        storeRoundSet.clear();
        createRounds(parent, storeRoundSet, teamId);
        createConditionRounds(parent, storeRoundSet, teamId);
    }

    private void createRounds(XmlReader.Element parent, RoundSet storeRoundSet, int teamId) {
        Array<XmlReader.Element> listRoundElements = parent.getChildrenByName(ROUND);
        Array<XmlReader.Element> listSpawnElements;
        Round round;

        if (teamId == DamagedEntity.ALLIES_TEAM) {
            PlayerWarMachine supportedWarMachine = warMachineManager.getPlayerSpawnLocation().getPlayerWarMachine();
            for (XmlReader.Element roundElement : listRoundElements) {
                round = new Round();
                listSpawnElements = roundElement.getChildrenByName(SPAWN_LOCATION);
                for (XmlReader.Element spawnLocationElement : listSpawnElements) {
                    SpawnLocation spawnLocation =
                            new SpawnLocation(getLocation(spawnLocationElement),
                                            DamagedEntity.ALLIES_TEAM,
                                            Integer.parseInt(spawnLocationElement.getAttribute(MAX_SPAWN_COUNT_ATTRIBUTE)),
                                            warMachineManager,
                                            spawnLocationElement.getAttribute(ENABLE_LIGHT_ATTRIBUTE).equals("1"));
                    spawnLocation.setListWarMachineModels(getWarMachineModels(spawnLocationElement));
                    if (spawnLocationElement.getAttribute(IS_SUPPORTER_ATTRIBUTE).equals("0")) {
                        spawnLocation.setControlAreas(getControlArea(spawnLocationElement));
                    } else {
                        spawnLocation.setSupportWarMachine(supportedWarMachine);
                    }
                    round.addSpawnLocation(spawnLocation);
                }
                storeRoundSet.addRound(round);
            }
        } else if (teamId == DamagedEntity.ENEMIES_TEAM) {
            for (XmlReader.Element roundElement : listRoundElements) {
                round = new Round();
                round.setParent(storeRoundSet);
                listSpawnElements = roundElement.getChildrenByName(SPAWN_LOCATION);
                for (XmlReader.Element spawnLocationElement : listSpawnElements) {
                    SpawnLocation spawnLocation =
                            new SpawnLocation(getLocation(spawnLocationElement),
                                            DamagedEntity.ENEMIES_TEAM,
                                            Integer.parseInt(spawnLocationElement.getAttribute(MAX_SPAWN_COUNT_ATTRIBUTE)),
                                            warMachineManager,
                                            spawnLocationElement.getAttribute(ENABLE_LIGHT_ATTRIBUTE).equals("1"));
                    spawnLocation.setListWarMachineModels(getWarMachineModels(spawnLocationElement));
                    spawnLocation.setControlAreas(getControlArea(spawnLocationElement));
                    round.addSpawnLocation(spawnLocation);
                }
                storeRoundSet.addRound(round);
            }
        }
    }

    private void createConditionRounds(XmlReader.Element parent, RoundSet storeRoundSet, int teamId) {
        Array<XmlReader.Element> listRoundElements = parent.getChildrenByName(CONDITION_ROUND);
        Array<XmlReader.Element> listSpawnElements;
        ConditionRound round;
        MovableWarMachine supportedWarMachine = null;
        if (teamId == DamagedEntity.ALLIES_TEAM) {
            supportedWarMachine = warMachineManager.getPlayerSpawnLocation().getPlayerWarMachine();
        }
        for (XmlReader.Element roundElement : listRoundElements) {
            round = new ConditionRound(getLocations(roundElement), terrainManager);
            round.setParent(storeRoundSet);
            listSpawnElements = roundElement.getChildrenByName(SPAWN_LOCATION);
            for (XmlReader.Element spawnLocationElement : listSpawnElements) {
                SpawnLocation spawnLocation =
                        new SpawnLocation(getLocation(spawnLocationElement),
                                        teamId,
                                        Integer.parseInt(spawnLocationElement.getAttribute(MAX_SPAWN_COUNT_ATTRIBUTE)),
                                        warMachineManager,
                                        spawnLocationElement.getAttribute(ENABLE_LIGHT_ATTRIBUTE).equals("1"));
                spawnLocation.setListWarMachineModels(getWarMachineModels(spawnLocationElement));
                spawnLocation.setControlAreas(getControlArea(spawnLocationElement));
                spawnLocation.setSupportWarMachine(supportedWarMachine);
                round.addSpawnLocation(spawnLocation);
            }
            storeRoundSet.addConditionRound(round);
        }
    }

    private void createPalmsTree(XmlReader.Element root) {
        Array<XmlReader.Element> palmElements = root.getChildrenByName(PALM);
        for (int i = palmElements.size - 1; i >= 0; i--) {
            GridPoint2 location = getLocation(palmElements.get(i));
            terrainManager.getAirManager().createPalmTree(location.x, location.y);
        }
    }

    private GridPoint2 getLocation(XmlReader.Element element) {
        int i = Integer.parseInt(element.getAttribute(I_ATTRIBUTE));
        int j = Integer.parseInt(element.getAttribute(J_ATTRIBUTE));
        return new GridPoint2(i, j);
    }

    private Array<GridPoint2> getLocations(XmlReader.Element parent) {
        Array<XmlReader.Element> listElements = parent.getChildrenByName(LOCATION_ITEM);
        Array<GridPoint2> result = new Array<GridPoint2>();
        for (XmlReader.Element child : listElements) {
            int i = Integer.parseInt(child.getAttribute(I_ATTRIBUTE));
            int j = Integer.parseInt(child.getAttribute(J_ATTRIBUTE));
            result.add(new GridPoint2(i, j));
        }
        return result;
    }

    private Array<GridPoint2> getControlArea(XmlReader.Element parent) {
        Array<XmlReader.Element> listControlAreas = parent.getChildByName(CONTROL_AREA).getChildrenByName(LOCATION_ITEM);
        Array<GridPoint2> result = new Array<GridPoint2>();
        for (XmlReader.Element controlArea : listControlAreas) {
            result.add(getLocation(controlArea));
        }
        return result;
    }

    private Array<SpawnLocation.WarMachineModel> getWarMachineModels(XmlReader.Element parent) {
        Array<XmlReader.Element> listWarMachineModels = parent.getChildByName(WAR_MACHINE).getChildrenByName(MACHINE_ITEM);
        Array<SpawnLocation.WarMachineModel> result = new Array<SpawnLocation.WarMachineModel>();
        for (XmlReader.Element model : listWarMachineModels) {
            SpawnLocation.WarMachineModel warMachineModel = new SpawnLocation.WarMachineModel();
            warMachineModel.id = Integer.parseInt(model.getAttribute(TYPE_ATTRIBUTE));
            warMachineModel.hitPoint = Integer.parseInt(model.getAttribute(HP_ATTRIBUTE));
            warMachineModel.speed = Float.parseFloat(model.getAttribute(SPEED_ATTRIBUTE));
            warMachineModel.count = Integer.parseInt(model.getAttribute(COUNT_ATTRIBUTE));
            warMachineModel.money = Integer.parseInt(model.getAttribute(MONEY_ATTRIBUTE));
            warMachineModel.range = Integer.parseInt(model.getAttribute(RANGE_ATTRIBUTE));
            XmlReader.Element forceFiledElement = model.getChildByName(FORCE_FIELD);
            if (forceFiledElement != null) {
                warMachineModel.maxForceField = Integer.parseInt(forceFiledElement.getAttribute(COUNT_ATTRIBUTE));
                warMachineModel.forceFieldRecoverTime = Float.parseFloat(forceFiledElement.getAttribute(FORCE_FIELD_RECOVER_TIME_ATTRIBUTE));
            }
            if (warMachineModel.id != WarMachine.KAMIKAZE_TANK_TYPE) {
                warMachineModel.bulletModel = getBulletModel(model);
                if (warMachineModel.id == WarMachine.ARTILLERY_TANK_TYPE) {
                    addArtilleryModel(model, warMachineModel);
                }
            }
            result.add(warMachineModel);
        }
        return result;
    }

    private WeaponModel getBulletModel(XmlReader.Element ownerElement) {
        XmlReader.Element bulletElement = ownerElement.getChildByName(BULLET);
        WeaponModel weaponModel = new WeaponModel();
        weaponModel.id = Integer.parseInt(bulletElement.getAttribute(BULLET_TYPE_ATTRIBUTE));
        weaponModel.damage = Integer.parseInt(bulletElement.getAttribute(DAMAGE_ATTRIBUTE));
        weaponModel.reloadTime = Float.parseFloat(bulletElement.getAttribute(RELOAD_TIME_ATTRIBUTE));
        return weaponModel;
    }

    private void addArtilleryModel(XmlReader.Element ownerElement, SpawnLocation.WarMachineModel container) {
        XmlReader.Element artilleryElement = ownerElement.getChildByName(ARTILLERY);
        WeaponModel artilleryModel = new WeaponModel();
        artilleryModel.id = AreaWeapon.ARTILLERY;
        artilleryModel.damage = Integer.parseInt(artilleryElement.getAttribute(DAMAGE_ATTRIBUTE));
        artilleryModel.explosion = Integer.parseInt(artilleryElement.getAttribute(EXPLOSION));
        int range = Integer.parseInt(artilleryElement.getAttribute(RANGE_ATTRIBUTE));
        float speed = Float.parseFloat(artilleryElement.getAttribute(SPEED_ATTRIBUTE));
        container.setArtilleryModel(artilleryModel, range, speed);
    }
}
