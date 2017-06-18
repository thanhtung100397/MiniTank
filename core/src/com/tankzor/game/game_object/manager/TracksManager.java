package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.animation.Track;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.WarMachineStateListener;

/**
 * Created by Admin on 11/25/2016.
 */

public class TracksManager extends Stage implements WarMachineStateListener {
    public static boolean ENABLE_EFFECT = true;
    private AssetLoader assetLoader;

    public TracksManager(Viewport viewport, Batch batch, AssetLoader assetLoader){
        super(viewport, batch);
        this.assetLoader = assetLoader;
//        getRoot().setCullingArea(((PlayerCamera) viewport.getCamera()).getCullingArea());
    }

    public void createTrack(MovableWarMachine movableMachine){
        if(ENABLE_EFFECT) {
            addActor(new Track(movableMachine, assetLoader));
        }
    }

    @Override
    public void onWarMachineStartMoving(WarMachine warMachine) {
        createTrack((MovableWarMachine) warMachine);
    }

    @Override
    public void onWarMachineStopMoving(WarMachine warMachine) {

    }

    @Override
    public void onWarMachineDestroyed(WarMachine warMachine) {

    }
}
