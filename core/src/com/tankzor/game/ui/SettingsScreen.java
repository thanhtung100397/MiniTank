package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.TracksManager;
import com.tankzor.game.main.Tankzor;

/**
 * Created by Admin on 2/11/2017.
 */

public class SettingsScreen extends BaseScreen {
    public static final String SETTINGS_PREFERENCE_NAME = "settings";
    public static final String DIRECTORY = Gdx.files.getLocalStoragePath() + "shared_prefs";
    public static final String KEY_BACKGROUND_MUSIC_VOLUME = "0";
    public static final String KEY_SOUND_EFFECT_VOLUME = "1";
    public static final String KEY_ENVIRONMENT_EFFECT = "2";
    public static final String KEY_FPS_DISPLAY = "3";
    public static final String KEY_TANK_TRACK_EFFECT = "4";

    private Stage screenStage;
    private Table container;
    private Label tileLabel, soundLabel, backgroundMusicLabel, soundEffectLabel, graphicsLabel, environmentEffectLabel, FPSDisplayLabel, tankTrackEffectLabel, gameLabel;
    private Image separateLine1, separateLine2, separateLine3, background;
    private Slider backgroundMusicSlider, soundEffectSlider;
    private CheckBox environmentEffectCheckBox, FPSDisplayCheckBox, tankTrackEffectCheckBox;
    private TextButton resetGameButton, backButton;
    private ScrollPane scrollPane;

    public SettingsScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, batch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();
        Skin skin = GameImages.getInstance().getUiSkin();

