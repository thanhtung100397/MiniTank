package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;

/**
 * Created by Admin on 1/26/2017.
 */

public class WeaponList extends Group {
    private PlayerWeaponMenu playerWeaponMenu;
    private VerticalGroup rootGroup;
    private ScrollPane scrollPane;

    public WeaponList(float width, float height, PlayerWeaponMenu playerWeaponMenu, WeaponManager weaponManager) {
        this.playerWeaponMenu = playerWeaponMenu;

        Image listBackground = new Image(GameImages.getInstance().getUiSkin().getDrawable(GameImages.KEY_LIST_WEAPON_BACKGROUND));
        listBackground.setBounds(0, 0, width, height);

        rootGroup = new VerticalGroup();
        rootGroup.setSize(width, height);

        scrollPane = new ScrollPane(rootGroup);
        scrollPane.setBounds(0, 0, width, height);

        addActor(listBackground);
        addActor(scrollPane);

        Array<WeaponManager.WeaponItem> listWeaponItems = weaponManager.getListWeaponItems();
        for (int i = 0; i < listWeaponItems.size; i++) {
            addNewWeaponItemButton(listWeaponItems.get(i), i);
        }
    }

    public ImageButton.ImageButtonStyle createWeaponItemButtonStyle(int weaponId) {
        ImageButton.ImageButtonStyle result = new ImageButton.ImageButtonStyle();
        GameImages gameImages = GameImages.getInstance();
        TextureRegionDrawable drawable = new TextureRegionDrawable(gameImages.getIcon(weaponId));
        drawable.setMinWidth(Dimension.mediumIconSize);
        drawable.setMinHeight(Dimension.mediumIconSize);
        result.imageUp = drawable;
        Drawable backgroundDrawable = gameImages.getUiSkin().getDrawable(GameImages.KEY_SELECTED_RECTANGLE);
        backgroundDrawable.setMinWidth(Dimension.playerWeaponItemWidth);
        backgroundDrawable.setMinHeight(Dimension.playerWeaponItemHeight);
        result.down = backgroundDrawable;
        return result;
    }

    public void addNewWeaponItemButton(WeaponManager.WeaponItem weaponItem, int index) {
        WeaponItemButton weaponItemButton = new WeaponItemButton(playerWeaponMenu,
                                                                    createWeaponItemButtonStyle(weaponItem.weaponModel.id));
        weaponItemButton.setWeaponItem(weaponItem);
        rootGroup.addActorAt(index, weaponItemButton);
    }
}
