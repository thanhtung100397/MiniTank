package com.tankzor.game.util;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Admin on 5/5/2017.
 */

public class CircleList<T> {
    private Array<Node<T>> listItems;

    public CircleList() {
        listItems = new Array<Node<T>>();
    }

    public CircleList(int size) {
        listItems = new Array<Node<T>>(size);
    }

    public void add(T t) {
        Node<T> node = new Node<T>(t);
        if (listItems.size != 0) {
            Node<T> lastNode = getNode(listItems.size - 1);
            Node<T> firstNode = getNode(0);
            node.next = firstNode;
            node.previous = lastNode;
            firstNode.previous = node;
            lastNode.next = node;

        }
        listItems.add(node);
    }

    public boolean getBestIterator(int fromOrient, int toOrient) {
        if (toOrient < fromOrient) {
            toOrient += listItems.size;
        }
        int step = toOrient - fromOrient;
        int revertStep = toOrient - listItems.size - fromOrient;
        int result = Math.abs(step) <= Math.abs(revertStep) ? step : revertStep;
        return result > 0;
    }

    public Node<T> getNode(int index) {
        return listItems.get(index);
    }

    public static class Node<T> {
        private T value;
        public Node<T> next;
        public Node<T> previous;

        Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }
}
