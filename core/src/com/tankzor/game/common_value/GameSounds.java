package com.tankzor.game.common_value;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Admin on 2/12/2017.
 */

public class GameSounds {
    public static final int FIRE_SFX_ID = 0;
    public static final int BULLET_EXPLOSION_SFX_ID = 1;
    public static final int EXPLOSION_SFX_ID = 2;
    public static final int MISSILE_LAUNCH_SFX_ID = 3;
    public static final int ARTILLERY_LAUNCH_SFX_ID = 4;
    public static final int CHOOSE_WEAPON_SFX_ID = 5;
    public static final int CHOOSE_TARGET_SFX_ID = 6;
    public static final int FORCE_FIELD_HIT_SFX_ID = 7;
    public static final int DISABLE_SFX_ID = 8;
    public static final int ENABLE_SFX_ID = 9;
    public static final int PURCHASE_SFX_ID = 10;
    public static final int RESEARCH_SFX_ID = 11;
    public static final int PICK_ITEM_SFX_ID = 12;

    private static GameSounds instance = new GameSounds();

    private Sound[] shotSFX;
    private Sound bulletExplosionSFX;
    private Sound explosionSFX;
    private Sound missileLaunchSFX;
    private Sound artilleryLaunchSFX;
    private Sound pickItemSFX;
    private Sound bomberFlySFX;
    private Sound bipSFX;
    private Sound chooseWeaponSFX;
    private Sound chooseTargetSFX;
    private Sound forceFieldHitSFX;
    private Sound disableSFX;
    private Sound enableSFX;
    private Sound purchaseSFX;
    private Sound researchSFX;
    private Music backgroundMusic;
    private float SFXVolume;
    private float BGMVolume;

    public static GameSounds getInstance() {
        return instance;
    }

    private GameSounds() {
        shotSFX = new Sound[2];
        for (int i = 0; i < 2; i++) {
            shotSFX[i] = Gdx.audio.newSound(Gdx.files.internal("sfx/shot" + i + "_sfx.wav"));
        }
        bulletExplosionSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/bullet_explosion_sfx.wav"));
        explosionSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/explosion_sfx.wav"));
        missileLaunchSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/missile_launch_sfx.wav"));
        artilleryLaunchSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/artillery_launch_sfx.wav"));
        pickItemSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/pick_item_sfx.ogg"));
        bomberFlySFX = Gdx.audio.newSound(Gdx.files.internal("sfx/bomber_fly_sfx.wav"));
        bipSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/bip_sfx.wav"));
        chooseWeaponSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/choose_weapon_sfx.wav"));
        chooseTargetSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/choose_target_sfx.wav"));
        forceFieldHitSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/force_field_hit_sfx.wav"));
        disableSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/disable_sfx.wav"));
        enableSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/enable_sfx.wav"));
        purchaseSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/purchase_sfx.wav"));
        researchSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/research_sfx.wav"));
    }

    public void prepareBackgroundMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music/track0" + MathUtils.random(1, 9) + ".mid"));
        backgroundMusic.setVolume(BGMVolume);
        backgroundMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                backgroundMusic.dispose();
                prepareBackgroundMusic();
                playBGM();
            }
        });
    }

    public void playSFX(int id) {
        switch (id) {
            case FIRE_SFX_ID: {
                shotSFX[MathUtils.random(1)].play(SFXVolume);
            }
            break;

            case BULLET_EXPLOSION_SFX_ID: {
                bulletExplosionSFX.play(SFXVolume);
            }
            break;

            case EXPLOSION_SFX_ID: {
                explosionSFX.play(SFXVolume);
            }
            break;

            case MISSILE_LAUNCH_SFX_ID: {
                missileLaunchSFX.play(SFXVolume);
            }
            break;

            case ARTILLERY_LAUNCH_SFX_ID: {
                artilleryLaunchSFX.play(SFXVolume);
            }
            break;

            case PICK_ITEM_SFX_ID: {
                pickItemSFX.play(SFXVolume);
            }
            break;

            case CHOOSE_WEAPON_SFX_ID: {
                chooseWeaponSFX.play(SFXVolume);
            }
            break;

            case CHOOSE_TARGET_SFX_ID: {
                chooseTargetSFX.play(SFXVolume);
            }
            break;

            case FORCE_FIELD_HIT_SFX_ID: {
                forceFieldHitSFX.play(SFXVolume);
            }
            break;

            case ENABLE_SFX_ID: {
                enableSFX.play(SFXVolume);
            }
            break;

            case DISABLE_SFX_ID: {
                disableSFX.play(SFXVolume);
            }
            break;

            case PURCHASE_SFX_ID: {
                purchaseSFX.play(SFXVolume);
            }
            break;

            case RESEARCH_SFX_ID: {
                researchSFX.play(SFXVolume);
            }
            break;

            default: {
                break;
            }
        }
    }

    public long playBomberFlySFX() {
        return bomberFlySFX.loop(SFXVolume);
    }

    public void stopBomberFlySFX(long id) {
        bomberFlySFX.stop(id);
    }

    public long playBipSFX() {
        return bipSFX.loop(SFXVolume);
    }

    public void stopBipSFX(long id) {
        bipSFX.stop(id);
    }

    public void setSFXVolume(float SFXVolume) {
        this.SFXVolume = SFXVolume;
    }

    public void setBGMVolume(float BGMVolume) {
        this.BGMVolume = BGMVolume;
        if(backgroundMusic != null){
            backgroundMusic.setVolume(BGMVolume);
        }
    }

    public void disposeAllResources() {
        for (int i = 0; i < 2; i++) {
            shotSFX[0].dispose();
        }
        bulletExplosionSFX.dispose();
        explosionSFX.dispose();
        missileLaunchSFX.dispose();
        artilleryLaunchSFX.dispose();
        pickItemSFX.dispose();
        bomberFlySFX.dispose();
        bipSFX.dispose();
        chooseWeaponSFX.dispose();
        chooseTargetSFX.dispose();
        forceFieldHitSFX.dispose();
        disableSFX.dispose();
        enableSFX.dispose();
        purchaseSFX.dispose();
        researchSFX.dispose();
        if(backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }
    }

    public void stopBGM(){
        if(backgroundMusic != null){
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }
    }

    public void pauseBGM() {
        if (backgroundMusic != null){
            backgroundMusic.pause();
        }
    }

    public void playBGM() {
        if (backgroundMusic != null){
            backgroundMusic.play();
        }
    }
}
