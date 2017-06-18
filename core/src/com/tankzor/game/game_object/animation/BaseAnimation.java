package com.tankzor.game.game_object.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.tankzor.game.game_object.GameEntity;

/**
 * Created by Admin on 11/25/2016.
 */

public abstract class BaseAnimation extends GameEntity{
    protected float duration;
    protected float frameTime;

    public BaseAnimation() {
        super(0, 0, 0, 0);
    }

    public void setDuration(float duration) {
        if(images.length == 0){
            this.duration = 0;
            this.frameTime = 0;
            return;
        }
        this.duration = duration;
        this.frameTime = duration / images.length;
    }

    protected void init(TextureRegion[] images, float duration){
        this.images = images;
        setDuration(duration);
        addAction(new AnimationTimer());
    }

    protected class AnimationTimer extends Action {
        protected float timeElapse = 0;

        @Override
        public boolean act(float delta) {
            if(timeElapse >= frameTime){
                if(currentImageIndex == images.length - 1){
                    remove();
                    return true;
                }
                timeElapse = 0;
                currentImageIndex++;
            }
            timeElapse += delta;
            return false;
        }
    }
}
