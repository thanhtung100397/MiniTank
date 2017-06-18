package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.common_value.research_model.ResearchModel;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.AreaWeapon;
import com.tankzor.game.game_object.movable_item.weapon.Bullet;
import com.tankzor.game.game_object.movable_item.weapon.WeaponManager;
import com.tankzor.game.game_object.support_item.SupportItem;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 1/21/2017.
 */

public class WorkshopScreen extends BaseScreen {
    private static final String BACK_BUTTON_LABEL = "Exit Workshop";
    private static final int DRAG_PIXEL_IGNORE = 20;

    private ScrollPane scrollPane;
    private Stage screenStage;
    private ScreenTitle screenTitle;
    private Image background;
    private VerticalGroup mainContainer;
    private TextButton backButton;
    private Image lineImage;
    private Array<WorkshopMenu> listWorkshopMenu;
    private WorkshopMenu researchMenu;
    private BaseScreen previousScreen;
    private AllyTankRepairButton allyTankRepairButton;

    private WarMachineManager warMachineManager;
    private WeaponManager weaponManager;

    public WorkshopScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    public void setWeaponManager(WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    public void setWarMachineManager(WarMachineManager warMachineManager) {
        this.warMachineManager = warMachineManager;
        allyTankRepairButton.setAlliesWarMachines(warMachineManager.getAlliesMovableMachines());
    }

    public void updateAllWorkshop() {
        screenTitle.update();
        int money = PlayerProfile.getInstance().getMoney();
        for (int i = 0; i < listWorkshopMenu.size; i++) {
            listWorkshopMenu.get(i).updateAllButton(money);
        }
        researchMenu.updateAllButton(PlayerProfile.getInstance().getStar());
    }

    public void updateAllWeaponItem() {
        screenTitle.update();
        int money = PlayerProfile.getInstance().getMoney();
        for (int i = 0; i < listWorkshopMenu.size; i++) {
            listWorkshopMenu.get(i).updateAllButton(money);
        }
    }

    public void setPreviousScreen(BaseScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void initViews() {
        float widthScreen = Gdx.graphics.getWidth();
        float heightScreen = Gdx.graphics.getHeight();

        Skin skin = GameImages.getInstance().getUiSkin();

        background = new Image(skin.getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, widthScreen, heightScreen);

        screenTitle = new ScreenTitle(0, heightScreen - Dimension.screenTitleHeight * 1.5f, widthScreen, Dimension.screenTitleHeight * 1.5f);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.down = GameImages.getInstance().getUiSkin().getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        textButtonStyle.font = GameImages.getInstance().getGameFont();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.disabledFontColor = Color.LIGHT_GRAY;
        textButtonStyle.overFontColor = Color.DARK_GRAY;

        createMenu(textButtonStyle);

        Drawable lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        lineImage = new Image(lineDrawable);

        backButton = new TextButton(BACK_BUTTON_LABEL, textButtonStyle);
        backButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerProfile.getInstance().savePlayerData();
                getParent().setScreen(previousScreen);
            }
        });
    }

    private void createMenu(TextButton.TextButtonStyle textButtonStyle) {
        mainContainer = new VerticalGroup();
        mainContainer.fill();
        mainContainer.space(Dimension.buttonSpace / 2);
        listWorkshopMenu = new Array<WorkshopMenu>();

        float itemButtonWidth = Gdx.graphics.getWidth();
        float itemButtonHeight = Dimension.buttonHeight;

        WorkshopMenu weaponMenu = new WorkshopMenu(1, "Basic Ammunition");
        for (int i = Bullet.NORMAL_BULLET; i <= Bullet.ARMOR_PIERCING_BULLET; i++) {
            weaponMenu.addItemButton(new WeaponItemButton(i, itemButtonWidth, itemButtonHeight, textButtonStyle));
        }
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(7, "Additional Ammunition");
        for (int i = Bullet.MISSILE_BULLET; i <= AreaWeapon.AIR_STRIKE; i++) {
            weaponMenu.addItemButton(new WeaponItemButton(i, itemButtonWidth, itemButtonHeight, textButtonStyle));
        }
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(16, "Allies");
        weaponMenu.addItemButton(new WeaponItemButton(SupportItem.ALLY_TANK, itemButtonWidth, itemButtonHeight, textButtonStyle));
        weaponMenu.addItemButton(new WeaponItemButton(SupportItem.ALLY_KAMIKAZE_TANK, itemButtonWidth, itemButtonHeight, textButtonStyle));
        weaponMenu.addItemButton(new WeaponItemButton(SupportItem.ALLY_ARTILLERY_TANK, itemButtonWidth, itemButtonHeight, textButtonStyle));
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(20, "Defence");
        weaponMenu.addItemButton(new UpgradeButton(SupportItem.TEMPORARY_ARMOR, itemButtonWidth, itemButtonHeight, textButtonStyle) {

            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                if (playerWarMachine != null) {
                    playerWarMachine.updateHitPoint(1);
                }
            }
        });
        weaponMenu.addItemButton(new UpgradeButton(SupportItem.PERMANENT_ARMOR, itemButtonWidth, itemButtonHeight, textButtonStyle) {

            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                if (playerWarMachine != null) {
                    playerWarMachine.updateHitPoint(1);
                }
            }
        });
        weaponMenu.addItemButton(new WeaponItemButton(SupportItem.FORCE_FIELD, itemButtonWidth, itemButtonHeight, textButtonStyle));
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(22, "Repair");
        weaponMenu.addItemButton(new WeaponItemButton(SupportItem.REPAIR_KIT, itemButtonWidth, itemButtonHeight, textButtonStyle));
        TankRepairButton tankRepairButton = new TankRepairButton(itemButtonWidth, itemButtonHeight, textButtonStyle);
        weaponMenu.addItemButton(tankRepairButton);
        allyTankRepairButton = new AllyTankRepairButton(itemButtonWidth, itemButtonHeight, textButtonStyle);
        weaponMenu.addItemButton(allyTankRepairButton);
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        weaponMenu = new WorkshopMenu(25, "Other");
        weaponMenu.addItemButton(new UpgradeButton(SupportItem.RADAR, itemButtonWidth, itemButtonHeight, textButtonStyle) {
            @Override
            void onAddButtonPress() {
                PlayerProfile playerProfileInstance = PlayerProfile.getInstance();
                if (playerProfileInstance.getResearchModel(ResearchModel.AIR_STRIKE_RESEARCH_ID).currentLevel >= 1) {
                    playerProfileInstance.getWeaponModel(AreaWeapon.AIR_STRIKE).unlocked = true;
                }
                super.onAddButtonPress();
            }
        });
        weaponMenu.addItemButton(new UpgradeButton(SupportItem.THERMOVISION, itemButtonWidth, itemButtonHeight, textButtonStyle) {
            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                if(warMachineManager != null) {
                    warMachineManager.getPlayerWarMachine().updateThermovisionBound();
                }
            }
        });
        weaponMenu.addItemButton(new UpgradeButton(SupportItem.UPGRADE_TANK, itemButtonWidth, itemButtonHeight, textButtonStyle) {
            @Override
            void onAddButtonPress() {
                if (warMachineManager != null) {
                    PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                    playerWarMachine.updateTankForm(WarMachine.HEAVY_TANK_TYPE);
                    playerWarMachine.updateHitPoint(3);
                }
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                playerProfile.addHP(3);
                playerProfile.getWeaponModel(Bullet.DOUBLE_NORMAL_BULLET).maxCapacity = 99;
                playerProfile.getWeaponModel(Bullet.DOUBLE_PLASMA_BULLET).maxCapacity = 99;
                playerProfile.getWeaponModel(Bullet.HIGH_EXPLOSIVE_BULLET).maxCapacity = 99;
                playerProfile.getWeaponModel(Bullet.ARMOR_PIERCING_BULLET).maxCapacity = 99;
                super.onAddButtonPress();
            }
        });
        weaponMenu.addItemButton(new WeaponItemButton(SupportItem.BOOST_SPEED, itemButtonWidth, itemButtonHeight, textButtonStyle));
        weaponMenu.addItemButton(new WeaponItemButton(SupportItem.TIME_FREEZE, itemButtonWidth, itemButtonHeight, textButtonStyle));
        weaponMenu.addItemButton(new UpgradeButton(SupportItem.LIFE_ITEM, itemButtonWidth, itemButtonHeight, textButtonStyle) {

            @Override
            void onAddButtonPress() {
                GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
                PlayerProfile.getInstance().addMoney(-PlayerProfile.getInstance().getWeaponModel(SupportItem.LIFE_ITEM).value);
                PlayerProfile.getInstance().addLife(1);
                updateAllWeaponItem();
            }
        });
        listWorkshopMenu.add(weaponMenu);
        mainContainer.addActor(weaponMenu);

        researchMenu = new WorkshopMenu(19, "Research");
        researchMenu.addItemButton(new ResearchButton(ResearchModel.ADDITIONAL_INTEREST_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.ROUNDS_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.ARTILLERY_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.MISSILES_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.MINES_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.DYNAMITE_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.ARMOR_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.FORCE_FIELD_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle) {
            @Override
            void onAddButtonPress() {
                super.onAddButtonPress();
                PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
                if (playerWarMachine != null) {
                    playerWarMachine.getForceField()
                            .setBaseRecoverTime(PlayerProfile.getInstance().getForceFieldRecoverTime());
                }
            }
        });
        researchMenu.addItemButton(new ResearchButton(ResearchModel.AIR_STRIKE_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        researchMenu.addItemButton(new ResearchButton(ResearchModel.ALLY_TANK_RESEARCH_ID, itemButtonWidth, itemButtonHeight, textButtonStyle));
        mainContainer.addActor(researchMenu);
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(background);
        screenStage.addActor(screenTitle);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.padTop(Dimension.buttonSpace / 3);
        buttonGroup.space(Dimension.buttonSpace / 2);
        buttonGroup.addActor(lineImage);
        buttonGroup.addActor(backButton);
        mainContainer.addActor(buttonGroup);

        scrollPane = new ScrollPane(mainContainer);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - screenTitle.getHeight());
        scrollPane.setTouchable(Touchable.enabled);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setSmoothScrolling(true);
        screenStage.addActor(scrollPane);
    }

    @Override
    public void render(float delta) {
        screenStage.act();
        screenStage.draw();
    }

    @Override
    public void show() {
//        scrollPane.scrollTo(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameMultiplexer.addProcessor(screenStage);
        updateAllWorkshop();
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
        warMachineManager = null;
        weaponManager = null;
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }

    public class WorkshopMenu extends VerticalGroup {
        Array<ItemButton> listItemButtons;

        public WorkshopMenu(int iconId, String title) {
            fill();

            space(Dimension.buttonSpace / 2);

            MenuTitle menuTitle = new MenuTitle(iconId, title, Gdx.graphics.getWidth(), Dimension.screenTitleHeight);
            addActor(menuTitle);

            listItemButtons = new Array<ItemButton>();
        }

        public void addItemButton(ItemButton itemButton) {
            listItemButtons.add(itemButton);
            addActor(itemButton);
        }

        public void updateAllButton(int value) {
            for (int i = 0; i < listItemButtons.size; i++) {
                listItemButtons.get(i).update(value);
            }
        }

        public int size() {
            return listItemButtons.size;
        }
    }

    private class MenuTitle extends Group {

        MenuTitle(int iconId, String title, float width, float height) {
            setSize(width, height);
            Skin skin = GameImages.getInstance().getUiSkin();
            Image background = new Image(skin.getDrawable(GameImages.KEY_TITLE_MENU_ITEM_BACKGROUND));
            background.setBounds(0, 0, width, height);
            addActor(background);

            Image icon = new Image(GameImages.getInstance().getIcon(iconId));
            Drawable drawable = icon.getDrawable();
            drawable.setMinWidth(Dimension.smallIconSize);
            drawable.setMinHeight(Dimension.smallIconSize);
            Label label = new Label(title, GameImages.getInstance().getLabelStyle());
            label.setFontScale(Dimension.quiteLargeFontScale);

            HorizontalGroup horizontalGroup = new HorizontalGroup();
            horizontalGroup.setBounds(0, 0, width, height);
            horizontalGroup.padLeft(Dimension.buttonSpace / 2);
            horizontalGroup.space(Dimension.buttonSpace / 2);
            horizontalGroup.align(Align.left);

            horizontalGroup.addActor(icon);
            horizontalGroup.addActor(label);
            addActor(horizontalGroup);
        }
    }

    private class ScreenTitle extends Group {
        Label moneyLabel, starLabel, lifeLabel;
        Image background;
        PlayerProfile playerProfile = PlayerProfile.getInstance();

        ScreenTitle(float x, float y, float width, float height) {
            setBounds(x, y, width, height);
            Skin skin = GameImages.getInstance().getUiSkin();
            background = new Image(skin.getDrawable(GameImages.KEY_LABEL_BACKGROUND));
            background.setBounds(0, 0, width, height);
            addActor(background);

            Table iconGroup = new Table();
            iconGroup.padLeft(Dimension.buttonSpace * 2);
            iconGroup.setBounds(0, 0, width, height);

            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = GameImages.getInstance().getGameFont();
            buttonStyle.fontColor = Color.WHITE;
            buttonStyle.overFontColor = Color.LIGHT_GRAY;
            TextButton backButton = new TextButton("<< BACK", buttonStyle);
            backButton.getLabel().setFontScale(Dimension.normalFontScale);
            backButton.setBounds(10, 0, Dimension.buttonWidth / 3, height);
            backButton.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    PlayerProfile.getInstance().savePlayerData();
                    WorkshopScreen.this.getParent().setScreen(previousScreen);
                }
            });

            Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();
            Label money = new Label("Coins: ", labelStyle);
            money.setFontScale(Dimension.normalFontScale);
            moneyLabel = new Label(playerProfile.getMoney() + "", labelStyle);
            moneyLabel.setAlignment(Align.center);
            moneyLabel.setFontScale(Dimension.normalFontScale);


            Label star = new Label("Stars: ", labelStyle);
            star.setFontScale(Dimension.normalFontScale);
            starLabel = new Label(playerProfile.getStar() + "", labelStyle);
            starLabel.setAlignment(Align.center);
            starLabel.setFontScale(Dimension.normalFontScale);

            Label life = new Label("Lives: ", labelStyle);
            life.setFontScale(Dimension.normalFontScale);
            lifeLabel = new Label(playerProfile.getLife() + "", labelStyle);
            lifeLabel.setAlignment(Align.center);
            lifeLabel.setFontScale(Dimension.normalFontScale);

            iconGroup.add(money);
            iconGroup.add(moneyLabel).padRight(Dimension.screenTitleHeight);
            iconGroup.add(star);
            iconGroup.add(starLabel).padRight(Dimension.screenTitleHeight);
            iconGroup.add(life);
            iconGroup.add(lifeLabel);
            iconGroup.align(Align.center);

            addActor(iconGroup);
            addActor(backButton);
        }

        public void update() {
            moneyLabel.setText(playerProfile.getMoney() + "");
            starLabel.setText(playerProfile.getStar() + "");
            lifeLabel.setText(playerProfile.getLife() + "");
        }
    }

    private abstract class ItemButton extends TextButton {
        ImageButton addButton;
        float xTouchDown, yTouchDown;
        boolean isDragged;
        boolean isTouchChild = false;
        int id;

        ItemButton(int id, float width, float height, TextButtonStyle style) {
            super("", style);
            this.id = id;
            setSize(width, height);

            padLeft(Dimension.buttonSpace);
            padRight(Dimension.buttonSpace);
            Label buttonLabel = getLabel();
            buttonLabel.setAlignment(Align.left);
            buttonLabel.setFontScale(Dimension.normalFontScale);

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (isTouchChild) {
                        return false;
                    }
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
                    if (Math.abs(xTouchDown - x) > DRAG_PIXEL_IGNORE || Math.abs(yTouchDown - y) > DRAG_PIXEL_IGNORE) {
                        isDragged = true;
                    }
                }
            });

            Skin skin = GameImages.getInstance().getUiSkin();
            ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
            Drawable drawable = skin.getDrawable(GameImages.KEY_ADD_BUTTON_PRESS_BACKGROUND);
            drawable.setMinWidth(Dimension.smallIconSize);
            drawable.setMinHeight(Dimension.smallIconSize);
            imageButtonStyle.imageDown = drawable;
            imageButtonStyle.imageOver = drawable;
            imageButtonStyle.imageDisabled = drawable;
            drawable = skin.getDrawable(GameImages.KEY_ADD_BUTTON_NORMAL_BACKGROUND);
            drawable.setMinWidth(Dimension.mediumIconSize);
            drawable.setMinHeight(Dimension.mediumIconSize);
            imageButtonStyle.imageUp = drawable;
            addButton = new ImageButton(imageButtonStyle);
            addButton.setVisible(false);
            addButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    xTouchDown = x;
                    yTouchDown = y;
                    isTouchChild = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    isTouchChild = false;
                    if (isDragged) {
                        isDragged = false;
                        return;
                    }
                    onAddButtonPress();
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (Math.abs(xTouchDown - x) > DRAG_PIXEL_IGNORE || Math.abs(yTouchDown - y) > DRAG_PIXEL_IGNORE) {
                        isDragged = true;
                    }
                }
            });

            setTouchable(Touchable.childrenOnly);
        }

        abstract void update(int value);

        abstract void onPress();

        abstract void onAddButtonPress();
    }

    private class UpgradeButton extends ItemButton {
        Label valueLabel, capacityLabel;

        UpgradeButton(int id, float width, float height, TextButtonStyle style) {
            super(id, width, height, style);

            Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

            capacityLabel = new Label("", labelStyle);
            capacityLabel.setAlignment(Align.center);
            capacityLabel.setFontScale(Dimension.normalFontScale);
            add(capacityLabel).align(Align.right).fillY().width(100);

            valueLabel = new Label("", labelStyle);
            valueLabel.setAlignment(Align.center);
            valueLabel.setFontScale(Dimension.normalFontScale);
            add(valueLabel).align(Align.right).width(120);

            add(addButton).padRight(Dimension.buttonSpace / 2);

            setText(PlayerProfile.getInstance().getWeaponModel(id).name);
        }

        @Override
        void update(int value) {
            WeaponModel weaponModel = PlayerProfile.getInstance().getWeaponModel(id);
            if (!weaponModel.unlocked || weaponModel.capacity == weaponModel.maxCapacity) {
                setDisabled(true);
                if (weaponModel.capacity != 0) {
                    capacityLabel.setText(weaponModel.capacity + "");
                } else {
                    capacityLabel.setText("");
                }
                valueLabel.setText("");
                addButton.setVisible(false);
                return;
            }
            setDisabled(false);
            valueLabel.setText(weaponModel.value + "");
            if (weaponModel.capacity != 0) {
                capacityLabel.setText(weaponModel.capacity + "");
            } else {
                capacityLabel.setText("");
            }
            addButton.setVisible(value > weaponModel.value);
        }

        @Override
        void onPress() {
            Tankzor parent = WorkshopScreen.this.getParent();
            WeaponDetailScreen weaponDetailScreen = parent.getWeaponDetailScreen();
            weaponDetailScreen.setWeaponModel(PlayerProfile.getInstance().getWeaponModel(id));
            parent.setScreen(weaponDetailScreen);
        }

        @Override
        void onAddButtonPress() {
            GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
            PlayerProfile playerProfile = PlayerProfile.getInstance();
            WeaponModel weaponModel = playerProfile.getWeaponModel(id);
            playerProfile.updateWeapon(id, 1);
            playerProfile.addMoney(-weaponModel.value);

            updateAllWeaponItem();
        }
    }

    private class TankRepairButton extends UpgradeButton {
        int currentPrice = -1;

        TankRepairButton(float width, float height, TextButtonStyle style) {
            super(SupportItem.REPAIR_TANK, width, height, style);
        }

        void calculateRepairCost() {
            if (warMachineManager == null) {
                currentPrice = 0;
                return;
            }
            PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
            int hitPoint = playerWarMachine.getHitPoint();
            int maxHitPoint = playerWarMachine.getMaxHitPoint();
            if (hitPoint == maxHitPoint) {
                currentPrice = 0;
                return;
            }
            currentPrice = (maxHitPoint - hitPoint) * PlayerProfile.getInstance().getWeaponModel(id).value;
        }

        @Override
        void update(int value) {
            if (currentPrice == -1) {
                calculateRepairCost();
            }
            if (currentPrice > 0) {
                setDisabled(false);
                valueLabel.setText(currentPrice + "");
                if (currentPrice <= value) {
                    addButton.setVisible(true);
                } else {
                    addButton.setVisible(false);
                }
            } else {
                setDisabled(true);
                valueLabel.setText("");
                addButton.setVisible(false);
            }
        }

        @Override
        void onAddButtonPress() {
            GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
            PlayerWarMachine playerWarMachine = warMachineManager.getPlayerWarMachine();
            playerWarMachine.setHitPoint(playerWarMachine.getMaxHitPoint());
            PlayerProfile.getInstance().addMoney(-currentPrice);

            updateAllWeaponItem();
        }
    }

    private class AllyTankRepairButton extends UpgradeButton {
        int currentPrice = -1;
        Array<MovableWarMachine> alliesWarMachines;

        AllyTankRepairButton(float width, float height, TextButtonStyle style) {
            super(SupportItem.REPAIR_ALLY_TANK, width, height, style);
        }

        void setAlliesWarMachines(Array<MovableWarMachine> alliesWarMachines) {
            this.alliesWarMachines = alliesWarMachines;
        }

        void calculateRepairCost() {
            if (alliesWarMachines == null) {
                currentPrice = 0;
                return;
            }
            int totalHitPointLost = 0;
            for (int i = 0; i < alliesWarMachines.size; i++) {
                WarMachine warMachine = alliesWarMachines.get(i);
                totalHitPointLost += warMachine.getMaxHitPoint() - warMachine.getHitPoint();
            }
            if (totalHitPointLost == 0) {
                currentPrice = 0;
                return;
            }
            currentPrice = totalHitPointLost * PlayerProfile.getInstance().getWeaponModel(id).value;
        }

        @Override
        void update(int value) {
            if (currentPrice == -1) {
                calculateRepairCost();
            }
            if (currentPrice > 0) {
                setDisabled(false);
                valueLabel.setText(currentPrice + "");
                if (currentPrice <= value) {
                    addButton.setVisible(true);
                } else {
                    addButton.setVisible(false);
                }
            } else {
                setDisabled(true);
                valueLabel.setText("");
                addButton.setVisible(false);
            }
        }

        @Override
        void onAddButtonPress() {
            GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
            for (int i = 0; i < alliesWarMachines.size; i++) {
                WarMachine warMachine = alliesWarMachines.get(i);
                warMachine.setHitPoint(warMachine.getMaxHitPoint());
            }
            PlayerProfile.getInstance().addMoney(-currentPrice);

            updateAllWeaponItem();
        }
    }

    private class WeaponItemButton extends ItemButton {
        Label valueLabel;
        Label capacityLabel;

        WeaponItemButton(int id, float width, float height, TextButtonStyle textButtonStyle) {
            super(id, width, height, textButtonStyle);

            Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

            capacityLabel = new Label("", labelStyle);
            capacityLabel.setAlignment(Align.center);
            capacityLabel.setFontScale(Dimension.normalFontScale);
            add(capacityLabel).align(Align.right).fillY().width(100);

            valueLabel = new Label("", labelStyle);
            valueLabel.setAlignment(Align.center);
            valueLabel.setFontScale(Dimension.normalFontScale);
            add(valueLabel).align(Align.right).width(120);

            add(addButton).padRight(Dimension.buttonSpace / 2);

            setText(PlayerProfile.getInstance().getWeaponModel(id).name);
        }

        @Override
        public void update(int value) {
            WeaponModel weaponModel = PlayerProfile.getInstance().getWeaponModel(id);
            if (!weaponModel.unlocked || weaponModel.capacity == weaponModel.maxCapacity) {
                setDisabled(true);
                if (weaponModel.capacity != 0) {
                    capacityLabel.setText(weaponModel.capacity + "");
                } else {
                    capacityLabel.setText("");
                }
                valueLabel.setText("");
                addButton.setVisible(false);
                return;
            }
            setDisabled(false);
            valueLabel.setText(weaponModel.value + "");
            if (weaponModel.capacity != 0) {
                capacityLabel.setText(weaponModel.capacity + "");
            } else {
                capacityLabel.setText("");
            }
            addButton.setVisible(value > weaponModel.value);
        }

        @Override
        void onPress() {
            Tankzor parent = WorkshopScreen.this.getParent();
            WeaponDetailScreen weaponDetailScreen = parent.getWeaponDetailScreen();
            weaponDetailScreen.setWeaponModel(PlayerProfile.getInstance().getWeaponModel(id));
            parent.setScreen(weaponDetailScreen);
        }

        @Override
        void onAddButtonPress() {
            GameSounds.getInstance().playSFX(GameSounds.PURCHASE_SFX_ID);
            int value;
            WeaponModel weaponModel = PlayerProfile.getInstance().getWeaponModel(id);

            if (weaponModel.maxCapacity - weaponModel.purchaseCount >= weaponModel.capacity) {
                value = weaponModel.value;
                if (weaponManager == null) {
                    weaponModel.capacity += weaponModel.purchaseCount;
                } else {
                    weaponManager.updateWeaponItem(id, weaponModel.purchaseCount);
                }
            } else {
                int purchaseAmount = weaponModel.maxCapacity - weaponModel.capacity;
                value = (int) (weaponModel.value * (purchaseAmount * 1.0f / weaponModel.purchaseCount));
                if (weaponManager == null) {
                    weaponModel.capacity += purchaseAmount;
                } else {
                    weaponManager.updateWeaponItem(id, purchaseAmount);
                }
            }
            PlayerProfile.getInstance().addMoney(-value);

            updateAllWeaponItem();
        }
    }

    private class ResearchButton extends ItemButton {
        Label valueLabel, levelLabel;
        Image starImage;

        public ResearchButton(int id, float width, float height, TextButtonStyle style) {
            super(id, width, height, style);

            Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

            valueLabel = new Label("", labelStyle);
            valueLabel.setAlignment(Align.right);
            valueLabel.setFontScale(Dimension.normalFontScale);

            levelLabel = new Label("", labelStyle);
            levelLabel.setAlignment(Align.center);
            levelLabel.setFontScale(Dimension.normalFontScale);

            starImage = new Image(GameImages.getInstance().getIcon(31));
            Drawable drawable = starImage.getDrawable();
            drawable.setMinWidth(Dimension.starIconSize);
            drawable.setMinHeight(Dimension.starIconSize);

            add(levelLabel).align(Align.right).width(100);
            add(valueLabel).align(Align.right).width(180);
            add(starImage).align(Align.right).padRight(Dimension.starIconSize);
            add(addButton).padRight(Dimension.buttonSpace / 2);

            setText(PlayerProfile.getInstance().getResearchModel(id).name);
        }

        @Override
        public void update(int star) {
            ResearchModel researchModel = PlayerProfile.getInstance().getResearchModel(id);
            levelLabel.setText(researchModel.currentLevel + "");
            if (researchModel.isMaximumLevel()) {
                setDisabled(true);
                valueLabel.setText("Max");
                starImage.setVisible(false);
                addButton.setVisible(false);
                return;
            }
            setDisabled(false);
            int starNeeded = researchModel.getStarOfCurrentLevel();
            valueLabel.setText(starNeeded + "");
            starImage.setVisible(true);
            addButton.setVisible(star >= starNeeded);
        }

        @Override
        void onPress() {
            Tankzor parent = WorkshopScreen.this.getParent();
            DocumentScreen documentScreen = parent.getDocumentScreen();
            documentScreen.setPreviousScreen(WorkshopScreen.this);
            ResearchModel researchModel = PlayerProfile.getInstance().getResearchModel(id);
            documentScreen.setTitle(researchModel.name);
            documentScreen.setContent(researchModel.description);
            parent.setScreen(documentScreen);
        }

        @Override
        void onAddButtonPress() {
            GameSounds.getInstance().playSFX(GameSounds.RESEARCH_SFX_ID);
            PlayerProfile playerProfile = PlayerProfile.getInstance();
            ResearchModel researchModel = PlayerProfile.getInstance().getResearchModel(id);
            playerProfile.addStar(-researchModel.getStarOfCurrentLevel());
            researchModel.levelUp();
            updateAllWorkshop();
        }
    }
}
