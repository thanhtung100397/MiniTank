package com.tankzor.game.common_value;

import com.badlogic.gdx.Gdx;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.util.CircleList;

/**
 * Created by Admin on 2/28/2017.
 */

public class Dimension {
    public static final float BASE_SCREEN_HEIGHT = 720.0f;
    public static float gameObjectScaleFactor = 2.0f;
    public static float buttonWidth = 500.0f;
    public static float buttonHeight = 80.0f;
    public static float buttonSpace = 10.0f;
    public static float smallFontScale = 1.0f;
    public static float normalFontScale = 1.1f;
    public static float quiteLargeFontScale = 1.4f;//setting font
    public static float largeFontScale = 1.6f;
    public static float endGameWidgetHeight = 150.0f;
    public static float screenTitleHeight = 60.0f;
    public static float buttonGroupMargin = 20.0f;
    public static float padding = 30.0f;
    public static float tankPictureBackgroundHeight = 75.0f;
    public static float sliderWidth = 100.0f;
    public static float mediumIconSize = 54.0f;
    public static float smallIconSize = 46.0f;
    public static float starIconSize = 32.0f;
    public static float separateLineWidth = 400.0f;

    public static float smallCircleButtonBackgroundSize = 72.0f;
    public static float mediumCircleButtonBackgroundSize = 100.0f;
    public static float bigCircleButtonBackgroundSize = 150.0f;
    public static float messageLabelHeight = 200.0f;
    public static float informationPanelWidth = 129.5f;
    public static float informationPanelHeight = 171.5f;
    public static float informationPanelLabelSpace = 10.0f;
    public static float playerWeaponItemWidth = 100.0f;
    public static float playerWeaponItemHeight = 32.0f;
    public static float healPaneWidth = 17.5f;
    public static float healPaneHeight = 15.0f;
    public static float DPADSize = 350.0f;
    private static CircleList<Integer> warMachineRotateImageIndex = new CircleList<Integer>(16);
    public static float scaleRatio;

    public static void initDimension() {
        scaleRatio = ((int) (Gdx.graphics.getHeight() / BASE_SCREEN_HEIGHT * 10)) / 10.0f;
        gameObjectScaleFactor *= scaleRatio;
        GameEntity.ITEM_SIZE *= gameObjectScaleFactor;
        buttonWidth *= scaleRatio;
        buttonHeight *= scaleRatio;
        buttonSpace *= scaleRatio;
        smallFontScale *= scaleRatio;
        normalFontScale *= scaleRatio;
        quiteLargeFontScale *= scaleRatio;
        largeFontScale *= scaleRatio;
        endGameWidgetHeight *= scaleRatio;
        screenTitleHeight *= scaleRatio;
        buttonGroupMargin *= scaleRatio;
        padding *= scaleRatio;
//        tankPictureBackgroundHeight *= scaleRatio;
        sliderWidth *= scaleRatio;
        mediumIconSize *= scaleRatio;
        smallIconSize *= scaleRatio;
        starIconSize *= scaleRatio;
        separateLineWidth *= scaleRatio;

        smallCircleButtonBackgroundSize *= scaleRatio * 1.1;
        mediumCircleButtonBackgroundSize *= scaleRatio * 1.1;
        bigCircleButtonBackgroundSize *= scaleRatio * 1.1;
        DPADSize *= scaleRatio * 1.1;

        messageLabelHeight *= scaleRatio;
        informationPanelWidth *= scaleRatio;
        informationPanelHeight *= scaleRatio;
        informationPanelLabelSpace *= scaleRatio;
        playerWeaponItemWidth *= scaleRatio;
        playerWeaponItemHeight *= scaleRatio;
        healPaneWidth *= scaleRatio;
        healPaneHeight *= scaleRatio;

        for (int i = 0; i < 16; i++) {
            warMachineRotateImageIndex.add(i);
        }
    }

    public static CircleList<Integer> getWarMachineRotateImageIndex() {
        return warMachineRotateImageIndex;
    }
}
