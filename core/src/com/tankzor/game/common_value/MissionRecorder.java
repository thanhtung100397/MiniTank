package com.tankzor.game.common_value;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Admin on 5/9/2017.
 */

public class MissionRecorder {
    public String name;
    public boolean isComplete;
    public int money;
    public int star;
    public long startTime;
    public int unitDestroyed;
    public int shotCount;
    public int takenDamage;
    public int travelDistance;

    public String getPlayTime() {
        long duration = TimeUtils.timeSinceMillis(startTime) / 1000;
        long h = duration / 3600;
        duration %= 3600;
        long min = duration / 60;
        duration %= 60;
        long sec = duration;
        return h + " : " + min + " : " + sec;
    }
}
