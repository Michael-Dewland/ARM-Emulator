package com.michaelcode;

import java.util.ArrayList;

public class Stack <T> {

    private ArrayList<T> values = new ArrayList<>();
    private int top = -1;

    public static void main(String[] args) {
        // no initialisation needed
    }

    public void push(T value) {
        values.add(value);
    }

    public T pop() {
        T value = values.get(top);
        values.remove(values.size() - 1);

        return value;
    }

    public T peek() {
        return values.get(top);
    }

    public boolean isEmpty() {
        return (top < 0);
    }

    public boolean isFull() {
        return false;
    }
}
