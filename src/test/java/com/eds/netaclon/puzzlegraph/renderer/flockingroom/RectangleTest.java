package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by emanuelesan on 09/04/16.
 */
public class RectangleTest {

    @Test
    public void testSubtractHorizontalLeft() throws Exception {

        Rectangle rectangle = new Rectangle(1, 1, 3, 3);

        Rectangle recToSubtract = new Rectangle(0, 1, 2, 3);

        Rectangle subctracted = rectangle.subtractHorizontal(recToSubtract);


        Assert.assertTrue(Math.abs(subctracted.xMin() - recToSubtract.xMax()) < 2 * Double.MIN_VALUE);
    }

    @Test
    public void testSubtractHorizontalRight() throws Exception {

        Rectangle rectangle = new Rectangle(1, 1, 3, 3);

        Rectangle recToSubtract = new Rectangle(2, 1, 4, 3);

        Rectangle subctracted = rectangle.subtractHorizontal(recToSubtract);
        Assert.assertTrue(Math.abs(subctracted.xMax() - recToSubtract.xMin()) < 2 * Double.MIN_VALUE);
    }
}