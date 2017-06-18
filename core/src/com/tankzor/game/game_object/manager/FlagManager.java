package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.game_mode.Flag;
import com.tankzor.game.game_object.game_mode.OnFlagRemovedListener;
import com.tankzor.game.game_object.immovable_item.Terrain;
import com.tankzor.game.gamehud.GameHUD;
import com.tankzor.game.ui.EndGameWidget;

/**
 * Created by Admin on 3/28/2017.
 */

public class FlagManager extends Stage implements OnFlagRemovedListener {
    private TerrainManager terrainManager;
    private GameHUD gameHUD;
    private AssetLoader assetLoader;

    public FlagManager(Viewport gameViewport, SpriteBatch gameBatch, AssetLoader assetLoader){
        super(gameViewport, gameBatch);
        this.assetLoader = assetLoader;
//        getRoot().setCullingArea(((PlayerCamera) gameViewport.getCamera()).getCullingArea());
    }

    public void setTerrainManager(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public void setGameHUD(GameHUD gameHUD) {
        this.gameHUD = gameHUD;
    }

    public void addFlagOnTerrain(int i, int j, int flagType, boolean hasListener) {
        Flag flag = new Flag(i * GameEntity.ITEM_SIZE, j * GameEntity.ITEM_SIZE,
                            assetLoader.getFlagImage(flagType),
                            terrainManager.getTerrain(i, j));
        if(hasListener){
            flag.setOnFlagRemovedListener(this);
        }
        addActor(flag);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void onFlagRemoved() {
        if(TerrainManager.gameMode == TerrainManager.CAPTURE_FLAG) {
            if (getActors().size == 0) {
                gameHUD.showEndGameWidget(EndGameWidget.WIN_ID);
            } else {
                gameHUD.showToastMessage("Good job! keep trying to get other flags");
            }
        }
    }
}
