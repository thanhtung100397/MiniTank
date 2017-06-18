package com.tankzor.game.util;

/**
 * Created by Admin on 6/18/2017.
 */

public class LinkedList<T> {
    private Node<T> head;
    private int size;

    public LinkedList() {
        clear();
    }

    public void add(T data){
        Node<T> node = new Node<T>(data);
        if(head == null){
            head = node;
        }else {
            Node<T> temp = head;
            while (temp.next != null){
                temp = temp.next;
            }
            temp.next = node;
        }
        size++;
    }

    public T removeFirst(){
        if(head == null){
            return null;
        }
        T result = head.data;
        head = head.next;
        size--;
        return result;
    }

    public T getFirst(){
        return head.data;
    }

    public Node<T> getFirstNode(){
        return head;
    }

    public int size(){
        return size;
    }

    public void clear(){
        this.head = null;
        this.size = 0;
    }

    public static class Node<T> {
        public T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }

        public Node<T> getNext() {
            return next;
        }
    }
}
