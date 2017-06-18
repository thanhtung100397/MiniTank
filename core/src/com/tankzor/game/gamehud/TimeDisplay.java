package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.game_object.support_item.SupportItemDurationListener;

/**
 * Created by Admin on 1/26/2017.
 */

public class TimeDisplay extends Table implements SupportItemDurationListener{
    private Image icon;
    private Label valueLabel;
    private DisplayActiveItemPane displayActiveItemPane;

    public TimeDisplay(DisplayActiveItemPane displayActiveItemPane, int iconId){
        this.displayActiveItemPane = displayActiveItemPane;
        TextureRegionDrawable drawable = new TextureRegionDrawable(GameImages.getInstance().getIcon(iconId));
        drawable.setMinWidth(Dimension.mediumIconSize);
        drawable.setMinHeight(Dimension.mediumIconSize);
        icon = new Image(drawable);
        add(icon).align(Align.left);

        valueLabel = new Label("", GameImages.getInstance().getLabelStyle());
        valueLabel.setAlignment(Align.center);
        valueLabel.setFontScale(Dimension.quiteLargeFontScale);
        add(valueLabel).align(Align.right).fillY().width(60);
    }

    @Override
    public void onActive(int duration) {
        if(getParent() == null) {
            displayActiveItemPane.addActor(this);
        }
        valueLabel.setText(duration+"");
    }

    @Override
    public void onDurationChange(int duration) {
        valueLabel.setText(duration+"");
    }

    @Override
    public void onTimeOut() {
        displayActiveItemPane.removeActor(this);
    }
}
