package com.eds.netaclon.puzzlegraph.renderer.flockingroom;


import java.io.Serializable;

/**
 * Created by emanuelesan on 31/03/16.
 * <p>
 * aaah the rectangle class, an evergreen.
 */
public class Rectangle implements Serializable {
    private double x, y, xMin, yMin, xMax, yMax;

    private double xVel;
    private double yVel;

    public Rectangle() {
    }

    public Rectangle(double xMin, double yMin, double xMax, double yMax) {
        this();
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

    public double intersectionArea(Rectangle r) {
        if (intersects(r)) {
            return intersection(r).area();
        }
        return 0;
    }

    public Rectangle intersection(Rectangle r) {
        return new Rectangle(Math.max(xMin, r.xMin), Math.max(yMin, r.yMin), Math.min(xMax, r.xMax), Math.min(yMax, r.yMax));
    }

    public Vector2 minusPos(Rectangle r) {
        return new Vector2(this.x - r.x, this.y - r.y);
    }

    public boolean intersects(Rectangle r) {
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

    public Rectangle inBetween(Rectangle r) {
        Rectangle verticalInbetween = new Rectangle(Math.max(xMin, r.xMin), Math.min(yMax, r.yMax), Math.min(xMax, r.xMax), Math.max(yMin, r.yMin));
        if (verticalInbetween.height() < 0 || verticalInbetween.width() < 0) {       //return horizontalInbetween
            return new Rectangle(Math.min(xMax, r.xMax), Math.max(yMin, r.yMin), Math.max(xMin, r.xMin), Math.min(yMax, r.yMax));

        }
        return verticalInbetween;

    }

    public Rectangle subtractHorizontal(Rectangle rec) {

        if (this.intersects(rec)) {
            Rectangle toReduce = this.intersection(rec);
            if (toReduce.xMin - xMin > xMax - toReduce.xMax) {
                return new Rectangle(xMin, yMin, toReduce.xMin, yMax);
            }
            return new Rectangle(toReduce.xMax, yMin, xMax, yMax);
        }
        return this.copy();
    }

    /**
     * shave off columns, vertically.
     *
     * @param rec
     * @return
     */
    public Rectangle subtractVertical(Rectangle rec) {

        if (this.intersects(rec)) {
            Rectangle toReduce = this.intersection(rec);
            if (toReduce.yMin - yMin > yMax - toReduce.yMax) {
                return new Rectangle(xMin, yMin, xMax, toReduce.yMin);
            }
            return new Rectangle(xMin, toReduce.yMax, xMax, yMax);
        }
        return this.copy();
    }


    private Rectangle copy() {
        return new Rectangle(xMin, yMin, xMax, yMax);
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

    public double xMax() {
        return xMax;
    }

    public double yMin() {
        return yMin;
    }

    public double yMax() {
        return yMax;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    /**
     * center will remain still, corners will move!
     *
     * @param ratio factor by which distance from center will be changed.
     */
    public void scale(double ratio) {
        scale(ratio, ratio);

    }


    public void scale(double xRatio, double yRatio) {
        xMin = x - (x - xMin) * xRatio;
        xMax = x + (xMax - x) * xRatio;

        yMin = y - (y - yMin) * yRatio;
        yMax = y + (yMax - y) * yRatio;

    }

    public void roundCoords() {
        xMin = Math.round(xMin);
        xMax = Math.round(xMax);

        yMin = Math.round(yMin);
        yMax = Math.round(yMax);

    }

    public static Rectangle fromCenter(double xCenter, double yCenter, double width, double height) {
        return new Rectangle(xCenter - width/2, yCenter - height/2,
                xCenter + width/2, yCenter + height/2
        );
    }
}
