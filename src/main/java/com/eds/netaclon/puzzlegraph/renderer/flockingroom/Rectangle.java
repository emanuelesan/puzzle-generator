package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import java.io.Serializable;

/**
 * Created by emanuelesan on 31/03/16.
 */
public class Rectangle implements Serializable {

    private double x, y;
    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;

    private double xVel;
    private double yVel;

    public Rectangle(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;

        x = (xMin + xMax) / 2;
        y = (yMin + yMax) / 2;
    }

    public void move() {
        xMin += xVel;
        yMin += yVel;
        xMax += xVel;
        yMax += yVel;
        x += xVel;
        y += yVel;

        xVel = 0;
        yVel = 0;
    }

    public double intersection(Rectangle r) {
        if (intersects(r)) {
            return intersectionRect(r).area();
        }
        return 0;
    }

    private Rectangle intersectionRect(Rectangle r) {
        return new Rectangle(Math.max(xMin, r.xMin), Math.max(yMin, r.yMin), Math.min(xMax, r.xMax), Math.min(yMax, r.yMax));
    }

    public Vector2 minusPos(Rectangle r) {
        return new Vector2(this.x - r.x, this.y - r.y);
    }

    protected boolean intersects(Rectangle r) {
        return r.xMax >= xMin && r.xMin <= xMax && r.yMax >= yMin && r.yMin <= yMax;
    }

    public double area() {
        return (xMax - xMin) * (yMax - yMin);
    }


    public void push(Vector2 force) {
        xVel += force.x;
        yVel += force.y;
    }

    public Rectangle bbox(Rectangle r) {
        return new Rectangle(Math.min(xMin, r.xMin), Math.min(yMin, r.yMin), Math.max(xMax, r.xMax), Math.max(yMax, r.yMax));
    }

    public Rectangle inBetween(Rectangle r)
    {
        return intersectionRect(r);
    }

    public double width() {
        return xMax - xMin;
    }

    public double height() {
        return yMax - yMin;
    }

    public double xMin() {
        return xMin;
    }
    public double xMax()
    {
        return xMax;
    }
    public double yMin()
    {return yMin;}
    public double yMax()
    {
        return yMax;
    }

    public double x()
    {return x;}
    public double y()
    {return y;}

    /**
     * center will remain still, corners will move!
     * @param ratio
     */
    public void scale(double ratio) {
        xMin=x - (x-xMin)*ratio;
        xMax=x + (xMax-x)*ratio;

        yMin=y - (y-yMin)*ratio;
        yMax=y + (yMax-y)*ratio;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getxMin() {
        return xMin;
    }

    public double getyMin() {
        return yMin;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMax() {
        return yMax;
    }
}
