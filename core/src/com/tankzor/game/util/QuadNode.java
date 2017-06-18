package com.tankzor.game.util;

/**
 * Created by aguiet on 28/05/2015.
 */
public class QuadNode<T> {
    QuadRectangle r;
    T element;

    public QuadNode(QuadRectangle r, T element) {
        this.r = r;
        this.element = element;
    }

    @Override
    public String toString() {
        return r.toString();
    }
}