        background = new Image(skin.getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        tileLabel = new Label("Settings", labelStyle);
        tileLabel.setFontScale(Dimension.largeFontScale);

        soundLabel = new Label("Sound", labelStyle);
        soundLabel.setFontScale(Dimension.quiteLargeFontScale);

        Drawable lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        separateLine1 = new Image(lineDrawable);

        backgroundMusicLabel = new Label("Background music", labelStyle);
        backgroundMusicLabel.setFontScale(Dimension.normalFontScale);
        soundEffectLabel = new Label("Sound effect", labelStyle);
        soundEffectLabel.setFontScale(Dimension.normalFontScale);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.getDrawable(GameImages.KEY_SLIDER_BACKGROUND);
        sliderStyle.knob = skin.getDrawable(GameImages.KEY_SLIDER_KNOB_BACKGROUND);
        sliderStyle.knobDown = skin.getDrawable(GameImages.KEY_SLIDER_KNOB_TOUCH_BACKGROUND);
        sliderStyle.knobOver = skin.getDrawable(GameImages.KEY_SLIDER_KNOB_TOUCH_BACKGROUND);

        backgroundMusicSlider = new Slider(0, Dimension.sliderWidth, 1, false, sliderStyle);
        backgroundMusicSlider.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameSounds.getInstance().playSFX(GameSounds.DISABLE_SFX_ID);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameSounds.getInstance().playSFX(GameSounds.ENABLE_SFX_ID);
                float volume = backgroundMusicSlider.getValue() / 100;
                GameSounds.getInstance().setBGMVolume(volume);
                Preferences settingsPreference = Gdx.app.getPreferences(SETTINGS_PREFERENCE_NAME);
                settingsPreference.putFloat(KEY_BACKGROUND_MUSIC_VOLUME, volume);
                settingsPreference.flush();
            }
        });
        soundEffectSlider = new Slider(0, Dimension.sliderWidth, 1, false, sliderStyle);
        soundEffectSlider.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameSounds.getInstance().playSFX(GameSounds.DISABLE_SFX_ID);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameSounds.getInstance().playSFX(GameSounds.ENABLE_SFX_ID);
                float volume = soundEffectSlider.getValue() / 100;
                GameSounds.getInstance().setSFXVolume(volume);
                Preferences settingsPreference = Gdx.app.getPreferences(SETTINGS_PREFERENCE_NAME);
                settingsPreference.putFloat(KEY_SOUND_EFFECT_VOLUME, volume);
                settingsPreference.flush();
            }
        });

        graphicsLabel = new Label("Graphics and Performance", labelStyle);
        graphicsLabel.setFontScale(Dimension.quiteLargeFontScale);

        lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        separateLine2 = new Image(lineDrawable);

        environmentEffectLabel = new Label("Environment effect", labelStyle);
        environmentEffectLabel.setFontScale(Dimension.normalFontScale);
        FPSDisplayLabel = new Label("FPS display", labelStyle);
        FPSDisplayLabel.setFontScale(Dimension.normalFontScale);
        tankTrackEffectLabel = new Label("Tank track effect", labelStyle);
        tankTrackEffectLabel.setFontScale(Dimension.normalFontScale);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.up = skin.getDrawable(GameImages.KEY_CHECK_BOX_BACKGROUND);
        checkBoxStyle.over = skin.getDrawable(GameImages.KEY_CHECK_BOX_TOUCH_BACKGROUND);
        checkBoxStyle.down = checkBoxStyle.over;
        checkBoxStyle.checkboxOn = skin.getDrawable(GameImages.KEY_CHECK_TICK);
        checkBoxStyle.font = GameImages.getInstance().getGameFont();

        environmentEffectCheckBox = new CheckBox("", checkBoxStyle);
        environmentEffectCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                boolean result = environmentEffectCheckBox.isChecked();
                AirManager.ENABLE_EFFECT = result;
                if (result) {
                    GameSounds.getInstance().playSFX(GameSounds.ENABLE_SFX_ID);
                } else {
                    GameSounds.getInstance().playSFX(GameSounds.DISABLE_SFX_ID);
                    getParent().getGameScreen().getAirManager().removeAllCloudAndSmoke();
                }
                Preferences settingsPreference = Gdx.app.getPreferences(SETTINGS_PREFERENCE_NAME);
                settingsPreference.putBoolean(KEY_ENVIRONMENT_EFFECT, result);
                settingsPreference.flush();
            }
        });
        FPSDisplayCheckBox = new CheckBox("", checkBoxStyle);
        FPSDisplayCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                boolean result = FPSDisplayCheckBox.isChecked();
                FPSDisplay.isEnable = result;
                if (result) {
                    GameSounds.getInstance().playSFX(GameSounds.ENABLE_SFX_ID);
                } else {
                    GameSounds.getInstance().playSFX(GameSounds.DISABLE_SFX_ID);
                }
                Preferences settingsPreference = Gdx.app.getPreferences(SETTINGS_PREFERENCE_NAME);
                settingsPreference.putBoolean(KEY_FPS_DISPLAY, result);
                settingsPreference.flush();
            }
        });
        tankTrackEffectCheckBox = new CheckBox("", checkBoxStyle);
        tankTrackEffectCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                boolean result = tankTrackEffectCheckBox.isChecked();
                TracksManager.ENABLE_EFFECT = result;
                if (result) {
                    GameSounds.getInstance().playSFX(GameSounds.ENABLE_SFX_ID);
                } else {
                    GameSounds.getInstance().playSFX(GameSounds.DISABLE_SFX_ID);
                    getParent().getGameScreen().getTracksManager().clear();
                }
                Preferences settingsPreference = Gdx.app.getPreferences(SETTINGS_PREFERENCE_NAME);
                settingsPreference.putBoolean(KEY_TANK_TRACK_EFFECT, result);
                settingsPreference.flush();
            }
        });

        gameLabel = new Label("Game", labelStyle);
        gameLabel.setFontScale(Dimension.quiteLargeFontScale);

        lineDrawable = skin.getDrawable(GameImages.KEY_SEPARATE_LINE);
        lineDrawable.setMinWidth(Dimension.separateLineWidth);
        separateLine3 = new Image(lineDrawable);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        textButtonStyle.font = GameImages.getInstance().getGameFont();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.disabledFontColor = Color.LIGHT_GRAY;
        textButtonStyle.overFontColor = Color.DARK_GRAY;

        resetGameButton = new TextButton("Reset game progress", textButtonStyle);
        Label resetGameLabel = resetGameButton.getLabel();
        resetGameLabel.setAlignment(Align.left);
        resetGameLabel.setFontScale(Dimension.normalFontScale);
        resetGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerProfile playerProfile = PlayerProfile.getInstance();
                playerProfile.deleteExistProfile();
                playerProfile.readProfile();
            }
        });

        backButton = new TextButton("Back", textButtonStyle);
        backButton.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        backButton.getLabel().setFontScale(Dimension.normalFontScale);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getParent().setScreen(getPreviousScreen());
            }
        });

