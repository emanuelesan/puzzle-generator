package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by emanuelesan on 21/08/16.
 */
public class MyStackTest {

    public static final String UNO = "uno";
    public static final String CINQUE = "cinque";

    @Test
    public void testRemoveAfterDefault() throws Exception {

        MyStack<String> strings = createWithFive();

        strings.removeAfter(UNO);

        Assert.assertTrue(Collections.singletonList(UNO).containsAll(strings));
        Assert.assertTrue(strings.containsAll(Collections.singletonList(UNO)));

    }


    @Test
    public void testRemoveAfterNotContained() throws Exception {

        MyStack<String> strings = createWithFive();
        MyStack<String> sameStack = createWithFive();


        strings.removeAfter("NOT_CONTAINED");

        Assert.assertTrue(sameStack.containsAll(strings));
        Assert.assertTrue(strings.containsAll(sameStack));

    }

    @Test
    public void testRemoveAfterLasrt() throws Exception {

        MyStack<String> strings = createWithFive();
        MyStack<String> sameStack = createWithFive();


        strings.removeAfter(CINQUE);

        Assert.assertTrue(sameStack.containsAll(strings));
        Assert.assertTrue(strings.containsAll(sameStack));

    }

    private MyStack<String> createWithFive() {
        MyStack<String> strings = new MyStack<>();
        strings.add("uno");
        strings.add("due");
        strings.add("tre");
        strings.add("quattro");
        strings.add("cinque");
        return strings;
    }

}