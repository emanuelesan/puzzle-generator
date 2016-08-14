package com.eds.netaclon.puzzlegraph.renderer.flockingroom.math;

import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;

import java.util.Locale;

/**
 * Created by emanuelesan on 14/08/16.
 */
public class Partition {


    static final Partition VOID = new Partition(Vector2.ZERO, Vector2.ZERO, Verse.POSITIVE, 0, Vector2.Y);
    public static final int A_LOT = 60000;
    private final Vector2 minK;
    private final Vector2 maxK;
    private final Verse verse;
    private final Vector2 direction;
    private final float extension;


    public Partition(float xMin, float yMin, float xMax, float yMax) {
        checkArguments(xMin, yMin, xMax, yMax);
        extension = A_LOT;
        if (Float.isNaN(xMax)) {
            minK = new Vector2(xMin, yMin);
            maxK = new Vector2(xMin, yMax);
            verse = Verse.POSITIVE;
            direction = Vector2.X;
        } else if (Float.isNaN(yMax)) {
            minK = new Vector2(xMin, yMin);
            maxK = new Vector2(xMax, yMin);
            verse = Verse.POSITIVE;
            direction = Vector2.Y;


        } else if (Float.isNaN(xMin)) {
            minK = new Vector2(xMax, yMin);
            maxK = new Vector2(xMax, yMax);
            verse = Verse.NEGATIVE;
            direction = Vector2.X;


        } else {
            minK = new Vector2(xMin, yMax);
            maxK = new Vector2(xMax, yMax);
            verse = Verse.NEGATIVE;
            direction = Vector2.Y;

        }


    }

    public Partition(Vector2 minK, Vector2 maxK, Verse verse, float extension, Vector2 direction) {
        checkArguments(minK, maxK, extension, direction);
        this.minK = minK;
        this.maxK = maxK;
        this.verse = verse;
        this.extension = extension;
        this.direction = direction;

    }

    private static void checkArguments(Vector2 minK, Vector2 maxK, float extension, Vector2 direction) {
        if (
                Float.isNaN(minK.x) || Float.isNaN(minK.y) ||
                        Float.isNaN(maxK.x) || Float.isNaN(maxK.y)
                )
            throw new RuntimeException(String.format(Locale.ENGLISH, "no NaN value allowed! %s,%s,%f,%f", minK, maxK));
        if (extension < 0
                )
            throw new RuntimeException(String.format(Locale.ENGLISH, "extension cannot be negative! %f", extension));


    }

    private static void checkArguments(float xMin, float yMin, float xMax, float yMax) {
        int nans = 0;
        if (Float.isNaN(xMax)) nans++;
        if (Float.isNaN(yMax)) nans++;
        if (Float.isNaN(xMin)) nans++;
        if (Float.isNaN(yMin)) nans++;
        if (nans != 1)
            throw new RuntimeException(String.format(Locale.ENGLISH, "exactly 1 NaN value allowed! %f,%f,%f,%f", xMin, yMin, xMax, yMax));

    }

    /**
     * if they intersect, current partition is returned with decreased extension.
     * void partition otherwise
     */
    public Partition intersect(Partition partition) {
        Rectangle otherPartRect = partition.toRectangle();
        if (this.toRectangle().intersects(otherPartRect))
            return intersect(otherPartRect);
        return VOID;
    }

    private Rectangle toRectangle() {
        float xMin = verse == Verse.NEGATIVE && direction.equals(Vector2.X) ? maxK.x - extension : minK.x;
        float yMin = verse == Verse.NEGATIVE && direction.equals(Vector2.Y) ? maxK.y - extension : minK.y;

        float xMax = verse == Verse.POSITIVE && direction.equals(Vector2.X) ? minK.x + extension : maxK.x;
        float yMax = verse == Verse.POSITIVE && direction.equals(Vector2.Y) ? minK.y + extension : maxK.y;

        return new Rectangle(xMin, yMin, xMax, yMax);


    }

    public static Partition nonDegenerate(Partition partition, Partition partition1) {
        return partition.isDegenerate() ? partition1 : partition;
    }

    public boolean isDegenerate() {
        return extension <= 0;
    }

    public Rectangle complete(int width, float height) {
        float realWidth = Math.min(extension, width);
        float realHeight = Math.min(extension, height);
        float minX = minK.x,
                minY = minK.y,
                maxX = maxK.x,
                maxY = maxK.y;

        if (direction.equals(Vector2.X)) {
            if (this.verse == Verse.POSITIVE) {
                maxX = minK.x + realWidth;
            } else {
                minX = maxK.x - realWidth;
            }
        } else {
            if (this.verse == Verse.POSITIVE) {
                maxY = minK.y + realHeight;

            } else {
                minY = maxK.y - realHeight;
            }
        }

        return new Rectangle(minX, minY, maxX, maxY);
    }

    /**
     * returns an empty partition if no intersection.
     *
     * @param rect
     * @return
     */
    public Partition intersect(Rectangle rect) {
        //r.xMax >= xMin && r.xMin <= xMax && r.yMax >= yMin && r.yMin <= yMax;
        Rectangle intersection = toRectangle().intersection(rect);

        Vector2 minK, maxK;
        float extension;
        if (direction.equals(Vector2.X)) {
            extension = intersection.width();
            if (this.verse == Verse.POSITIVE) {
                minK = new Vector2(intersection.xMin(), intersection.yMin());
                maxK = new Vector2(intersection.xMin(), intersection.yMax());
            } else {
                minK = new Vector2(intersection.xMax(), intersection.yMin());
                maxK = new Vector2(intersection.xMax(), intersection.yMax());
            }
        } else {
            extension = intersection.height();
            if (this.verse == Verse.POSITIVE) {
                minK = new Vector2(intersection.xMin(), intersection.yMin());
                maxK = new Vector2(intersection.xMax(), intersection.yMin());
            } else {
                minK = new Vector2(intersection.xMin(), intersection.yMax());
                maxK = new Vector2(intersection.xMax(), intersection.yMax());
            }
        }

        return new Partition(minK, maxK,
                this.verse,
                extension,
                this.direction);
    }

    /**
     * @return a smaller partition only if intersection has a non 0 area.
     * the partition has the same attributes of the current, but a lower extension.
     */
    public Partition exclude(Rectangle rect) {
        if (this.toRectangle().intersects(rect)) {
            Rectangle intersection = this.toRectangle().intersection(rect);
            if (intersection.area() > 0) {
                float newExtension;
                if (direction.equals(Vector2.X)) {
                    if (this.verse == Verse.POSITIVE) {
                        newExtension = Math.max(0, rect.xMin() - minK.x);
                    } else {
                        newExtension = Math.max(0, maxK.x - rect.xMax());
                    }
                } else {
                    if (this.verse == Verse.POSITIVE) {
                        newExtension = Math.max(0, rect.yMin() - minK.y);
                    } else {
                        newExtension = Math.max(0, maxK.y - rect.yMax());
                    }
                }
                return new Partition(this.minK, this.maxK, this.verse, newExtension, this.direction);
            }
        }
        return this;
    }

    private enum Verse {
        NEGATIVE(-1), POSITIVE(1);

        final int verseCoeff;

        Verse(int verseCoeff) {
            this.verseCoeff = verseCoeff;
        }
    }

    public Vector2 getMinK() {
        return minK;
    }

    public Vector2 getMaxK() {
        return maxK;
    }

    public Verse getVerse() {
        return verse;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public float getExtension() {
        return extension;
    }
}
