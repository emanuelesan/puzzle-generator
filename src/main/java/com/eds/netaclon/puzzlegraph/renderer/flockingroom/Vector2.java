package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

/**
 * Created by emanuelesan on 31/03/16.
 */
public class Vector2 {

    public static final Vector2 ZERO = new Vector2(0,0);
    public static final Vector2 X = new Vector2(1, 0);
    public static final Vector2 Y = new Vector2(0, 1);

    public final float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

//    public float magnitude()
//    {
//        return Math.sqrt(x*x+y*y);
//    }

    public Vector2 times(float times)
    {
        return new Vector2(x*times,y*times);
    }

    public Vector2 point(Vector2 vector2) {
        return new Vector2(x*vector2.x,y*vector2.y);
    }

    public Vector2 minus(Vector2 vector2) {
        return new Vector2(x-vector2.x,y-vector2.y);
    }

    public Vector2 plus(Vector2 vector2) {
        return plus(vector2.x,vector2.y);
    }

    public Vector2 plus(float x, float y) {
        return new Vector2(this.x+x,this.y+y);
    }

    public Vector2 minus(float x, float y) {
        return plus(-x,-y);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;

        if (Float.compare(vector2.x, x) != 0) return false;
        return Float.compare(vector2.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}
