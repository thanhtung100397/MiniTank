package com.tankzor.game.game_object.movable_item.weapon;

import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.support_item.SupportItem;

/**
 * Created by Admin on 11/24/2016.
 */

public class WeaponManager {
    private Array<WeaponItem> listWeaponItems;
    private int currentWeaponItemIndex = -1;
    private WeaponListener weaponListener;
    private OnNewWeaponAddedListener weaponUpdateListener;
    private PlayerWarMachine playerWarMachine;

    public WeaponManager() {
        listWeaponItems = new Array<WeaponItem>();
    }

    public void setPlayerWarMachine(PlayerWarMachine playerWarMachine) {
        this.playerWarMachine = playerWarMachine;
    }

    public void setWeaponListener(WeaponListener weaponListener) {
        this.weaponListener = weaponListener;
    }

    public void setWeaponUpdateListener(OnNewWeaponAddedListener weaponUpdateListener) {
        this.weaponUpdateListener = weaponUpdateListener;
    }

    public void setCurrentWeaponItem(WeaponItem weaponItem) {
        for (int i = 0; i < listWeaponItems.size; i++) {
            if (listWeaponItems.get(i) == weaponItem) {
                this.currentWeaponItemIndex = i;
                if (weaponListener != null) {
                    weaponListener.onCurrentWeaponSwitch(weaponItem);
                }
                return;
            }
        }
    }

    public void setCurrentWeaponItemIndex(int index) {
        if (index < 0 || index >= listWeaponItems.size) {
            return;
        }
        currentWeaponItemIndex = index;
        if (weaponListener != null) {
            weaponListener.onCurrentWeaponSwitch(getCurrentWeaponItem());
        }
    }

    public Array<WeaponItem> getListWeaponItems() {
        return listWeaponItems;
    }

    public boolean isCurrentWeaponReady() {
        WeaponItem currentWeaponItem = getCurrentWeaponItem();
        if (currentWeaponItem == null || currentWeaponItem.weaponModel.capacity == 0) {
            if (listWeaponItems.size == 0 && weaponListener != null) {
                weaponListener.onNoWeaponLeft();
            } else {
                removeWeaponItem(currentWeaponItem);
                playerWarMachine.fireButtonPress = false;
            }
            return false;
        }
        return currentWeaponItem.currentReloadTime >= currentWeaponItem.weaponModel.reloadTime;
    }

    public boolean nextWeapon() {
        if (currentWeaponItemIndex == -1) {
            return false;
        }
        if(listWeaponItems.size == 0){
            currentWeaponItemIndex = -1;
            return false;
        }
        if (currentWeaponItemIndex >= listWeaponItems.size - 1) {
            currentWeaponItemIndex = 0;
        } else {
            currentWeaponItemIndex++;
        }
        if (weaponListener != null) {
            weaponListener.onCurrentWeaponSwitch(getCurrentWeaponItem());
        }
        return true;
    }

    public WeaponItem getWeaponItemByID(int id) {
        for (int i = 0; i < listWeaponItems.size; i++) {
            if (listWeaponItems.get(i).weaponModel.id == id) {
                return listWeaponItems.get(i);
            }
        }
        return null;
    }

    public void initListWeaponItems(Array<WeaponModel> listModels) {
        clear();
        for (int i = Bullet.NORMAL_BULLET; i <= SupportItem.TIME_FREEZE; i++) {
            WeaponModel weaponModel = listModels.get(i - 1);
            if (weaponModel.capacity == 0) {
                continue;
            }
            listWeaponItems.add(createWeaponItem(weaponModel));
            if (currentWeaponItemIndex == -1 && i <= AreaWeapon.AIR_STRIKE) {
                currentWeaponItemIndex = i - 1;
            }
        }
    }

    public void clear() {
        for (int i = listWeaponItems.size - 1; i >= 0; i--) {
            listWeaponItems.removeIndex(i).notifyWeaponRemovedToAllListeners();
        }
    }

    private WeaponItem createWeaponItem(WeaponModel weaponModel) {
        return new WeaponItem(weaponModel);
    }

