package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.TerrainExplosionListener;
import com.tankzor.game.game_object.game_mode.Flag;
import com.tankzor.game.game_object.immovable_item.wall.WallFactory;
import com.tankzor.game.game_object.movable_item.DynamicDamagedEntity;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.immovable_item.wall.WallStateListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.game_object.immovable_item.wall.Wall;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.bot.MovableBotWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Mine;
import com.tankzor.game.game_object.support_item.BoostSpeedItem;
import com.tankzor.game.game_object.support_item.BoxItem;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.game_object.support_item.TimeFrenzyItem;
import com.tankzor.game.gamehud.GameHUD;
import com.tankzor.game.util.XmlMapReader;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

/**
 * Created by Admin on 11/7/2016.
 */

public class TerrainManager extends Stage implements OnDynamicDamagedEntityMovingListener, MapInformationProvider, PathFindingProvider, TerrainExplosionListener, WallStateListener {
    public static final String WALL_TILE_SET_NAME = "Wall";
    private static final String TERRAIN_TILE_SET_NAME = "Terrain";

    public static int gameMode;
    public static int additionRequirement;
    public static final int CAPTURE_FLAG = 0;
    public static final int DEATH_MATCH = 1;
    public static final int ATK_DEF_HQ = 2;
    public static final int ONE_LIFE = 1;

    private Terrain terrains[][];
    private ExplosionManager explosionManager;
    private WarMachineManager warMachineManager;
    private BulletManager bulletManager;
    private AirManager airManager;
    private LightingManager lightingManager;
    private GroundWeaponManager groundWeaponManager;
    private GameHUD gameHUD;
    private int row;
    private int column;
    private float widthMap;
    private float heightMap;
    private float xMinCameraTranslate, xMaxCameraTranslate;
    private float yMinCameraTranslate, yMaxCameraTranslate;

    private TiledMap map;
    private TiledMapTileLayer terrainLayer;
    private TiledMapTileLayer wallLayer;
    private TiledMapRenderer mapRenderer;

    private GridCell pathFindingMap[][];
    private GridCell pathFindingMapIgnoreSomeWall[][];
    private NavigationGrid<GridCell> navigationGrid;
    private NavigationGrid<GridCell> navigationGridIgnoreSomeWall;
    private AStarGridFinder<GridCell> pathFinder;

    private BoostSpeedItem activeBoostSpeedItem;
    private TimeFrenzyItem activeTimeFrenzyItem;

    private AssetLoader assetLoader;
    private XmlMapReader mapReader;

    private FlagManager flagManager;

//    private ShapeRenderer shapeRenderer;

    public TerrainManager(Viewport viewport, SpriteBatch batch, AssetLoader assetLoader) {
        super(viewport, batch);
        this.assetLoader = assetLoader;
        this.flagManager = new FlagManager(viewport, batch, assetLoader);
        this.flagManager.setTerrainManager(this);
        groundWeaponManager = new GroundWeaponManager(viewport, batch);
//        getRoot().setCullingArea(((PlayerCamera) viewport.getCamera()).getCullingArea());
//        shapeRenderer = new ShapeRenderer();
//        shapeRenderer.setAutoShapeType(true);
    }

    public void initLevel(int number) {
        startMap("maps/map_" + number);
    }

    private void startMap(String mapName) {
        map = new TmxMapLoader().load(mapName + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, Dimension.gameObjectScaleFactor, getBatch());

        initDimension();
        initTerrains();
        initWalls();
        initPathFindingSystem();
        warMachineManager.initQuadTree();
        mapReader.parseCampaignXml(mapName + ".xml");
        warMachineManager.firstSpawnWarMachine();
    }

    public void startOnlineMap(String mapName, int index){
        map = new TmxMapLoader().load(mapName + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, Dimension.gameObjectScaleFactor, getBatch());

        initDimension();
        initTerrains();
        initWalls();
        initPathFindingSystem();
        warMachineManager.initQuadTree();
        mapReader.parseOnlineMatchXml(mapName + ".xml", index);
        warMachineManager.firstSpawnWarMachine();
    }

