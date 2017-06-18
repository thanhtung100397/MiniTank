package com.tankzor.game.common_value;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Admin on 11/6/2016.
 */

public class GameImages {
    public static final String ICON_FILE_NAME = "images/icon.atlas";
    public static final String BITMAP_FONT_FILE_NAME = "font/game_font.fnt";
    public static final String BITMAP_FONT_IMAGES_NAME = "font/game_font.png";
    public static final String LOADING_IMAGE_FILE_NAME = "images/loading_icon.png";

    public static final String KEY_BUTTON_BACKGROUND = "0";
    public static final String KEY_LABEL_BACKGROUND = "1";
    public static final String KEY_BACKGROUND = "2";
    public static final String KEY_SEPARATE_LINE = "3";
    public static final String KEY_MAIN_MENU_PICTURE = "4";
    public static final String KEY_MAIN_MENU_PICTURE_PART = "5";
    public static final String KEY_TITLE_MENU_ITEM_BACKGROUND = "6";
    public static final String KEY_ADD_BUTTON_NORMAL_BACKGROUND = "7";
    public static final String KEY_ADD_BUTTON_PRESS_BACKGROUND = "8";
    public static final String KEY_SELECTED_RECTANGLE = "9";
    public static final String KEY_NORMAL_SMALL_DPAD_BUTTON_BACKGROUND = "10";
    public static final String KEY_PRESS_SMALL_DPAD_BUTTON_BACKGROUND = "11";
    public static final String KEY_NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND = "12";
    public static final String KEY_PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND = "13";
    public static final String KEY_NORMAL_BIG_DPAD_BUTTON_BACKGROUND = "14";
    public static final String KEY_PRESS_BIG_DPAD_BUTTON_BACKGROUND = "15";
    public static final String KEY_FIRE_BUTTON_ICON = "16";
    public static final String KEY_CANCEL_FIRE_BUTTON_ICON = "17";
    public static final String KEY_SWITCH_BUTTON_ICON = "18";
    public static final String KEY_BACK_BUTTON_ICON = "19";
    public static final String KEY_NORMAL_MENU_BUTTON_ICON = "20";
    public static final String KEY_PRESS_MENU_BUTTON_ICON = "21";
    public static final String KEY_UP_ICON = "22";
    public static final String KEY_DOWN_ICON = "23";
    public static final String KEY_LEFT_ICON = "24";
    public static final String KEY_RIGHT_ICON = "25";
    public static final String KEY_BACK_ICON = "26";
    public static final String KEY_REPAIR_BUTTON_ICON = "27";
    public static final String KEY_BOOST_BUTTON_ICON = "28";
    public static final String KEY_LIST_WEAPON_BACKGROUND = "29";
    public static final String KEY_SEPARATE_LINE_LONG = "30";
    public static final String KEY_SLIDER_BACKGROUND = "31";
    public static final String KEY_SLIDER_KNOB_BACKGROUND = "32";
    public static final String KEY_SLIDER_KNOB_TOUCH_BACKGROUND = "33";
    public static final String KEY_CHECK_BOX_BACKGROUND = "34";
    public static final String KEY_CHECK_BOX_TOUCH_BACKGROUND = "35";
    public static final String KEY_CHECK_TICK = "36";
    public static final String KEY_FLASH_LIGHT_ICON = "37";
    public static final String KEY_END_GAME_WIDGET_BACKGROUND = "38";
    public static final String KEY_ROOM_BUTTON_NORMAL = "39";
    public static final String KEY_ROOM_BUTTON_PRESS = "40";
    public static final String KEY_DIALOG_BACKGROUND = "41";
    public static final String KEY_TEXT_FIELD_BACKGROUND = "42";

