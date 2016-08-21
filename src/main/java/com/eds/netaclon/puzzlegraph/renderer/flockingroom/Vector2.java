package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

/**
 * immutable vector class.
 */
public class Vector2 {

    public static final Vector2 ZERO = new Vector2(0,0);
    public static final Vector2 X = new Vector2(1, 0);
    public static final Vector2 Y = new Vector2(0, 1);

    public final int x, y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }


//    public int magnitude()
//    {
//        return Math.sqrt(x*x+y*y);
//    }

    public Vector2 times(float times)
    {
        return new Vector2((int) (x * times), (int) (y * times));
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

    public Vector2 plus(int x, int y) {
        return new Vector2(this.x+x,this.y+y);
    }

    public Vector2 minus(int x, int y) {
        return plus(-x,-y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Float.compare(vector2.x, x) == 0 && Float.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public int l1() {
        return x + y;
    }
}
