package com.tankzor.game.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 6/7/2017.
 */

public abstract class BaseDialog {
    protected boolean canDismissWithoutAction = false;
    private Window window;

    public BaseDialog(){
        GameImages gameImages = GameImages.getInstance();
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.background = gameImages.getUiSkin().getDrawable(GameImages.KEY_DIALOG_BACKGROUND);
        float widthWindow = windowStyle.background.getMinWidth();
        float heightWindow = windowStyle.background.getMinHeight();
//        windowStyle.background.setMinWidth(widthWindow);
//        windowStyle.background.setMinHeight(heightWindow);
        windowStyle.titleFont = gameImages.getGameFont();
        windowStyle.titleFontColor = Color.WHITE;
        window = new Window("", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);
        initViews();
        window.pack();
        window.setPosition((Gdx.graphics.getWidth() - widthWindow) / 2,
                (Gdx.graphics.getHeight() - heightWindow) / 2);
    }

    protected Window getWindow() {
        return window;
    }

    public void showOn(Stage stage){
        stage.addActor(window);
        DialogManager.getInstance().addCurrentVisibleDialog(this);
    }

    public void dismiss(){
        DialogManager.getInstance().dismissDialog(this);
    }

    protected abstract void initViews();


}