    public static final String SELECTED_RECTANGLE_IMAGE_NAME = "images/selected_rectangle.png";
    public static final String MAIN_MENU_BACKGROUND_IMAGE_NAME = "images/menu_background.png";
    public static final String MAIN_MENU_PICTURE_IMAGE_NAME = "images/menu_picture.png";
    public static final String MAIN_MENU_PICTURE_PART_IMAGE_NAME = "images/menu_picture_part.png";
    public static final String BUTTON_BACKGROUND_IMAGE_NAME = "images/selected_background.png";
    public static final String ADD_BUTTON_NORMAL_IMAGE_NAME = "images/add_icon_normal.png";
    public static final String ADD_BUTTON_PRESS_IMAGE_NAME = "images/add_icon_press.png";
    public static final String LABEL_BACKGROUND_IMAGE_NAME = "images/screen_tile_background.png";
    public static final String LINE_IMAGE_NAME = "images/horizontal_line.png";
    public static final String TITLE_MENU_ITEM_BACKGROUND_IMAGE_NAME = "images/menu_item_tile_background.png";
    public static final String NORMAL_SMALL_DPAD_BUTTON_BACKGROUND_IMAGE_NAME = "images/small_button_normal.png";
    public static final String PRESS_SMALL_DPAD_BUTTON_BACKGROUND_IMAGE_NAME = "images/small_button_press.png";
    public static final String NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND_IMAGE_NAME = "images/medium_button_normal.png";
    public static final String PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND_IMAGE_NAME = "images/medium_button_press.png";
    public static final String NORMAL_BIG_DPAD_BUTTON_BACKGROUND_IMAGE_NAME = "images/big_button_normal.png";
    public static final String PRESS_BIG_DPAD_BUTTON_BACKGROUND_IMAGE_NAME = "images/big_button_press.png";
    public static final String FIRE_BUTTON_ICON_NAME = "images/fire_button_icon.png";
    public static final String CANCEL_FIRE_BUTTON_ICON_NAME = "images/cancel_fire_button_icon.png";
    public static final String SWITCH_BUTTON_ICON_NAME = "images/switch_icon.png";
    public static final String BACK_BUTTON_ICON_NAME = "images/back_button_icon.png";
    public static final String NORMAL_MENU_BUTTON_ICON_NAME = "images/menu_icon_normal.png";
    public static final String PRESS_MENU_BUTTON_ICON_NAME = "images/menu_icon_press.png";
    public static final String UP_ICON_NAME = "images/up.png";
    public static final String DOWN_ICON_NAME = "images/down.png";
    public static final String LEFT_ICON_NAME = "images/left.png";
    public static final String RIGHT_ICON_NAME = "images/right.png";
    public static final String BACK_ICON_NAME = "images/back.png";
    public static final String REPAIR_BUTTON_ICON_NAME = "images/repair_icon.png";
    public static final String BOOST_BUTTON_ICON_NAME = "images/speed_icon.png";
    public static final String LIST_WEAPON_BACKGROUND_IMAGE_NAME = "images/list_weapon_background.png";
    public static final String LINE_LONG_IMAGE_NAME = "images/spline.png";
    public static final String SLIDER_BACKGROUND_IMAGE_NAME = "images/slider_bar_background.png";
    public static final String SLIDER_KNOB_IMAGE_NAME = "images/slider_knob.png";
    public static final String SLIDER_KNOB_TOUCH_IMAGE_NAME = "images/slider_knob_touch.png";
    public static final String CHECK_BOX_BACKGROUND_IMAGE_NAME = "images/check_box_background.png";
    public static final String CHECK_BOX_TOUCH_BACKGROUND_IMAGE_NAME = "images/check_box_touch_background.png";
    public static final String CHECK_BOX_TICK_IMAGE_NAME = "images/check_box_tick.png";
    public static final String FLASH_LIGHT_ICON_IMAGE_NAME = "images/flash_light_icon.png";
    public static final String END_GAME_WIDGET_BACKGROUND_IMAGE_NAME = "images/end_game_widget_background.png";
    public static final String ROOM_BUTTON_NORMAL_IMAGE_NAME = "images/room_button_normal.png";
    public static final String ROOM_BUTTON_PRESS_IMAGE_NAME = "images/room_button_press.png";
    public static final String DIALOG_BACKGROUND_IMAGE_NAME = "images/dialog_background.png";
    public static final String TEXT_FIELD_BACKGROUND_IMAGE_NAME = "images/text_field_background.png";
    public static final String GRAY_BACKGROUND_IMAGE_NAME = "images/gray_background.png";
    public static final String ORANGE_BACKGROUND_IMAGE_NAME = "images/orange_background.png";
    public static final String BROWN_BACKGROUND_IMAGE_NAME = "images/brown_background.png";

