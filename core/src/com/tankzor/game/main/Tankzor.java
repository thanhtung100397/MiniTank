package com.tankzor.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.dialog.DialogManager;
import com.tankzor.game.ui.BaseScreen;
import com.tankzor.game.ui.DocumentScreen;
import com.tankzor.game.ui.FPSDisplay;
import com.tankzor.game.ui.GameScreen;
import com.tankzor.game.ui.ListMissionScreen;
import com.tankzor.game.ui.LoadingScreen;
import com.tankzor.game.ui.MenuScreen;
import com.tankzor.game.ui.MissionResultScreen;
import com.tankzor.game.ui.ObjectiveScreen;
import com.tankzor.game.ui.ListRoomScreen;
import com.tankzor.game.ui.PauseScreen;
import com.tankzor.game.ui.RoomScreen;
import com.tankzor.game.ui.SettingsScreen;
import com.tankzor.game.ui.WorkshopScreen;
import com.tankzor.game.ui.WeaponDetailScreen;

/**
 * Created by Admin on 12/27/2016.
 */

public class Tankzor extends Game{
    private SpriteBatch gameBatch;
    private ScreenViewport uiViewPort;

    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private WorkshopScreen workshopScreen;
    private WeaponDetailScreen weaponDetailScreen;
    private DocumentScreen documentScreen;
    private ListMissionScreen listMissionScreen;
    private LoadingScreen loadingScreen;
    private PauseScreen pauseScreen;
    private SettingsScreen settingsScreen;
    private ObjectiveScreen objectiveScreen;
    private MissionResultScreen missionResultScreen;
    private ListRoomScreen listRoomScreen;
    private RoomScreen roomScreen;
    private FPSDisplay fpsDisplay;
    private InputMultiplexer gameInputMultiplexer;

    public Tankzor(){

    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        gameInputMultiplexer = new InputMultiplexer();
        gameInputMultiplexer.addProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACK){
                    BaseScreen currentScreen = ((BaseScreen) getScreen());
                    currentScreen.onBackPress();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(gameInputMultiplexer);

        Dimension.initDimension();//init game factor and dimension
        Box2D.init();

        OrthographicCamera orthographicCamera = new OrthographicCamera();
        uiViewPort = new ScreenViewport(orthographicCamera);
        gameBatch = new SpriteBatch();

        menuScreen = new MenuScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        workshopScreen = new WorkshopScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        weaponDetailScreen = new WeaponDetailScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        documentScreen = new DocumentScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        listMissionScreen = new ListMissionScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        objectiveScreen = new ObjectiveScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        loadingScreen = new LoadingScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        pauseScreen = new PauseScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        settingsScreen = new SettingsScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        missionResultScreen = new MissionResultScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        listRoomScreen = new ListRoomScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        roomScreen = new RoomScreen(this, uiViewPort, gameBatch, gameInputMultiplexer);
        fpsDisplay = new FPSDisplay(gameBatch, orthographicCamera);

        gameScreen = new GameScreen(this, gameInputMultiplexer);//initHitPoint last

        listMissionScreen.setPreviousScreen(menuScreen);
        roomScreen.setPreviousScreen(listRoomScreen);
        listRoomScreen.setPreviousScreen(menuScreen);

        gameScreen.setPreviousScreen(pauseScreen);
        pauseScreen.setPreviousScreen(gameScreen);
        objectiveScreen.setPreviousScreen(listMissionScreen);
        missionResultScreen.setPreviousScreen(listMissionScreen);

        documentScreen.setPreviousScreen(workshopScreen);
        weaponDetailScreen.setPreviousScreen(workshopScreen);

        setScreen(menuScreen);
    }

    public ScreenViewport getUiViewPort() {
        return uiViewPort;
    }

    public InputMultiplexer getGameInputMultiplexer() {
        return gameInputMultiplexer;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        super.render();
        fpsDisplay.updateAndRender();
        DialogManager.getInstance().removeDismissDialogs();
    }

    @Override
    public void dispose() {
        super.dispose();
        GameImages.getInstance().disposeAllResources();
        GameSounds.getInstance().disposeAllResources();
        gameBatch.dispose();
        menuScreen.dispose();
        workshopScreen.dispose();
        weaponDetailScreen.dispose();
        documentScreen.dispose();
        listMissionScreen.dispose();
        objectiveScreen.dispose();
        loadingScreen.dispose();
        pauseScreen.dispose();
        gameScreen.dispose();
        settingsScreen.dispose();
        listRoomScreen.dispose();
        missionResultScreen.dispose();
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public ListMissionScreen getListMissionScreen() {
        return listMissionScreen;
    }

    public LoadingScreen getLoadingScreen() {
        return loadingScreen;
    }

    public WorkshopScreen getWorkshopScreen() {
        return workshopScreen;
    }

    public WeaponDetailScreen getWeaponDetailScreen() {
        return weaponDetailScreen;
    }

    public DocumentScreen getDocumentScreen() {
        return documentScreen;
    }

    public PauseScreen getPauseScreen() {
        return pauseScreen;
    }

    public SettingsScreen getSettingsScreen() {
        return settingsScreen;
    }

    public ObjectiveScreen getObjectiveScreen() {
        return objectiveScreen;
    }

    public MissionResultScreen getMissionResultScreen() {
        return missionResultScreen;
    }

    public ListRoomScreen getListRoomScreen() {
        return listRoomScreen;
    }

    public RoomScreen getRoomScreen() {
        return roomScreen;
    }
}