    private void initDimension() {
        MapProperties mapProperties = map.getProperties();
        this.column = mapProperties.get("width", Integer.class);
        this.row = mapProperties.get("height", Integer.class);
        this.widthMap = column * GameEntity.ITEM_SIZE;
        this.heightMap = row * GameEntity.ITEM_SIZE;

        groundWeaponManager.setMapSize(widthMap, heightMap);

        float widthScreen = Gdx.graphics.getWidth();
        float heightScreen = Gdx.graphics.getHeight();

        this.xMinCameraTranslate = ((int) (widthScreen / 2 / GameEntity.ITEM_SIZE)) * GameEntity.ITEM_SIZE + GameEntity.ITEM_SIZE;
        this.xMaxCameraTranslate = (int) (widthMap - xMinCameraTranslate);
        this.yMinCameraTranslate = ((int) (heightScreen / 2 / GameEntity.ITEM_SIZE)) * GameEntity.ITEM_SIZE;
        this.yMaxCameraTranslate = (int) (heightMap - yMinCameraTranslate);
    }

    private void initTerrains() {
        terrainLayer = (TiledMapTileLayer) map.getLayers().get("Terrain_Layer");
        terrains = new Terrain[column][row];
        int id;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                id = terrainLayer.getCell(i, j).getTile().getId();
                terrains[i][j] = new Terrain(id, i * GameEntity.ITEM_SIZE, j * GameEntity.ITEM_SIZE, this);
            }
        }
    }

    private void initWalls() {
        wallLayer = (TiledMapTileLayer) map.getLayers().get("Wall_Layer");
        TiledMapTileLayer.Cell cell;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                cell = wallLayer.getCell(i, j);
                if (cell != null) {
                    terrains[i][j].putWallOn(WallFactory.createWall(cell.getTile().getId(), terrains[i][j], this, explosionManager, lightingManager));
                }
            }
        }
    }

    private void initPathFindingSystem() {
        pathFindingMap = new GridCell[column][row];
        pathFindingMapIgnoreSomeWall = new GridCell[column][row];

        navigationGrid = new NavigationGrid<GridCell>(pathFindingMap, false);
        navigationGridIgnoreSomeWall = new NavigationGrid<GridCell>(pathFindingMapIgnoreSomeWall, false);

        Wall wall;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                pathFindingMap[i][j] = new GridCell(i, j);
                pathFindingMapIgnoreSomeWall[i][j] = new GridCell(i, j);
                wall = terrains[i][j].getWallOn();
                if (wall != null) {
                    if (wall.getType() > Wall.PLANT_TYPE) {//spikes, stones or steels wall
                        pathFindingMapIgnoreSomeWall[i][j].setWalkable(false);
                    } else {
                        pathFindingMapIgnoreSomeWall[i][j].setWalkable(true);
                    }
                    pathFindingMap[i][j].setWalkable(false);
                } else {
                    pathFindingMap[i][j].setWalkable(true);
                    pathFindingMapIgnoreSomeWall[i][j].setWalkable(true);
                }
            }
        }

        GridFinderOptions options = new GridFinderOptions();
        options.allowDiagonal = false;
        pathFinder = new AStarGridFinder<GridCell>(GridCell.class, options);
    }

    private void updatePathFindingMapKamikaze(int i, int j, boolean isWalkable) {
        if ((i < 0 || i > column - 1) ||
                (j < 0 || j > row - 1)) {
            return;
        }
        pathFindingMap[i][j].setWalkable(isWalkable);
    }

    private void updatePathFindingMapTank(int i, int j, boolean isWalkable) {
        if ((i < 0 || i > column - 1) ||
                (j < 0 || j > row - 1)) {
            return;
        }
        pathFindingMapIgnoreSomeWall[i][j].setWalkable(isWalkable);
    }

    public void addSupportItem(SupportItem supportItem) {
        int i = (int) (supportItem.getX() / GameEntity.ITEM_SIZE);
        int j = (int) (supportItem.getY() / GameEntity.ITEM_SIZE);
        terrains[i][j].putSupportItemOn(supportItem);
        addActor(supportItem);
    }

    public void activeCaptureFlagGameMode() {
        gameMode = CAPTURE_FLAG;
    }

    public void setAdditionRequirement(int option){
        additionRequirement = option;
    }

    public void activeDeathMatchGameMode() {
        gameMode = DEATH_MATCH;
    }

    public void activeDefenceHQGameMode() {
        gameMode = ATK_DEF_HQ;
    }

    @Override
    public void repaintWall(Wall wall) {
        int iWall = (int) (wall.getX() / GameEntity.ITEM_SIZE);
        int jWall = (int) (wall.getY() / GameEntity.ITEM_SIZE);

        wallLayer.getCell(iWall, jWall)
                .setTile(map.getTileSets().getTileSet(WALL_TILE_SET_NAME).getTile(wall.getId()));
    }

    @Override
    public void repaintTerrain(Terrain terrain) {
        int iTerrain = (int) (terrain.getX() / GameEntity.ITEM_SIZE);
        int jTerrain = (int) (terrain.getY() / GameEntity.ITEM_SIZE);
        terrainLayer.getCell(iTerrain, jTerrain)
                .setTile(map.getTileSets().getTileSet(TERRAIN_TILE_SET_NAME).getTile(terrain.getId()));
    }

    @Override
    public void onWallDestroyed(Wall wall) {
        int iWall = (int) (wall.getX() / GameEntity.ITEM_SIZE);
        int jWall = (int) (wall.getY() / GameEntity.ITEM_SIZE);
        terrains[iWall][jWall].putWallOn(null);
        updatePathFindingMapKamikaze(iWall, jWall, true);
        updatePathFindingMapTank(iWall, jWall, true);
        if (terrains[iWall][jWall].canCreateHole()) {
            if (MathUtils.random(0, 9) > Wall.EXPLOSION_HOLE_CHANCE) {
                return;
            }
            terrains[iWall][jWall].createHole();
        }
    }

    @Override
    public GameHUD getGameHud() {
        return gameHUD;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        groundWeaponManager.act(delta);
    }

    @Override
    public void draw() {
        mapRenderer.setView((OrthographicCamera) getCamera());
        mapRenderer.render();

        groundWeaponManager.draw();
        flagManager.draw();

        super.draw();

//        shapeRenderer.setProjectionMatrix(getCamera().combined);
//        shapeRenderer.begin();
//        for (int i = 0; i < column; i++) {
//            for (int j = 0; j < row; j++) {
//                if (terrains[i][j].hasWarMachineOn() || terrains[i][j].hasIncomingWarMachineEnter()) {
//                    shapeRenderer.rect(i * GameEntity.ITEM_SIZE, j * GameEntity.ITEM_SIZE, GameEntity.ITEM_SIZE, GameEntity.ITEM_SIZE);
//                }
//            }
//        }
//        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.flagManager.dispose();
        if (map != null) {
            map.dispose();
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.activeBoostSpeedItem = null;
        this.activeTimeFrenzyItem = null;
    }

    @Override
    public void onDynamicDamagedEntityEnter(DynamicDamagedEntity item, float x1, float y1, float x2, float y2) {
        Terrain terrain = getTerrain((int) (x1 / item.getWidth()), (int) (y1 / item.getHeight()));
        if (terrain != null && terrain.addItemOn(item)) {
            if (x2 != x1 || y2 != y1) {
                terrain = getTerrain((int) (x2 / item.getWidth()), (int) (y2 / item.getHeight()));
                if (terrain != null) {
                    terrain.addItemOn(item);
                }
            }
        }
    }

    @Override
    public void onDynamicDamagedEntityExit(DynamicDamagedEntity item, float x1, float y1, float x2, float y2) {
        Terrain terrain = getTerrain((int) (x1 / item.getWidth()), (int) (y1 / item.getHeight()));
        if (terrain != null) {
            terrain.removeItemOn(item);
        }

        if (x2 != x1 || y2 != y1) {
            terrain = getTerrain((int) (x2 / item.getWidth()), (int) (y2 / item.getHeight()));
            if (terrain != null) {
                terrain.removeItemOn(item);
            }
        }
    }

    @Override
    public boolean checkMovable(int iDes, int jDes, int orient) {
        Terrain terrain = getTerrain(iDes, jDes);
        if (terrain != null && !terrain.hasWallOn() && !terrain.hasIncomingWarMachineEnter()) {
            if (terrain.getWarMachineOn() != null) {
                WarMachine warMachineOn = terrain.getWarMachineOn();
                if (warMachineOn.getTeamID() == DamagedEntity.ALLIES_TEAM && warMachineOn instanceof MovableBotWarMachine) {
                    ((MovableBotWarMachine) warMachineOn).getOutOfThisWay(orient);
                }
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public float getWidthMap() {
        return widthMap;
    }

    @Override
    public float getHeightMap() {
        return heightMap;
    }

    @Override
    public int getRowNumber() {
        return row;
    }

    @Override
    public int getColumnNumber() {
        return column;
    }

    @Override
    public Terrain getTerrain(int i, int j) {
        if ((i < 0 || i > column - 1) ||
                (j < 0 || j > row - 1)) {
            return null;
        }
        return terrains[i][j];
    }

    @Override
    public boolean canTranslateCamera(float x, float y, int orient) {
        switch (orient) {
            case WarMachine.UP_ORIENT:
            case WarMachine.DOWN_ORIENT: {
                return (y >= yMinCameraTranslate && y <= yMaxCameraTranslate);
            }

            case WarMachine.LEFT_ORIENT:
            case WarMachine.RIGHT_ORIENT: {
                return (x >= xMinCameraTranslate && x <= xMaxCameraTranslate);
            }

            default: {
                return false;
            }
        }
    }

    @Override
    public AStarGridFinder<GridCell> getPathFinder() {
        return pathFinder;
    }

    public NavigationGrid<GridCell> getNavigationGridIgnoreSomeWall() {
        return navigationGridIgnoreSomeWall;
    }

    @Override
    public NavigationGrid<GridCell> getNavigationGrid() {
        return navigationGrid;
    }

    @Override
    public boolean takeItemIfPossible(MovableWarMachine movableMachine) {
        Terrain terrain = getTerrain((int) (movableMachine.getX() / GameEntity.ITEM_SIZE),
                (int) (movableMachine.getY() / GameEntity.ITEM_SIZE));
        SupportItem supportItem = terrain.getSupportItemOn();
        if (supportItem != null) {
            if (supportItem instanceof BoxItem) {
                gameHUD.showToastMessage(((BoxItem) supportItem).getMessage());
            }
            supportItem.addEffectTo(movableMachine);
            terrain.putSupportItemOn(null);
        }

        Mine mine = terrain.getMine();
        if (mine != null && mine.getTeamID() + movableMachine.getTeamID() == 0) {
            //just destroy mine, damage will be calculated (ExplosionManager)
            mine.destroy();
            terrain.putMineOn(null);
            return mine.getHitPoint() >= movableMachine.getHitPoint();
        }
        return false;
    }

    public void setExplosionManager(ExplosionManager explosionManager) {
        this.explosionManager = explosionManager;
    }

    public ExplosionManager getExplosionManager() {
        return explosionManager;
    }

    public void setWarMachineManager(WarMachineManager warMachineManager) {
        this.warMachineManager = warMachineManager;
        this.mapReader = new XmlMapReader(this, explosionManager, warMachineManager, bulletManager, lightingManager, airManager, assetLoader);
    }

    public void setBulletManager(BulletManager bulletManager) {
        this.bulletManager = bulletManager;
    }

    public BulletManager getBulletManager() {
        return bulletManager;
    }

    public GameHUD getGameHUD() {
        return gameHUD;
    }

    public void setGameHUD(GameHUD gameHUD) {
        this.gameHUD = gameHUD;
        flagManager.setGameHUD(gameHUD);
    }

    public AirManager getAirManager() {
        return airManager;
    }

    public void setAirManager(AirManager airManager) {
        this.airManager = airManager;
    }

    public BoostSpeedItem getActiveBoostSpeedItem() {
        return activeBoostSpeedItem;
    }

    public void setActiveBoostSpeedItem(BoostSpeedItem activeBoostSpeedItem) {
        this.activeBoostSpeedItem = activeBoostSpeedItem;
    }

    public TimeFrenzyItem getActiveTimeFrenzyItem() {
        return activeTimeFrenzyItem;
    }

    public void setActiveTimeFrenzyItem(TimeFrenzyItem activeTimeFrenzyItem) {
        this.activeTimeFrenzyItem = activeTimeFrenzyItem;
    }

    public void setLightingManager(LightingManager lightingManager) {
        this.lightingManager = lightingManager;
    }

    public LightingManager getLightingManager() {
        return lightingManager;
    }

    public FlagManager getFlagManager() {
        return flagManager;
    }

    @Override
    public GroundWeaponManager getGroundWeaponManager() {
        return groundWeaponManager;
    }
}
