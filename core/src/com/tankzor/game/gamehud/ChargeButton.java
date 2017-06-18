package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.game_object.movable_item.weapon.OnWeaponCapacityListener;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;

/**
 * Created by Admin on 1/26/2017.
 */

public abstract class ChargeButton extends Group implements OnWeaponCapacityListener{
    protected Label chargeCapacityLabel;
    protected ImageButton rootButton;
    protected WeaponManager.WeaponItem weaponItem;

    public ChargeButton(ImageButton.ImageButtonStyle imageButtonStyle) {
        rootButton = new ImageButton(imageButtonStyle);
        rootButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return !rootButton.isDisabled();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onPress();
            }
        });
        rootButton.setDisabled(true);
        addActor(rootButton);

        chargeCapacityLabel = new Label("", GameImages.getInstance().getLabelStyle());
        chargeCapacityLabel.setFontScale(Dimension.smallFontScale);
        chargeCapacityLabel.setAlignment(Align.center);
        float labelSize = chargeCapacityLabel.getHeight() * 1.2f;
        chargeCapacityLabel.setBounds(0 + rootButton.getWidth() - labelSize,
                0 + rootButton.getHeight() - labelSize,
                labelSize,
                labelSize);
        addActor(chargeCapacityLabel);

        setTouchable(Touchable.childrenOnly);

        setSize(rootButton.getWidth(), rootButton.getHeight());
    }

    public void setWeaponItem(WeaponManager.WeaponItem weaponItem) {
        this.weaponItem = weaponItem;
        if(weaponItem != null){
            weaponItem.registerWeaponCapacityChangeListener(this);
        }
    }

    protected void onPress(){
        if(doEffect()){
            weaponItem.decrease(1);
            if (weaponItem.weaponModel.capacity == 0){
                weaponItem.remove();
            }
        }
    }

    public abstract boolean doEffect();

    @Override
    public void onWeaponChangeCapacity(int newCapacity) {
        chargeCapacityLabel.setText(newCapacity+"");
        rootButton.setDisabled(false);
    }

    @Override
    public void onWeaponRemoved() {
        chargeCapacityLabel.setText("");
        rootButton.setDisabled(true);
    }
}
