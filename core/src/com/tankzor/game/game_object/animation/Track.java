package com.tankzor.game.game_object.animation;

import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;

/**
 * Created by Admin on 11/25/2016.
 */

public class Track extends BaseAnimation{
    public static final float TRACKS_FADE_TIME = 5.0f;

    public Track(MovableWarMachine movableMachine, AssetLoader assetLoader) {
        setPosition(movableMachine.getX(), movableMachine.getY());
        setSize(DamagedEntity.ITEM_SIZE, DamagedEntity.ITEM_SIZE);
        init(assetLoader.getTrackTextureRegions(movableMachine.getPreviousOrient(), movableMachine.getCurrentOrient()), TRACKS_FADE_TIME);
    }
}
