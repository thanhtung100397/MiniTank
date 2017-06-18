package com.tankzor.game.gamehud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.Dimension;

/**
 * Created by Admin on 1/24/2017.
 */

public class Pane extends Image {
    public static final int PANE_NORMAL = 0;
    public static final int PANE_LOST = 1;

    public static final int GREEN_PANE = 0;
    public static final int RED_PANE = 1;
    public static final int BLUE_PANE = 2;

    private Drawable drawables[];

    public Pane(int type, int stage, AssetLoader assetLoader){
        TextureRegion[] images = assetLoader.getPane(type);
        drawables = new Drawable[2];
        drawables[PANE_NORMAL] = new TextureRegionDrawable(images[PANE_NORMAL]);
        drawables[PANE_NORMAL].setMinWidth(Dimension.healPaneWidth);
        drawables[PANE_NORMAL].setMinHeight(Dimension.healPaneHeight);
        drawables[PANE_LOST] = new TextureRegionDrawable(images[PANE_LOST]);
        drawables[PANE_LOST].setMinWidth(Dimension.healPaneWidth);
        drawables[PANE_LOST].setMinHeight(Dimension.healPaneHeight);
        setState(stage);
    }

    public void setState(int state){
        setDrawable(drawables[state]);
    }
}