//        if (!isExistSettingsPreference()) {
//            createDefaultSettings();
//        }
        applyUserSettings();
    }

    @Override
    public void setPreviousScreen(BaseScreen previousScreen) {
        super.setPreviousScreen(previousScreen);
        if (previousScreen == getParent().getPauseScreen()) {
            resetGameButton.setDisabled(true);
        } else {
            resetGameButton.setDisabled(false);
        }
    }

    private void applyUserSettings() {
        Preferences settingsPreference = Gdx.app.getPreferences(SETTINGS_PREFERENCE_NAME);

        float backgroundMusicVolume = settingsPreference.getFloat(KEY_BACKGROUND_MUSIC_VOLUME, 0.5f);
        GameSounds.getInstance().setBGMVolume(backgroundMusicVolume);
        backgroundMusicSlider.setValue(backgroundMusicVolume * 100);

        float soundEffectVolume = settingsPreference.getFloat(KEY_SOUND_EFFECT_VOLUME, 0.5f);
        GameSounds.getInstance().setSFXVolume(soundEffectVolume);
        soundEffectSlider.setValue(soundEffectVolume * 100);

        boolean environmentEffect = settingsPreference.getBoolean(KEY_ENVIRONMENT_EFFECT, true);
        environmentEffectCheckBox.setChecked(environmentEffect);
        AirManager.ENABLE_EFFECT = environmentEffect;

        boolean fps = settingsPreference.getBoolean(KEY_FPS_DISPLAY, false);
        FPSDisplayCheckBox.setChecked(fps);
        FPSDisplay.isEnable = fps;


        boolean trackEffect = settingsPreference.getBoolean(KEY_TANK_TRACK_EFFECT, true);
        tankTrackEffectCheckBox.setChecked(trackEffect);
        TracksManager.ENABLE_EFFECT = trackEffect;
    }

    @Override
    protected void addViews() {
        screenStage = new Stage();
        screenStage.addActor(background);

        container = new Table();

        container.add(tileLabel).colspan(2).align(Align.center).row();

        float padTop = Dimension.buttonSpace * 2;
        float padBottom = Dimension.buttonSpace / 1.5f;

        container.add(soundLabel).align(Align.left).colspan(2).padBottom(Dimension.buttonSpace).padTop(padTop).row();
        container.add(separateLine1).colspan(2).align(Align.left).padBottom(padBottom).row();
        container.add(backgroundMusicLabel).width(150).align(Align.left);
        container.add(backgroundMusicSlider).align(Align.right).width(450).height(60).pad(15).row();
        container.add(soundEffectLabel).width(150).align(Align.left);
        container.add(soundEffectSlider).align(Align.right).width(450).height(60).pad(15).row();

        container.add(graphicsLabel).align(Align.left).colspan(2).padBottom(Dimension.buttonSpace).padTop(padTop).row();
        container.add(separateLine2).colspan(2).align(Align.left).padBottom(padBottom).row();
        container.add(environmentEffectLabel).align(Align.left);
        container.add(environmentEffectCheckBox).align(Align.right).pad(15).row();
        container.add(tankTrackEffectLabel).align(Align.left);
        container.add(tankTrackEffectCheckBox).align(Align.right).pad(15).row();
        container.add(FPSDisplayLabel).align(Align.left);
        container.add(FPSDisplayCheckBox).align(Align.right).pad(15).row();

        container.add(gameLabel).align(Align.left).colspan(2).padBottom(Dimension.buttonSpace).padTop(padTop).row();
        container.add(separateLine3).colspan(2).align(Align.left).padBottom(padBottom).row();
        container.add(resetGameButton).align(Align.left).row();

        container.add(backButton).colspan(2).align(Align.center);

        scrollPane = new ScrollPane(container);
        scrollPane.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollPane.setDebug(true);
        scrollPane.setCancelTouchFocus(false);
        screenStage.addActor(scrollPane);
    }

    @Override
    public void show() {
        gameMultiplexer.addProcessor(screenStage);
//        scrollPane.scrollTo(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
    }

    @Override
    public void render(float delta) {
        screenStage.act(delta);
        screenStage.draw();
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }
}
