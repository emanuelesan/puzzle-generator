package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

/**
 * Created by emanuelesan on 31/03/16.
 */
public class Vector2 {

    public static final Vector2 ZERO = new Vector2(0,0);
    public final double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double magnitude()
    {
        return Math.sqrt(x*x+y*y);
    }

    public Vector2 times(double times)
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

    public Vector2 plus(double x, double y) {
        return new Vector2(this.x+x,this.y+y);
    }

    public Vector2 minus(double x, double y) {
        return plus(-x,-y);
    }
}
