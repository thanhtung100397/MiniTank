package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;

/**
 * Created by Admin on 11/27/2016.
 */
public class PlayerWeaponMenu extends Group {
    public static final int MAX_ITEM_DISPLAYED_IN_LIST = 6;
    public static final int MAX_ITEM_DISPLAYED_IN_DISPLAY_PANE = 3;

    private WeaponList weaponList;
    private DisplayActiveItemPane displayActiveItemPane;
    private WeaponManager weaponManager;

    public PlayerWeaponMenu(float xTopLeft, float yTopLeft, WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
        float listHeight = Dimension.playerWeaponItemHeight * MAX_ITEM_DISPLAYED_IN_LIST;

        setBounds(xTopLeft, yTopLeft - listHeight,
                Dimension.playerWeaponItemWidth,
                listHeight);

        weaponList = new WeaponList(Dimension.playerWeaponItemWidth, listHeight, this, weaponManager);
        weaponList.setPosition(0, 0);

        float paneHeight = Dimension.playerWeaponItemHeight * MAX_ITEM_DISPLAYED_IN_DISPLAY_PANE;
        displayActiveItemPane = new DisplayActiveItemPane(Dimension.playerWeaponItemWidth, paneHeight, this, weaponManager.getCurrentWeaponItem());
        displayActiveItemPane.setPosition(0, listHeight - paneHeight);

        addActor(weaponList);
        addActor(displayActiveItemPane);

        setItemDisplayPaneVisible(true);
    }

    public WeaponList getWeaponList() {
        return weaponList;
    }

    public DisplayActiveItemPane getDisplayActiveItemPane() {
        return displayActiveItemPane;
    }

    public void setItemDisplayPaneVisible(boolean isVisible){
        displayActiveItemPane.setVisible(isVisible);
        weaponList.setVisible(!isVisible);
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }
}