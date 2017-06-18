package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.BulletManager;
import com.tankzor.game.game_object.manager.ExplosionManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.TerrainManager;
import com.tankzor.game.game_object.manager.TracksManager;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.gamehud.GameHUD;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.network.Network;
import com.tankzor.game.network.NotifyAdapter;
import com.tankzor.game.network.message.MessageCreator;
import com.tankzor.game.network.message.MessageResolver;

import org.json.JSONObject;

/**
 * Created by Admin on 12/27/2016.
 */

public class GameScreen extends BaseScreen {
    public int level;
    private AssetLoader assetLoader;
    private GameHUD gameHUD;
    private WarMachineManager warMachineManager;
    private TerrainManager terrainManager;
    private ExplosionManager explosionManager;
    private TracksManager tracksManager;
    private BulletManager bulletManager;
    private AirManager airManager;
    private LightingManager lightingManager;
    private PlayerCamera playerCamera;
    private WeaponManager weaponManager;
    private boolean isPause;

    public GameScreen(Tankzor parent, InputMultiplexer gameInputMultiplexer) {
        super(parent, null, new SpriteBatch(), gameInputMultiplexer);
    }

    public void initAndStartCampaignMap(int level) {
        this.level = level;
        Network.init(false, warMachineManager);
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Tankzor parent = getParent();
                parent.setScreen(parent.getLoadingScreen());

                assetLoader.loadAsset();
                weaponManager.initListWeaponItems(PlayerProfile.getInstance().getListWeaponModels());

                gameHUD.initHUD(assetLoader);

                gameHUD.getTouchScreenStage().setTerrainManager(terrainManager);
                gameHUD.getTouchScreenStage().setGameCamera(playerCamera);
                gameHUD.getTouchScreenStage().setBulletManager(bulletManager);
                gameHUD.setTouchPadInputEventListener(warMachineManager);

                lightingManager.initWorld();
                terrainManager.initLevel(GameScreen.this.level);

                GameSounds.getInstance().prepareBackgroundMusic();
                parent.setScreen(GameScreen.this);
            }
        });
    }

    private MessageResolver messageResolver = new MessageResolver() {
        @Override
        protected void onMessageReceived(int messageId, JSONObject jsonObject) {
            switch (messageId){
                case MessageCreator.WAR_MACHINE_ROTATE_MESSAGE:{
                    Network.getInstance()
                            .rotateWarMachine(jsonObject.getString(MessageCreator.NAME),
                                            jsonObject.getInt(MessageCreator.ORIENT));
                }
                break;

                default:{
                    break;
                }
            }
        }
    };

    public void initAndStartOnlineMap(final String mapName, final int index) {
        Network.init(true, warMachineManager);
        try {
            WarpClient.getInstance().addNotificationListener(new NotifyAdapter() {
                @Override
                public void onUpdatePeersReceived(UpdateEvent updateEvent) {
                    messageResolver.resolveMessage(new String(updateEvent.getUpdate()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Tankzor parent = getParent();
                parent.setScreen(parent.getLoadingScreen());

                assetLoader.loadAsset();
                weaponManager.initListWeaponItems(PlayerProfile.getInstance().getListWeaponModels());

                gameHUD.initHUD(assetLoader);

                gameHUD.getTouchScreenStage().setTerrainManager(terrainManager);
                gameHUD.getTouchScreenStage().setGameCamera(playerCamera);
                gameHUD.getTouchScreenStage().setBulletManager(bulletManager);
                gameHUD.setTouchPadInputEventListener(warMachineManager);

                lightingManager.initWorld();
                terrainManager.startOnlineMap(mapName, index);

                GameSounds.getInstance().prepareBackgroundMusic();
                parent.setScreen(GameScreen.this);
            }
        });
    }

    public void endGame() {
        assetLoader.dispose();

        warMachineManager.clear();
        bulletManager.clear();
        terrainManager.clear();
        explosionManager.clear();
        tracksManager.clear();
        airManager.clear();
        lightingManager.clear();
        gameHUD.clear();

        PlayerProfile.getInstance().setMoneyStarLifeChangedListener(null);
    }

    @Override
    protected void initViews() {
        assetLoader = new AssetLoader();
        weaponManager = new WeaponManager();

        playerCamera = new PlayerCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenViewport gameViewPort = new ScreenViewport(playerCamera);

        lightingManager = new LightingManager(playerCamera);
        explosionManager = new ExplosionManager(gameViewPort, getBatch(), assetLoader);

        terrainManager = new TerrainManager(gameViewPort, getBatch(), assetLoader);
        terrainManager.setExplosionManager(explosionManager);
        terrainManager.setLightingManager(lightingManager);

        explosionManager.setTerrainManager(terrainManager);
        explosionManager.setLightingManager(lightingManager);

        tracksManager = new TracksManager(gameViewPort, getBatch(), assetLoader);

        Tankzor parent = getParent();
        gameHUD = new GameHUD(parent.getUiViewPort(), getBatch(), parent, weaponManager);
        terrainManager.setGameHUD(gameHUD);
        gameHUD.setTerrainManager(terrainManager);

        airManager = new AirManager(gameViewPort, getBatch(), assetLoader);
        airManager.setTerrainManager(terrainManager);
        explosionManager.setAirManager(airManager);
        terrainManager.setAirManager(airManager);

        bulletManager = new BulletManager(gameViewPort, getBatch(), assetLoader);
        bulletManager.setAirManager(airManager);
        bulletManager.setGameHUD(gameHUD);
        bulletManager.setTerrainManager(terrainManager);
        bulletManager.setLightingManager(lightingManager);
        bulletManager.setExplosionManager(explosionManager);
        terrainManager.setBulletManager(bulletManager);

        warMachineManager = new WarMachineManager(gameViewPort, getBatch(), assetLoader, weaponManager);
        warMachineManager.setTerrainManager(terrainManager);
        warMachineManager.setExplosionManager(explosionManager);
        warMachineManager.setTracksManager(tracksManager);
        warMachineManager.setAirManager(airManager);
        warMachineManager.setBulletManager(bulletManager);//last call
        warMachineManager.setGameHUD(gameHUD);
        warMachineManager.setLightingManager(lightingManager);
        bulletManager.setWarMachineManager(warMachineManager);

        terrainManager.setWarMachineManager(warMachineManager);
    }

    @Override
    protected void addViews() {

    }

    @Override
    public void show() {
        gameMultiplexer.addProcessor(gameHUD.getGameHUDInputMultiplexer());
        GameSounds.getInstance().playBGM();
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(gameHUD.getGameHUDInputMultiplexer());
        GameSounds.getInstance().pauseBGM();
    }

    @Override
    public void pause() {
        isPause = true;
    }

    @Override
    public void resume() {
        isPause = false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        gameHUD.act();
        if (!isPause) {
            airManager.act(delta);
            terrainManager.act(delta);
            tracksManager.act(delta);
            bulletManager.act(delta);
            explosionManager.act(delta);
            lightingManager.act(delta);
            warMachineManager.act(delta);
        }

        terrainManager.draw();// not drop fps
        tracksManager.draw();
        warMachineManager.draw();// not drop fps
        bulletManager.draw();
        explosionManager.draw();
        airManager.draw();// not drop fps
        lightingManager.draw();// not drop fps
        gameHUD.draw();
    }

    @Override
    public void dispose() {
        assetLoader.dispose();

        warMachineManager.dispose();
        bulletManager.dispose();
        terrainManager.dispose();
        explosionManager.dispose();
        tracksManager.dispose();
        airManager.dispose();
        lightingManager.dispose();
        gameHUD.dispose();
    }

    public PlayerCamera getPlayerCamera() {
        return playerCamera;
    }

    public WarMachineManager getWarMachineManager() {
        return warMachineManager;
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public AirManager getAirManager() {
        return airManager;
    }

    public TracksManager getTracksManager() {
        return tracksManager;
    }
}
