package com.tankzor.game.util;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by aguiet on 28/05/2015.
 */
public class QuadRectangle {
    public float x, y, width, height;
    private FloatPoint centerPoint;

    public QuadRectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        centerPoint = new FloatPoint(x + width / 2, y + height / 2);
    }

    public boolean contains(QuadRectangle r) {
        return this.width > 0 && this.height > 0 && r.width > 0 && r.height > 0
                && r.x >= this.x && r.x + r.width <= this.x + this.width
                && r.y >= this.y && r.y + r.height <= this.y + this.height;
    }

    public boolean overlaps(QuadRectangle r){
        return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
    }

    public FloatPoint getCenterPoint(){
        centerPoint.x = x + width / 2;
        centerPoint.y = y + height / 2;
        return centerPoint;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y + " w: " + width + " h: " + height;
    }
}
