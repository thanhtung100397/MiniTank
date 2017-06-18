package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.PlayerSpawnLocation;
import com.tankzor.game.game_object.RoundSet;

import com.tankzor.game.game_object.movable_item.war_machine.building.Building;
import com.tankzor.game.game_object.movable_item.war_machine.building.NuclearBuilding;
import com.tankzor.game.game_object.movable_item.war_machine.building.RadarBuilding;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.bot.MovableBotWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.bot.Turret;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.gamehud.GameHUD;
import com.tankzor.game.gamehud.MovingTouchPad;
import com.tankzor.game.gamehud.TouchPadInputEventListener;
import com.tankzor.game.ui.EndGameWidget;
import com.tankzor.game.util.QuadRectangle;
import com.tankzor.game.util.QuadTree;

/**
 * Created by Admin on 11/7/2016.
 */

public class WarMachineManager extends Stage implements TouchPadInputEventListener {
    public static boolean isEnemiesFrenzy = false;

    private TerrainManager terrainManager;
    private BulletManager bulletManager;
    private ExplosionManager explosionManager;
    private TracksManager tracksManager;
    private AirManager airManager;
    private GameHUD gameHUD;
    private LightingManager lightingManager;
    private AssetLoader assetLoader;

    private PlayerWarMachine playerMachine;
    private Array<MovableWarMachine> alliesMovableMachines;
    private Array<RadarBuilding> alliesHQBuildings;
    private Array<MovableWarMachine> enemiesMovableMachines;
    private Array<RadarBuilding> enemiesHQBuildings;

    private RoundSet alliesSpawnRoundSet;
    private RoundSet enemySpawnRoundSet;
    private PlayerSpawnLocation playerSpawnLocation;

    private WeaponManager weaponManager;

    private QuadTree<MovableWarMachine> alliesMovableMachineQuadTree;
    private QuadTree<MovableWarMachine> enemiesMovableMachineQuadTree;
    private QuadTree<WarMachine> alliesBuildingQuadTree;
    private QuadTree<WarMachine> enemiesBuildingQuadTree;

    private int enemiesTurretCount = 0;

//    private ShapeRenderer shapeRenderer;

    private int currentEnemiesLeft = 0;

    public WarMachineManager(Viewport viewport, Batch batch, AssetLoader assetLoader, WeaponManager weaponManager) {
        super(viewport, batch);
        this.assetLoader = assetLoader;
        this.weaponManager = weaponManager;
        alliesMovableMachines = new Array<MovableWarMachine>();
        enemiesMovableMachines = new Array<MovableWarMachine>();

        alliesHQBuildings = new Array<RadarBuilding>(5);
        enemiesHQBuildings = new Array<RadarBuilding>(5);

        alliesSpawnRoundSet = new RoundSet();
        enemySpawnRoundSet = new RoundSet();

//        getRoot().setCullingArea(((PlayerCamera) viewport.getCamera()).getCullingArea());

//        shapeRenderer = new ShapeRenderer();
//        shapeRenderer.setAutoShapeType(true);
    }

    public void initQuadTree() {
        alliesMovableMachineQuadTree = new QuadTree<MovableWarMachine>(new QuadRectangle(0, 0, terrainManager.getWidthMap(), terrainManager.getHeightMap()), 0);
        alliesBuildingQuadTree = new QuadTree<WarMachine>(new QuadRectangle(0, 0, terrainManager.getWidthMap(), terrainManager.getHeightMap()), 0);
        enemiesMovableMachineQuadTree = new QuadTree<MovableWarMachine>(new QuadRectangle(0, 0, terrainManager.getWidthMap(), terrainManager.getHeightMap()), 0);
        enemiesBuildingQuadTree = new QuadTree<WarMachine>(new QuadRectangle(0, 0, terrainManager.getWidthMap(), terrainManager.getHeightMap()), 0);
    }

    public void setBulletManager(BulletManager bulletManager) {
        this.bulletManager = bulletManager;
    }

