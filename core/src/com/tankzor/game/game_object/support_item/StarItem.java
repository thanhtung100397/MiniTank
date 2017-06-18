package com.tankzor.game.game_object.support_item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;

/**
 * Created by Admin on 12/24/2016.
 */

public class StarItem extends SupportItem {
    public static final Color LIGHT_COLOR = new Color(0.75f, 0.75f, 0.5f, 0.5f);

    public StarItem(float x, float y, LightingManager lightingManager, AssetLoader assetLoader) {
        super(x, y, lightingManager);
        init(assetLoader.getSupportItemImages(STAR_ITEM), BLINK_TIME);
        setLightColor(LIGHT_COLOR);
        setBlinkAnimation(true);
    }

    @Override
    public void addEffectTo(MovableWarMachine item) {
        super.addEffectTo(item);
        if (item instanceof PlayerWarMachine) {
            PlayerProfile.getInstance().addStar(1);
        }
        remove();
    }
}
