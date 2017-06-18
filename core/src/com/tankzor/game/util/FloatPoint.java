package com.tankzor.game.util;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Admin on 12/14/2016.
 */

public class FloatPoint {
    public float x;
    public float y;

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float calculateDistance(FloatPoint otherPoint) {
        return (float) Math.sqrt(Math.pow(otherPoint.x - x, 2) + Math.pow(otherPoint.y - y, 2));
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