    public void setTerrainManager(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public void setExplosionManager(ExplosionManager explosionManager) {
        this.explosionManager = explosionManager;
    }

    public void setTracksManager(TracksManager tracksManager) {
        this.tracksManager = tracksManager;
    }

    public void setGameHUD(GameHUD gameHUD) {
        this.gameHUD = gameHUD;
    }

    public GameHUD getGameHUD() {
        return gameHUD;
    }

    public void addMovableMachine(MovableWarMachine movableMachine) {
        int teamId = movableMachine.getTeamID();
        if (teamId == DamagedEntity.ALLIES_TEAM) {
            alliesMovableMachines.add(movableMachine);
        } else if (teamId == DamagedEntity.ENEMIES_TEAM) {
            enemiesMovableMachines.add(movableMachine);
        }
        addActor(movableMachine);
    }

    public void addTurret(Turret turret){
        int teamId = turret.getTeamID();
        if (teamId == DamagedEntity.ALLIES_TEAM) {
            alliesBuildingQuadTree.insert(turret.getQuadBound(), turret);
        } else if (teamId == DamagedEntity.ENEMIES_TEAM) {
            enemiesBuildingQuadTree.insert(turret.getQuadBound(), turret);
            enemiesTurretCount++;
        }
        addActor(turret);
    }

    public void addBuilding(int type, GridPoint2 location, int hitPoint, int teamId) {
        switch (type) {
            case Building.RADAR_BUILDING: {
                RadarBuilding radarBuilding = new RadarBuilding(location.x * DamagedEntity.ITEM_SIZE,
                                                                location.y * DamagedEntity.ITEM_SIZE,
                                                                teamId,
                                                                hitPoint,
                                                                terrainManager,
                                                                explosionManager,
                                                                lightingManager,
                                                                terrainManager,
                                                                airManager,
                                                                assetLoader);
                alliesHQBuildings.add(radarBuilding);
                addActor(radarBuilding);
                if(TerrainManager.gameMode == TerrainManager.ATK_DEF_HQ){
                    if (teamId == DamagedEntity.ALLIES_TEAM) {
                        alliesBuildingQuadTree.insert(radarBuilding.getQuadBound(), radarBuilding);
                    } else {
                        enemiesBuildingQuadTree.insert(radarBuilding.getQuadBound(), radarBuilding);
                    }
                }
            }
            break;

            case Building.NUCLEAR_BUILDING: {
                addActor(new NuclearBuilding(location.x * DamagedEntity.ITEM_SIZE,
                                                location.y * DamagedEntity.ITEM_SIZE,
                                                teamId,
                                                hitPoint,
                                                terrainManager,
                                                explosionManager,
                                                lightingManager,
                                                terrainManager,
                                                airManager,
                                                assetLoader));
            }
            break;

            default: {
                break;
            }
        }
    }

    public void removeMovableMachine(MovableWarMachine movableMachine) {
        int teamId = movableMachine.getTeamID();
        if (teamId == DamagedEntity.ALLIES_TEAM) {
            alliesMovableMachines.removeValue(movableMachine, true);
            alliesMovableMachineQuadTree.findThenRemoveQuadNode(movableMachine.getQuadBound(), movableMachine);
        } else if (teamId == DamagedEntity.ENEMIES_TEAM) {
            enemiesMovableMachines.removeValue(movableMachine, true);
            enemiesMovableMachineQuadTree.findThenRemoveQuadNode(movableMachine.getQuadBound(), movableMachine);
            gameHUD.onEnemiesNumberChange(--currentEnemiesLeft);
        }
    }

    public void removeTurret(Turret turret){
        int teamId = turret.getTeamID();
        if (teamId == DamagedEntity.ALLIES_TEAM) {
            alliesBuildingQuadTree.findThenRemoveQuadNode(turret.getQuadBound(), turret);
        } else if (teamId == DamagedEntity.ENEMIES_TEAM) {
            enemiesBuildingQuadTree.findThenRemoveQuadNode(turret.getQuadBound(), turret);
            gameHUD.onEnemiesNumberChange(--currentEnemiesLeft);
        }
    }

    public void removeHQBuilding(RadarBuilding radarBuilding){
        int teamId = radarBuilding.getTeamID();
        if (teamId == DamagedEntity.ALLIES_TEAM) {
            alliesHQBuildings.removeValue(radarBuilding, true);
            if(TerrainManager.gameMode == TerrainManager.ATK_DEF_HQ){
                alliesBuildingQuadTree.findThenRemoveQuadNode(radarBuilding.getQuadBound(), radarBuilding);
                if (alliesHQBuildings.size == 0) {
                    gameHUD.showEndGameWidget(EndGameWidget.LOSE_ID);
                }
            }
        } else if (teamId == DamagedEntity.ENEMIES_TEAM) {
            enemiesHQBuildings.removeValue(radarBuilding, true);
            if(TerrainManager.gameMode == TerrainManager.ATK_DEF_HQ){
                enemiesBuildingQuadTree.findThenRemoveQuadNode(radarBuilding.getQuadBound(), radarBuilding);
                if (enemiesHQBuildings.size == 0) {
                    gameHUD.showEndGameWidget(EndGameWidget.WIN_ID);
                }
            }
        }
    }

    public void setAirManager(AirManager airManager) {
        this.airManager = airManager;
    }

    private void updateAlliesMovableMachineQuadTree() {
        alliesMovableMachineQuadTree.clear();
        MovableWarMachine movableMachine;
        for (int i = 0; i < alliesMovableMachines.size; i++) {
            movableMachine = alliesMovableMachines.get(i);
            alliesMovableMachineQuadTree.insert(movableMachine.getQuadBound(), movableMachine);
        }
        if (playerMachine != null && !playerMachine.isDestroyed()) {
            alliesMovableMachineQuadTree.insert(playerMachine.getQuadBound(), playerMachine);
        }
    }

    private void updateEnemiesMovableMachineQuadTree() {
        enemiesMovableMachineQuadTree.clear();
        MovableWarMachine movableMachine;
        for (int i = 0; i < enemiesMovableMachines.size; i++) {
            movableMachine = enemiesMovableMachines.get(i);
            enemiesMovableMachineQuadTree.insert(movableMachine.getQuadBound(), movableMachine);
        }
    }

    public void updateAttackTargetFor(MovableBotWarMachine botWarMachine) {
        Array<MovableWarMachine> listMovableTargetsInRange = new Array<MovableWarMachine>();
        Array<WarMachine> listImmovableTargetsInRange = new Array<WarMachine>();
        if (botWarMachine.getTeamID() == DamagedEntity.ALLIES_TEAM) {
            enemiesMovableMachineQuadTree.findAllElementOverlap(listMovableTargetsInRange, botWarMachine.getRangeBound());
            enemiesBuildingQuadTree.findAllElementOverlap(listImmovableTargetsInRange, botWarMachine.getRangeBound());
        } else {
            alliesMovableMachineQuadTree.findAllElementOverlap(listMovableTargetsInRange, botWarMachine.getRangeBound());
            alliesBuildingQuadTree.findAllElementOverlap(listImmovableTargetsInRange, botWarMachine.getRangeBound());
        }
        botWarMachine.solveAttackTarget(listMovableTargetsInRange, listImmovableTargetsInRange);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateAlliesMovableMachineQuadTree();
        updateEnemiesMovableMachineQuadTree();
    }

//    @Override
//    public void draw() {
//        super.draw();
//        Array<QuadTree<WarMachine>> listNode = new Array<QuadTree<WarMachine>>();
//        enemiesBuildingQuadTree.getAllTrees(listNode);
//        shapeRenderer.setProjectionMatrix(getCamera().combined);
//        shapeRenderer.begin();
//        for (QuadTree<WarMachine> z : listNode) {
//            QuadRectangle rectangle = z.getZone();
//            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//            SpriteBatch spriteBatch = (SpriteBatch) getBatch();
//            spriteBatch.begin();
//            GameImages.getInstance().getGameFont().draw(spriteBatch, z.nodes.size+"", rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
//            spriteBatch.end();
//        }
//        shapeRenderer.end();
//    }

    @Override
    public void clear() {
        alliesMovableMachines.clear();
        enemiesMovableMachines.clear();

        setPlayerMachine(null);
        currentEnemiesLeft = 0;

        enemiesTurretCount = 0;

        isEnemiesFrenzy = false;

        super.clear();
    }

    @Override
    public void onFireButtonInputEvent(boolean isPress) {
        if (playerMachine == null || playerMachine.isDestroyed()) {
            return;
        }

        if (weaponManager.getCurrentWeaponItem() == null) {
            gameHUD.onNoWeaponLeft();
        }

        playerMachine.fireButtonPress = isPress;
    }

    @Override
    public void onTouchFlashLight() {
        if (playerMachine == null || playerMachine.isDestroyed()) {
            return;
        }
        playerMachine.switchFlashLightMode();
    }

    @Override
    public void onTouchSwitchWeaponsButton() {
        if (playerMachine == null) {
            return;
        }
        gameHUD.getPlayerWeaponMenu().setItemDisplayPaneVisible(true);
        weaponManager.nextWeapon();
    }

    @Override
    public void onTouchDPadEvent(int orient, boolean isPress) {
        if (playerMachine == null || playerMachine.isDestroyed()) {
            return;
        }
        if (isPress) {
//            Network.getInstance().messenger.onWarMachineRotate(playerMachine.getId(), orient);
            playerMachine.setNextOrient(orient);
            playerMachine.nextMoveOrient = orient;
        } else {
            playerMachine.nextMoveOrient = -1;
        }
    }

    @Override
    public void onTouchGesturePad(MovingTouchPad movingTouchPad) {
        if (playerMachine == null || playerMachine.isDestroyed()) {
            return;
        }
        float xPercent = movingTouchPad.getKnobPercentX();
        float yPercent = movingTouchPad.getKnobPercentY();
        if (xPercent == 0 && yPercent == 0) {
            return;
        }
        int orient;
        if (xPercent > 0) {
            if (yPercent >= MovingTouchPad.PERCENT_BOUND) {
                orient = WarMachine.UP_ORIENT;
            } else if (MovingTouchPad.PERCENT_BOUND > yPercent
                    && yPercent > -MovingTouchPad.PERCENT_BOUND) {
                orient = WarMachine.RIGHT_ORIENT;
            } else {
                orient = WarMachine.DOWN_ORIENT;
            }
        } else {
            if (yPercent >= MovingTouchPad.PERCENT_BOUND) {
                orient = WarMachine.UP_ORIENT;
            } else if (MovingTouchPad.PERCENT_BOUND > yPercent
                    && yPercent > -MovingTouchPad.PERCENT_BOUND) {
                orient = WarMachine.LEFT_ORIENT;
            } else {
                orient = WarMachine.DOWN_ORIENT;
            }
        }
        playerMachine.setNextOrient(orient);
    }

    @Override
    public PlayerWarMachine getPlayerWarMachine() {
        return playerMachine;
    }

    public AirManager getAirManager() {
        return airManager;
    }

    public TerrainManager getTerrainManager() {
        return terrainManager;
    }

    public BulletManager getBulletManager() {
        return bulletManager;
    }

    public ExplosionManager getExplosionManager() {
        return explosionManager;
    }

    public TracksManager getTracksManager() {
        return tracksManager;
    }

    public RoundSet getAlliesSpawnRoundSet() {
        return alliesSpawnRoundSet;
    }

    public RoundSet getEnemySpawnRoundSet() {
        return enemySpawnRoundSet;
    }

    public void initPlayerSpawnLocation(GridPoint2 location) {
        this.playerSpawnLocation = new PlayerSpawnLocation(location, this, weaponManager);
    }

    public void setPlayerMachine(PlayerWarMachine playerMachine) {
        this.playerMachine = playerMachine;
    }

    public void calculateCurrentEnemiesLeft() {
        this.currentEnemiesLeft = enemySpawnRoundSet.getTotalWarMachine() + enemiesTurretCount;
        gameHUD.onEnemiesNumberChange(currentEnemiesLeft);
    }

    public void firstSpawnWarMachine() {
        playerSpawnLocation.spawnWarMachine();
        alliesSpawnRoundSet.activeFirstRound();
        enemySpawnRoundSet.activeFirstRound();
    }

    public PlayerSpawnLocation getPlayerSpawnLocation() {
        return playerSpawnLocation;
    }

    public Array<MovableWarMachine> getAlliesMovableMachines() {
        return alliesMovableMachines;
    }

    public void setLightingManager(LightingManager lightingManager) {
        this.lightingManager = lightingManager;
    }

    public LightingManager getLightingManager() {
        return lightingManager;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
}