package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import java.util.Stack;

/**
 * Created by emanuelesan on 21/08/16.
 */
public class MyStack<T> extends Stack<T> {

    public void removeAfter(T t) {
        int i = indexOf(t) + 1;

        while (i > 0 && i < size()) {
            remove(i);
        }
    }
}
