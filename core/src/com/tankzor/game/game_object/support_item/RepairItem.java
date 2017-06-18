package com.tankzor.game.game_object.support_item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;

/**
 * Created by Admin on 12/12/2016.
 */

public class RepairItem extends SupportItem {
    public static final Color LIGHT_COLOR = new Color(0.0f, 0.5f, 0.0f, 0.75f);

    public RepairItem(float x, float y, LightingManager lightingManager, AssetLoader assetLoader) {
        super(x, y, lightingManager);
        init(assetLoader.getSupportItemImages(REPAIR_KIT), BLINK_TIME);
        setLightColor(LIGHT_COLOR);
        setBlinkAnimation(true);
    }

    public RepairItem(){
        super(0,0, null);
    }

    @Override
    public void addEffectTo(MovableWarMachine item) {
        super.addEffectTo(item);
        item.setHitPoint(item.getMaxHitPoint());
        remove();
    }

    public void addEffectTo(WarMachine item){
        item.setHitPoint(item.getMaxHitPoint());
        remove();
    }
}
