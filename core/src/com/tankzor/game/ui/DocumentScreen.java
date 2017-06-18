package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 1/24/2017.
 */

public class DocumentScreen extends BaseScreen {
    private Stage screenStage;
    private TankBackground tankBackground;
    private Label titleLabel, contentLabel;
    private TextButton backButton;
    private Image lineImage;
    private ScrollPane scrollPane;
    private VerticalGroup rootGroup;

    public DocumentScreen(Tankzor parent,
                          Viewport viewport,
                          SpriteBatch batch,
                          InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        tankBackground = new TankBackground();

        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

        titleLabel = new Label("", labelStyle);
        titleLabel.setFontScale(Dimension.largeFontScale);
        titleLabel.setWrap(true);

        contentLabel = new Label("", labelStyle);
        contentLabel.setFontScale(Dimension.normalFontScale);
        contentLabel.setWrap(true);
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
        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getParent().setScreen(getPreviousScreen());
            }
        });
    }

    public void setTitle(String tile){
        titleLabel.setText(tile);
    }

    public void setContent(String content){
        contentLabel.setText(content);
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(tankBackground);

        rootGroup = new VerticalGroup();
        rootGroup.fill();
        rootGroup.align(Align.left);
        rootGroup.pad(20);
        rootGroup.space(20);

        rootGroup.addActor(titleLabel);
        rootGroup.addActor(contentLabel);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.padTop(20);
        buttonGroup.space(Dimension.buttonSpace);
        buttonGroup.addActor(lineImage);
        buttonGroup.addActor(backButton);
        rootGroup.addActor(buttonGroup);

        scrollPane = new ScrollPane(rootGroup);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 100);
        screenStage.addActor(scrollPane);
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

    @Override
    public void show() {
        super.show();
        gameMultiplexer.addProcessor(screenStage);
        float heightScreen = Gdx.graphics.getHeight();
        scrollPane.scrollTo(0, rootGroup.getHeight() - heightScreen, Gdx.graphics.getWidth(), heightScreen);
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
    }
}
