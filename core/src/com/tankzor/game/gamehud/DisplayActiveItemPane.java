package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 1/26/2017.
 */

public class DisplayActiveItemPane extends VerticalGroup {
    private WeaponItemButton currentItem;
    private ImageButton.ImageButtonStyle currentItemButtonStyle;
    private TimeDisplay speedCoolDownDisplay;
    private TimeDisplay freezeCoolDownDisplay;

    public DisplayActiveItemPane(float width, float height, final PlayerWeaponMenu playerWeaponMenu, final WeaponManager.WeaponItem weaponItem) {
        setSize(width, height);
        space(Dimension.buttonSpace / 5);


        initWeaponItemButtonStyle();
        currentItem = new WeaponItemButton(playerWeaponMenu, currentItemButtonStyle) {
            @Override
            protected void onPress() {
                GameSounds.getInstance().playSFX(GameSounds.CHOOSE_WEAPON_SFX_ID);
                if (weaponItem.weaponModel.capacity == 0) {
                    weaponItem.decrease(1);
                }
                playerWeaponMenu.setItemDisplayPaneVisible(false);
            }

            @Override
            public void onWeaponRemoved() {

            }
        };
        updateCurrentWeaponItem(weaponItem);

        speedCoolDownDisplay = new TimeDisplay(this, SupportItem.BOOST_SPEED);
        freezeCoolDownDisplay = new TimeDisplay(this, SupportItem.TIME_FREEZE);

        setTouchable(Touchable.childrenOnly);
    }

    private void initWeaponItemButtonStyle() {
        currentItemButtonStyle = new ImageButton.ImageButtonStyle();
        Drawable backgroundDrawable = GameImages.getInstance().getUiSkin().getDrawable(GameImages.KEY_SELECTED_RECTANGLE);
        backgroundDrawable.setMinWidth(Dimension.playerWeaponItemWidth);
        backgroundDrawable.setMinHeight(Dimension.playerWeaponItemHeight);
        currentItemButtonStyle.down = backgroundDrawable;
    }

    private Drawable createWeaponItemButtonIcon(int weaponId) {
        TextureRegionDrawable result = new TextureRegionDrawable(GameImages.getInstance().getIcon(weaponId));
        result.setMinWidth(Dimension.mediumIconSize);
        result.setMinHeight(Dimension.mediumIconSize);
        return result;
    }

    public void updateCurrentWeaponItem(WeaponManager.WeaponItem weaponItem) {
        WeaponManager.WeaponItem previousWeaponItem = currentItem.getWeaponItem();
        if(previousWeaponItem != null){
            previousWeaponItem.removeWeaponCapacityChangeListener(currentItem);
        }
        if(weaponItem == null){
            if(currentItem.getParent() != null){
                removeActor(currentItem);
            }
        }else {
            currentItemButtonStyle.imageUp = createWeaponItemButtonIcon(weaponItem.weaponModel.id);
            currentItem.capacityLabel.setText(weaponItem.weaponModel.capacity + "");
            currentItem.setWeaponItem(weaponItem);
            if(currentItem.getParent() == null){
                addActor(currentItem);
            }
        }
    }

    public TimeDisplay getSpeedCoolDownDisplay() {
        return speedCoolDownDisplay;
    }

    public TimeDisplay getFreezeCoolDownDisplay() {
        return freezeCoolDownDisplay;
    }
}
