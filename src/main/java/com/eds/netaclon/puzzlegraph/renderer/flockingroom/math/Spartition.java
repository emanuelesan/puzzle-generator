package com.eds.netaclon.puzzlegraph.renderer.flockingroom.math;

import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

/**
 * It's a rectangle with one or more edges to infinite. yeah.
 * one or more coordinates must be NaN.
 */
public class Spartition {

    public static final Spartition VOID = new Spartition(0, 0, 0, 0);
    private float xMin, yMin, xMax, yMax;

    public Spartition(float xMin, float yMin, float xMax, float yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    private static Float minNotNan(float... floats) {
        float f = Float.NaN;
        for (float f1 : floats) {
            if (!Float.isNaN(f1) && (Float.isNaN(f) || f1 < f)) {
                f = f1;
            }
        }
        return f;
    }

    private static Float maxNotNan(float... floats) {
        float f = Float.NaN;
        for (float f1 : floats) {
            if (!Float.isNaN(f1) && (Float.isNaN(f) || f1 > f)) {
                f = f1;
            }
        }
        return f;
    }

    public Spartition intersect(Spartition s) {
        return new Spartition(maxNotNan(xMin, s.xMin), maxNotNan(yMin, s.yMin), minNotNan(xMax, s.xMax), minNotNan(yMax, s.yMax));
    }

    /**
     * IF rectangle is in the way, reduce Spartition's dimension.
     *
     * @param r
     * @return
     */
    public Spartition exclude(Rectangle r) {
        float halfWidth = (xMax - xMin) / 2;
        float halfHeight = (yMax - yMin) / 2;

        float newXMax = xMax;
        if (Float.isNaN(xMax) && (isBetweenY(r.yMin()) || isBetweenY(r.yMax()))) {//adjustXMax
            newXMax = r.xMin();
        }
        if (Float.isNaN(yMax)) {//adjust y max
        }


        return this;
    }

    private boolean isBetweenY(float value) {
        return value > yMin && value < yMax;
    }

    public Rectangle complete(float width, float height) {
        float xm = xMin;
        float xM = xm + width;
        if (Float.isNaN(xm)) {
            xm = xMax - width;
            xM = xMax;
        }
        float ym = yMin;
        float yM = ym + height;
        if (Float.isNaN(ym)) {
            ym = yMax - height;
            yM = yMax;
        }
        if (Float.isNaN(xm) || Float.isNaN(ym)) {
            throw new RuntimeException();
        }

        return new Rectangle(xm, ym, xM, yM);
    }

    float xMin() {
        return xMin;
    }

    float yMin() {
        return yMin;
    }

    float xMax() {
        return xMax;
    }

    float yMax() {
        return yMax;
    }

    /**
     * doesn't have negative or 0 dimensions
     */
    public boolean isDegenerate() {
        return (Float.isNaN(xMax) || Float.isNaN(xMin) || xMax - xMin >= 1)
                && (Float.isNaN(yMax) || Float.isNaN(yMin) || yMax - yMin >= 1);
    }

    /**
     * @return the minimum partition containing both of them
     * (mbr)
     */
    public static Spartition union(Spartition s1, Spartition s2) {
        return new Spartition(Math.min(s1.xMin, s2.xMin), Math.min(s1.yMin, s2.yMin),
                Math.max(s1.xMax, s2.xMax), Math.max(s1.yMax, s2.yMax));

    }

    @Override
    public String toString() {
        return "Spartition{" +
                "xMin=" + xMin +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", yMax=" + yMax +
                '}';
    }

    public static Spartition nonDegenerate(Spartition spartition, Spartition spartition1) {
        return spartition.isDegenerate() ? spartition1 : spartition;

    }
}