    private static GameImages instance = new GameImages();

    private TextureAtlas iconImages;

    private TextureRegion loadingCircleImage;

    private BitmapFont gameFont;
    private Skin uiSkin;
    private Label.LabelStyle labelStyle;

    public static GameImages getInstance() {
        return instance;
    }

    private GameImages() {
        decodeGameFont();
        decodeIconImages();
        decodeLoadingImage();
        createUiSkin();
    }

    private void createUiSkin() {
        uiSkin = new Skin();
        uiSkin.add(KEY_BUTTON_BACKGROUND, new Texture(Gdx.files.internal(BUTTON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_MAIN_MENU_PICTURE, new Texture(Gdx.files.internal(MAIN_MENU_PICTURE_IMAGE_NAME)));
        uiSkin.add(KEY_MAIN_MENU_PICTURE_PART, new Texture(Gdx.files.internal(MAIN_MENU_PICTURE_PART_IMAGE_NAME)));
        uiSkin.add(KEY_BACKGROUND, new Texture(Gdx.files.internal(MAIN_MENU_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_LABEL_BACKGROUND, new Texture(Gdx.files.internal(LABEL_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_SEPARATE_LINE, new Texture(Gdx.files.internal(LINE_IMAGE_NAME)));
        uiSkin.add(KEY_SEPARATE_LINE_LONG, new Texture(Gdx.files.internal(LINE_LONG_IMAGE_NAME)));
        uiSkin.add(KEY_TITLE_MENU_ITEM_BACKGROUND, new Texture(Gdx.files.internal(TITLE_MENU_ITEM_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_ADD_BUTTON_NORMAL_BACKGROUND, new Texture(Gdx.files.internal(ADD_BUTTON_NORMAL_IMAGE_NAME)));
        uiSkin.add(KEY_ADD_BUTTON_PRESS_BACKGROUND, new Texture(Gdx.files.internal(ADD_BUTTON_PRESS_IMAGE_NAME)));
        uiSkin.add(KEY_SELECTED_RECTANGLE, new Texture(Gdx.files.internal(SELECTED_RECTANGLE_IMAGE_NAME)));
        uiSkin.add(KEY_NORMAL_SMALL_DPAD_BUTTON_BACKGROUND, new Texture(Gdx.files.internal(NORMAL_SMALL_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_PRESS_SMALL_DPAD_BUTTON_BACKGROUND, new Texture(Gdx.files.internal(PRESS_SMALL_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND, new Texture(Gdx.files.internal(NORMAL_MEDIUM_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND, new Texture(Gdx.files.internal(PRESS_MEDIUM_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_NORMAL_BIG_DPAD_BUTTON_BACKGROUND, new Texture(Gdx.files.internal(NORMAL_BIG_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_PRESS_BIG_DPAD_BUTTON_BACKGROUND, new Texture(Gdx.files.internal(PRESS_BIG_DPAD_BUTTON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_FIRE_BUTTON_ICON, new Texture(Gdx.files.internal(FIRE_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_CANCEL_FIRE_BUTTON_ICON, new Texture(Gdx.files.internal(CANCEL_FIRE_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_SWITCH_BUTTON_ICON, new Texture(Gdx.files.internal(SWITCH_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_REPAIR_BUTTON_ICON, new Texture(Gdx.files.internal(REPAIR_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_BOOST_BUTTON_ICON, new Texture(Gdx.files.internal(BOOST_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_BACK_BUTTON_ICON, new Texture(Gdx.files.internal(BACK_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_NORMAL_MENU_BUTTON_ICON, new Texture(Gdx.files.internal(NORMAL_MENU_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_PRESS_MENU_BUTTON_ICON, new Texture(Gdx.files.internal(PRESS_MENU_BUTTON_ICON_NAME)));
        uiSkin.add(KEY_UP_ICON, new Texture(Gdx.files.internal(UP_ICON_NAME)));
        uiSkin.add(KEY_DOWN_ICON, new Texture(Gdx.files.internal(DOWN_ICON_NAME)));
        uiSkin.add(KEY_LEFT_ICON, new Texture(Gdx.files.internal(LEFT_ICON_NAME)));
        uiSkin.add(KEY_RIGHT_ICON, new Texture(Gdx.files.internal(RIGHT_ICON_NAME)));
        uiSkin.add(KEY_BACK_ICON, new Texture(Gdx.files.internal(BACK_ICON_NAME)));
        uiSkin.add(KEY_LIST_WEAPON_BACKGROUND, new Texture(Gdx.files.internal(LIST_WEAPON_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_SLIDER_BACKGROUND, new Texture(Gdx.files.internal(SLIDER_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_SLIDER_KNOB_BACKGROUND, new Texture(Gdx.files.internal(SLIDER_KNOB_IMAGE_NAME)));
        uiSkin.add(KEY_SLIDER_KNOB_TOUCH_BACKGROUND, new Texture(Gdx.files.internal(SLIDER_KNOB_TOUCH_IMAGE_NAME)));
        uiSkin.add(KEY_CHECK_BOX_BACKGROUND, new Texture(Gdx.files.internal(CHECK_BOX_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_CHECK_BOX_TOUCH_BACKGROUND, new Texture(Gdx.files.internal(CHECK_BOX_TOUCH_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_CHECK_TICK, new Texture(Gdx.files.internal(CHECK_BOX_TICK_IMAGE_NAME)));
        uiSkin.add(KEY_FLASH_LIGHT_ICON, new Texture(Gdx.files.internal(FLASH_LIGHT_ICON_IMAGE_NAME)));
        uiSkin.add(KEY_END_GAME_WIDGET_BACKGROUND, new Texture(Gdx.files.internal(END_GAME_WIDGET_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_ROOM_BUTTON_NORMAL, new Texture(Gdx.files.internal(ROOM_BUTTON_NORMAL_IMAGE_NAME)));
        uiSkin.add(KEY_ROOM_BUTTON_PRESS, new Texture(Gdx.files.internal(ROOM_BUTTON_PRESS_IMAGE_NAME)));
        uiSkin.add(KEY_DIALOG_BACKGROUND, new Texture(Gdx.files.internal(DIALOG_BACKGROUND_IMAGE_NAME)));
        uiSkin.add(KEY_TEXT_FIELD_BACKGROUND, new Texture(Gdx.files.internal(TEXT_FIELD_BACKGROUND_IMAGE_NAME)));
    }

    private void decodeIconImages() {
        iconImages = new TextureAtlas(Gdx.files.internal(ICON_FILE_NAME));
    }

    private void decodeGameFont() {
        Texture texture = new Texture(Gdx.files.internal(BITMAP_FONT_IMAGES_NAME));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameFont = new BitmapFont(Gdx.files.internal(BITMAP_FONT_FILE_NAME), new TextureRegion(texture));
        labelStyle = new Label.LabelStyle();
        labelStyle.font = gameFont;
    }

    private void decodeLoadingImage() {
        loadingCircleImage = new TextureRegion(new Texture(Gdx.files.internal(LOADING_IMAGE_FILE_NAME)));
    }

    public TextureRegion getIcon(int type) {
        return iconImages.findRegion(type + "");
    }

    public BitmapFont getGameFont() {
        return gameFont;
    }

    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public TextureRegion getLoadingCircleImage() {
        return loadingCircleImage;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }

    public void disposeAllResources() {
        iconImages.dispose();
        loadingCircleImage.getTexture().dispose();
        gameFont.dispose();
        uiSkin.dispose();
    }
}
