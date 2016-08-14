package com.eds.netaclon.puzzlegraph.renderer.flockingroom.math;

import org.junit.Assert;
import org.junit.Test;


public class SpartitionTest {


    @Test
    public void testIntersectOrthogonalTrue() throws Exception {
        Spartition hpart = new Spartition(0, 0, Float.NaN, 1);
        Spartition vpart = new Spartition(1, -1, 2, Float.NaN);

        Spartition intersect = hpart.intersect(vpart);
        Assert.assertEquals(1, intersect.xMin(), Float.MIN_VALUE);
        Assert.assertEquals(0, intersect.yMin(), Float.MIN_VALUE);

        Assert.assertEquals(2, intersect.xMax(), Float.MIN_VALUE);
        Assert.assertEquals(1, intersect.yMax(), Float.MIN_VALUE);


    }

}