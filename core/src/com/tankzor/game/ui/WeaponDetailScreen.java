package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 1/22/2017.
 */

public class WeaponDetailScreen extends BaseScreen {
    private Stage screenStage;
    private TankBackground tankBackground;
    private Image icon;
    private Label nameLabel, valueLabel, descriptionLabel, reloadTimeLabel, damageLabel, explosionLabel;
    private Label price, reloadTime, damage, explosion;
    private TextButton backButton;
    private Image lineImage;
    private ScrollPane scrollPane;
    private VerticalGroup rootGroup;

    public WeaponDetailScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        tankBackground = new TankBackground();

        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

        icon = new Image();

        nameLabel = new Label("", labelStyle);
        nameLabel.setFontScale(Dimension.largeFontScale);

        price = new Label("Price: ", labelStyle);
        price.setFontScale(Dimension.normalFontScale);
        valueLabel = new Label("0", labelStyle);
        valueLabel.setFontScale(Dimension.normalFontScale);

        descriptionLabel = new Label("0", labelStyle);
        descriptionLabel.setFontScale(Dimension.normalFontScale);
        descriptionLabel.setWrap(true);

        reloadTime = new Label("Reload time: ", labelStyle);
        reloadTime.setFontScale(Dimension.normalFontScale);
        reloadTimeLabel = new Label("0", labelStyle);
        reloadTimeLabel.setFontScale(Dimension.normalFontScale);

        damage = new Label("Damage: ", labelStyle);
        damage.setFontScale(Dimension.normalFontScale);
        damageLabel = new Label("0", labelStyle);
        damageLabel.setFontScale(Dimension.normalFontScale);

        explosion = new Label("Explosion: ", labelStyle);
        explosion.setFontScale(Dimension.normalFontScale);
        explosionLabel = new Label("0x0", labelStyle);
        explosionLabel.setFontScale(Dimension.normalFontScale);

        Skin skin = GameImages.getInstance().getUiSkin();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        Drawable lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        lineImage = new Image(lineDrawable);

        backButton = new TextButton("Back", buttonStyle);
        backButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getParent().setScreen(getParent().getWorkshopScreen());
            }
        });
    }

    public void setWeaponModel(WeaponModel weaponModel) {
        if (weaponModel.id <= 25) {
            TextureRegionDrawable iconDrawable = new TextureRegionDrawable(GameImages.getInstance().getIcon(weaponModel.id));
            iconDrawable.setMinWidth(Dimension.mediumIconSize);
            iconDrawable.setMinHeight(Dimension.mediumIconSize);
            icon.setDrawable(iconDrawable);
        }

        nameLabel.setText(weaponModel.name);
        valueLabel.setText(weaponModel.value + " coin(s)");
        descriptionLabel.setText(weaponModel.description);
        reloadTimeLabel.setText((weaponModel.reloadTime == -1) ? "N/A" : weaponModel.reloadTime + "");
        damageLabel.setText((weaponModel.damage == -1) ? "N/A" : weaponModel.damage + "");
        explosionLabel.setText((weaponModel.explosion == -1) ? "N/A" : (weaponModel.explosion + 1) + "x" + (weaponModel.explosion + 1));
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(tankBackground);

        rootGroup = new VerticalGroup();
        rootGroup.fill();
        rootGroup.align(Align.left);
        rootGroup.pad(Dimension.buttonSpace * 2);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.space(Dimension.buttonSpace);
        horizontalGroup.pad(Dimension.buttonSpace * 2);
        horizontalGroup.addActor(icon);
        horizontalGroup.addActor(nameLabel);
        rootGroup.addActor(horizontalGroup);

        rootGroup.addActor(descriptionLabel);

        Table table = new Table();
        table.align(Align.left);
        table.padTop(Dimension.buttonSpace * 4);
        table.add(price).align(Align.left).width(500);
        table.add(valueLabel).align(Align.left);
        table.row();
        table.add(reloadTime).align(Align.left).width(500);
        table.add(reloadTimeLabel).align(Align.left);
        table.row();
        table.add(damage).align(Align.left).width(500);
        table.add(damageLabel).align(Align.left);
        table.row();
        table.add(explosion).align(Align.left).width(500);
        table.add(explosionLabel).align(Align.left);
        rootGroup.addActor(table);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.padTop(Dimension.buttonSpace);
        buttonGroup.space(Dimension.buttonSpace);
        buttonGroup.addActor(lineImage);
        buttonGroup.addActor(backButton);
        rootGroup.addActor(buttonGroup);

        scrollPane = new ScrollPane(rootGroup);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 100);
        scrollPane.setTouchable(Touchable.enabled);
        screenStage.addActor(scrollPane);
    }

    @Override
    public void show() {
        gameMultiplexer.addProcessor(screenStage);
        float heightScreen = Gdx.graphics.getHeight();
        scrollPane.scrollTo(0, rootGroup.getHeight() - heightScreen, Gdx.graphics.getWidth(), heightScreen);
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
    }

    @Override
    public void render(float delta) {
        screenStage.act();
        screenStage.draw();
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }
}
