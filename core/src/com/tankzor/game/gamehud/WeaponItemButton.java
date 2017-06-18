package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.movable_item.weapon.OnWeaponCapacityListener;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;

/**
 * Created by Admin on 1/26/2017.
 */

public class WeaponItemButton extends ImageButton implements OnWeaponCapacityListener {
    public Label capacityLabel;
    private boolean isDragged;
    private float xTouchDown, yTouchDown;
    private PlayerWeaponMenu playerWeaponMenu;
    private WeaponManager.WeaponItem weaponItem;

    public WeaponItemButton(PlayerWeaponMenu playerWeaponMenu, ImageButtonStyle style) {
        super(style);
        this.playerWeaponMenu = playerWeaponMenu;

        getImage().setAlign(Align.left);

        capacityLabel = new Label("", GameImages.getInstance().getLabelStyle());
        capacityLabel.setAlignment(Align.center);
        capacityLabel.setFontScale(Dimension.quiteLargeFontScale);
        add(capacityLabel).align(Align.right).fillY().width(Dimension.playerWeaponItemWidth - Dimension.mediumIconSize);

        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                xTouchDown = x;
                yTouchDown = y;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragged) {
                    isDragged = false;
                    return;
                }
                onPress();
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Math.abs(xTouchDown - x) > 10 || Math.abs(yTouchDown - y) > 10) {
                    isDragged = true;
                }
            }
        });
    }

    public void setWeaponItem(WeaponManager.WeaponItem weaponItem) {
        this.weaponItem = weaponItem;
        weaponItem.registerWeaponCapacityChangeListener(this);
    }

    public WeaponManager.WeaponItem getWeaponItem() {
        return weaponItem;
    }

    protected void onPress(){
        GameSounds.getInstance().playSFX(GameSounds.CHOOSE_WEAPON_SFX_ID);
        playerWeaponMenu.getWeaponManager().setCurrentWeaponItem(weaponItem);
        playerWeaponMenu.setItemDisplayPaneVisible(true);
    }

    @Override
    public void onWeaponChangeCapacity(int newCapacity) {
        capacityLabel.setText(newCapacity+"");
    }

    @Override
    public void onWeaponRemoved() {
        remove();
    }
}