    private void removeWeaponItem(WeaponItem weaponItem) {
        if (listWeaponItems.size == 0) {
            if (weaponListener != null) {
                weaponListener.onNoWeaponLeft();
            }
            return;
        }
        int weaponIndex;
        for (weaponIndex = 0; weaponIndex < listWeaponItems.size; weaponIndex++) {
            if (listWeaponItems.get(weaponIndex) == weaponItem) {
                listWeaponItems.removeIndex(weaponIndex);
                break;
            }
        }
        weaponItem.notifyWeaponRemovedToAllListeners();
        if (weaponIndex == listWeaponItems.size) {
            nextWeapon();
        }
        if(weaponListener != null) {
            weaponListener.onCurrentWeaponSwitch(getCurrentWeaponItem());
        }
    }

    public WeaponItem getCurrentWeaponItem() {
        if (currentWeaponItemIndex == -1) {
            return null;
        }
        return listWeaponItems.get(currentWeaponItemIndex);
    }

    public void updateWeaponItem(int weaponId, int amount) {
        if (amount == 0) {
            return;
        }
        WeaponItem weaponItem = getWeaponItemByID(weaponId);
        if (weaponItem == null) {
            if (amount > 0) {
                weaponItem = createWeaponItem(PlayerProfile.getInstance().getWeaponModel(weaponId));
                weaponItem.increase(amount);
                insertNewWeaponItem(weaponItem);
            }
        } else if (amount > 0) {
            weaponItem.increase(amount);
        } else {
            weaponItem.decrease(amount);
        }
    }

    private void insertNewWeaponItem(WeaponItem weaponItem) {
        int weaponId = weaponItem.weaponModel.id;
        for (int i = 0; i < listWeaponItems.size; i++) {
            int tempWeaponId = listWeaponItems.get(i).weaponModel.id;
            if (tempWeaponId > weaponId) {
                listWeaponItems.insert(i, weaponItem);
                if (i <= currentWeaponItemIndex) {
                    currentWeaponItemIndex++;
                }
                if (weaponUpdateListener != null) {
                    weaponUpdateListener.onNewWeaponAdded(weaponItem, i);
                }
                return;
            }
        }
        listWeaponItems.add(weaponItem);
        if (weaponUpdateListener != null) {
            weaponUpdateListener.onNewWeaponAdded(weaponItem, listWeaponItems.size);
        }
        if (listWeaponItems.size == 1) {
            setCurrentWeaponItemIndex(0);
        }
    }

    public WeaponModel getBestBulletModel() {
        for (int i = Bullet.MISSILE_BULLET - 1; i > 0; i--) {
            WeaponModel weaponModel = listWeaponItems.get(i).weaponModel;
            if (weaponModel.capacity != 0) {
                return weaponModel;
            }
        }
        return listWeaponItems.get(0).weaponModel;
    }

    public class WeaponItem {
        public WeaponModel weaponModel;
        public float currentReloadTime;
        private Array<OnWeaponCapacityListener> listWeaponCapacityChangeListeners;

        public WeaponItem(WeaponModel weaponModel) {
            this.weaponModel = weaponModel;
            this.currentReloadTime = weaponModel.reloadTime;
            this.listWeaponCapacityChangeListeners = new Array<OnWeaponCapacityListener>(1);
        }

        public void registerWeaponCapacityChangeListener(OnWeaponCapacityListener weaponCapacityChangeListener) {
            this.listWeaponCapacityChangeListeners.add(weaponCapacityChangeListener);
            weaponCapacityChangeListener.onWeaponChangeCapacity(weaponModel.capacity);
        }

        public void removeWeaponCapacityChangeListener(OnWeaponCapacityListener weaponCapacityChangeListener) {
            this.listWeaponCapacityChangeListeners.removeValue(weaponCapacityChangeListener, true);
        }

        private void notifyWeaponCapacityChangeToAllListeners(int newCapacity) {
            for (int i = 0; i < listWeaponCapacityChangeListeners.size; i++) {
                listWeaponCapacityChangeListeners.get(i).onWeaponChangeCapacity(newCapacity);
            }
        }

        private void notifyWeaponRemovedToAllListeners() {
            for (int i = 0; i < listWeaponCapacityChangeListeners.size; i++) {
                listWeaponCapacityChangeListeners.get(i).onWeaponRemoved();
            }
        }

        public void decrease(int number) {
            if (weaponModel.capacity >= number) {
                weaponModel.capacity -= number;
                notifyWeaponCapacityChangeToAllListeners(weaponModel.capacity);
            }
        }

        public void increase(int number) {
            weaponModel.capacity += number;
            notifyWeaponCapacityChangeToAllListeners(weaponModel.capacity);
        }

        public void remove() {
            removeWeaponItem(this);
        }
    }
}
