package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tankzor.game.common_value.GameImages;

/**
 * Created by Admin on 1/22/2017.
 */

public class TankBackground extends Group{

    public TankBackground(){
        float widthBackground = Gdx.graphics.getWidth();
        float heightBackground = Gdx.graphics.getHeight();
        setBounds(0, 0, widthBackground, heightBackground);

        Skin skin = GameImages.getInstance().getUiSkin();

        Image tankPicture = new Image(skin.getDrawable(GameImages.KEY_MAIN_MENU_PICTURE));
        tankPicture.setBounds((widthBackground - tankPicture.getWidth()) / 2,
                                heightBackground - tankPicture.getHeight(),
                                tankPicture.getWidth(),
                                tankPicture.getHeight());

        Image tankPictureLeftPart = new Image(skin.getDrawable(GameImages.KEY_MAIN_MENU_PICTURE_PART));
        tankPictureLeftPart.setBounds(0,
                                        heightBackground - tankPicture.getHeight(),
                                        tankPicture.getX(),
                                        tankPictureLeftPart.getHeight());

        Image tankPictureRightPart = new Image(skin.getDrawable(GameImages.KEY_MAIN_MENU_PICTURE_PART));
        tankPictureRightPart.setBounds(tankPicture.getX() + tankPicture.getWidth(),
                                        heightBackground - tankPicture.getHeight(),
                                        tankPicture.getX(),
                                        tankPictureRightPart.getHeight());

        Image background = new Image(skin.getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, widthBackground, heightBackground - tankPicture.getHeight());

        addActor(tankPictureLeftPart);
        addActor(tankPicture);
        addActor(tankPictureRightPart);
        addActor(background);
    }

    public TankBackground(float paddingTop){
        float widthBackground = Gdx.graphics.getWidth();
        float heightBackground = Gdx.graphics.getHeight() - paddingTop;
        setBounds(0, 0, widthBackground, heightBackground);

        Skin skin = GameImages.getInstance().getUiSkin();

        Image tankPicture = new Image(skin.getDrawable(GameImages.KEY_MAIN_MENU_PICTURE));
        tankPicture.setBounds((widthBackground - tankPicture.getWidth()) / 2,
                heightBackground - tankPicture.getHeight(),
                tankPicture.getWidth(),
                tankPicture.getHeight());

        Image tankPictureLeftPart = new Image(skin.getDrawable(GameImages.KEY_MAIN_MENU_PICTURE_PART));
        tankPictureLeftPart.setBounds(0,
                heightBackground - tankPicture.getHeight(),
                tankPicture.getX(),
                tankPictureLeftPart.getHeight());

        Image tankPictureRightPart = new Image(skin.getDrawable(GameImages.KEY_MAIN_MENU_PICTURE_PART));
        tankPictureRightPart.setBounds(tankPicture.getX() + tankPicture.getWidth(),
                heightBackground - tankPicture.getHeight(),
                tankPicture.getX(),
                tankPictureRightPart.getHeight());

        Image background = new Image(skin.getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, widthBackground, heightBackground - tankPicture.getHeight());

        addActor(tankPictureLeftPart);
        addActor(tankPicture);
        addActor(tankPictureRightPart);
        addActor(background);
    }
}
