package com.tankzor.game.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.ui.CircleProgressBar;

/**
 * Created by Admin on 6/7/2017.
 */

public class LoadingDialog extends BaseDialog {
    private Label label;

    public LoadingDialog(String message) {
        label.setText(message);
    }

    @Override
    protected void initViews() {
        Window window = getWindow();
        CircleProgressBar circleProgressBar = new CircleProgressBar();
        circleProgressBar.setSize(48, 48);
        label = new Label("", GameImages.getInstance().getLabelStyle());
        label.setFontScale(Dimension.smallFontScale);

        window.add(circleProgressBar).padRight(20).align(Align.center);
        window.add(label).align(Align.center);

        circleProgressBar.start();
    }

    public void setMessage(String message){
        label.setText(message);
    }
}
