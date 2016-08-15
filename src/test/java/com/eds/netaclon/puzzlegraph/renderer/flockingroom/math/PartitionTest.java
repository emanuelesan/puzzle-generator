package com.eds.netaclon.puzzlegraph.renderer.flockingroom.math;

import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by emanuelesan on 14/08/16.
 */
public class PartitionTest {

    @Test
    public void testDegeneration() {
        Assert.assertTrue(Partition.VOID.isDegenerate());
    }

    @Test
    public void testExcludeIntersectsFully() throws Exception {
        Partition east = new Partition(0, 0, Float.NaN, 1);
        Partition excluded = east.exclude(new Rectangle(2, -1, 3, 2));
        Assert.assertEquals(0, excluded.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(1, excluded.getMaxK().y, Float.MIN_VALUE);

        Assert.assertEquals(2, excluded.getExtension(), Float.MIN_VALUE);
    }

    @Test
    public void testExcludeIntersectsPartially() throws Exception {
        Partition east = new Partition(0, 0, Float.NaN, 2);
        Partition excluded = east.exclude(new Rectangle(2, 1, 3, 2));
        Assert.assertEquals(0, excluded.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(2, excluded.getMaxK().y, Float.MIN_VALUE);

        Assert.assertEquals(2, excluded.getExtension(), Float.MIN_VALUE);
    }

    @Test
    public void testExcludeOnlyAdjacent() throws Exception {
        Partition east = new Partition(0, 0, Float.NaN, 2);
        Partition excluded = east.exclude(new Rectangle(2, 2, 3, 4));
        Assert.assertEquals(0, excluded.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(2, excluded.getMaxK().y, Float.MIN_VALUE);

        Assert.assertEquals(Partition.A_LOT, excluded.getExtension(), Float.MIN_VALUE);
    }

    @Test
    public void testExcludeNotEvenClose() throws Exception {
        Partition east = new Partition(0, 0, Float.NaN, 2);
        Partition excluded = east.exclude(new Rectangle(2, 20, 3, 24));
        Assert.assertEquals(0, excluded.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(2, excluded.getMaxK().y, Float.MIN_VALUE);

        Assert.assertEquals(Partition.A_LOT, excluded.getExtension(), Float.MIN_VALUE);
    }


    @Test
    public void testExcludePartial() throws Exception {
        Partition east = new Partition(0, 0, Float.NaN, 8);
        Partition excluded = east.exclude(new Rectangle(0, 6, 3, 10));
        Assert.assertEquals(Partition.A_LOT, excluded.getExtension(), Float.MIN_VALUE);

        Assert.assertEquals(0, excluded.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(6, excluded.getMaxK().y, Float.MIN_VALUE);

    }

    @Test
    public void testExcludeLyingOnTwoSides() throws Exception {
        Partition east = new Partition(0, 0, Float.NaN, 8);
        Partition excluded = east.exclude(new Rectangle(0, 6, 3, 7));
        Assert.assertEquals(Partition.A_LOT, excluded.getExtension(), Float.MIN_VALUE);

        Assert.assertEquals(0, excluded.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(6, excluded.getMaxK().y, Float.MIN_VALUE);

    }

    @Test
    public void testExcludeCompletelyBlocking() throws Exception {
        Partition east = new Partition(0, 0, Float.NaN, 8);
        Assert.assertEquals(0, east.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, east.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, east.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(8, east.getMaxK().y, Float.MIN_VALUE);

        Partition excluded = east.exclude(new Rectangle(0, 0, 8, 8));
        Assert.assertEquals(0, excluded.getExtension(), Float.MIN_VALUE);

        Assert.assertEquals(0, excluded.getMinK().x, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMinK().y, Float.MIN_VALUE);
        Assert.assertEquals(0, excluded.getMaxK().x, Float.MIN_VALUE);
        Assert.assertEquals(8, excluded.getMaxK().y, Float.MIN_VALUE);

    }

}