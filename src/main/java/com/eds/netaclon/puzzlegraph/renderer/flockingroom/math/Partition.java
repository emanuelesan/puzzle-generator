package com.eds.netaclon.puzzlegraph.renderer.flockingroom.math;

import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class Partition {


    static final Partition VOID = new Partition(Vector2.ZERO, Vector2.ZERO, Verse.POSITIVE, 0, Vector2.Y);
    public static final int A_LOT = 60000;
    private final Vector2 minK;
    private final Vector2 maxK;
    private final Verse verse;
    private final Vector2 direction;
    private final int extension;


    public Partition(int xMin, int yMin, int xMax, int yMax) {
        checkArguments(xMin, yMin, xMax, yMax);
        extension = A_LOT;
        if (isALot(xMax)) {
            minK = new Vector2(xMin, yMin);
            maxK = new Vector2(xMin, yMax);
            verse = Verse.POSITIVE;
            direction = Vector2.X;
        } else if (isALot(yMax)) {
            minK = new Vector2(xMin, yMin);
            maxK = new Vector2(xMax, yMin);
            verse = Verse.POSITIVE;
            direction = Vector2.Y;


        } else if (isALot(xMin)) {
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

    public Partition(Vector2 minK, Vector2 maxK, Verse verse, int extension, Vector2 direction) {
        checkArguments(minK, maxK, extension, direction);
        this.minK = minK;
        this.maxK = maxK;
        this.verse = verse;
        this.extension = extension;
        this.direction = direction;
    }

    private static void checkArguments(Vector2 minK, Vector2 maxK, int extension, Vector2 direction) {
        if (
                isALot(minK.x) || isALot(minK.y) ||
                        isALot(maxK.x) || isALot(maxK.y)
                )
            throw new RuntimeException(String.format(Locale.ENGLISH, "no NaN value allowed! %s,%s,%f,%f", minK, maxK));
        if (extension < 0
                )
            throw new RuntimeException(String.format(Locale.ENGLISH, "extension cannot be negative! %f", extension));
    }


    private static boolean isALot(int y) {
        return y >= A_LOT;
    }

    private static void checkArguments(int xMin, int yMin, int xMax, int yMax) {
        int nans = 0;
        if (isALot(xMax)) nans++;
        if (isALot(yMax)) nans++;
        if (isALot(xMin)) nans++;
        if (isALot(yMin)) nans++;
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
        int xMin = verse == Verse.NEGATIVE && direction.equals(Vector2.X) ? maxK.x - extension : minK.x;
        int yMin = verse == Verse.NEGATIVE && direction.equals(Vector2.Y) ? maxK.y - extension : minK.y;

        int xMax = verse == Verse.POSITIVE && direction.equals(Vector2.X) ? minK.x + extension : maxK.x;
        int yMax = verse == Verse.POSITIVE && direction.equals(Vector2.Y) ? minK.y + extension : maxK.y;

        return new Rectangle(xMin, yMin, xMax, yMax);


    }

    public static Partition nonDegenerate(Partition partition, Partition partition1) {
        return partition.isDegenerate() ? partition1 : partition;
    }

    public boolean isDegenerate() {
        return extension <= 0 || maxK.minus(minK).l1() <= 0;
    }

    /**
     * @return some rectangles contained in the partition,
     * each having an edge lying the minK-maxK segment.
     */
    public Stream<Rectangle> complete(int width, int height) {
        int realWidth = Math.min(extension, width);
        int realHeight = Math.min(extension, height);
        int minX = minK.x,
                minY = minK.y,
                maxX = maxK.x,
                maxY = maxK.y;

        List<Rectangle> rectangles = new ArrayList<>();
        if (direction.equals(Vector2.X)) {
            if (this.verse == Verse.POSITIVE) {
                maxX = minK.x + realWidth;
            } else {
                minX = maxK.x - realWidth;
            }
            for (int j = minK.y; j <= maxY - height; j += 1) {
                rectangles.add(new Rectangle(minX, j, maxX, Math.min(maxY, j + height)));
            }
        } else {
            if (this.verse == Verse.POSITIVE) {
                maxY = minK.y + realHeight;

            } else {
                minY = maxK.y - realHeight;
            }
            for (int j = minK.x; j <= maxX - width; j += 1) {
                rectangles.add(new Rectangle(j, minY, Math.min(maxX, j + width), maxY));
            }
        }
        return rectangles.stream().distinct();
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
        int extension;
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
     * the partition has the same attributes of the current,
     * but a lower extension or lower maxK-minK distance.
     */
    public Partition exclude(Rectangle rect) {
        if (this.toRectangle().intersects(rect)) {
            Rectangle intersection = this.toRectangle().intersection(rect);
            if (intersection.area() > 0) {
                Vector2 newMinK = minK;
                Vector2 newMaxK = maxK;
                int newExtension;
                if (direction.equals(Vector2.X)) {
                    if (this.verse == Verse.POSITIVE) {
                        newExtension = Math.max(0, rect.xMin() - minK.x);
                    } else {
                        newExtension = Math.max(0, maxK.x - rect.xMax());
                    }
                    //both of the following can apply.
                    if (maxK.y > intersection.yMax()) newMinK = new Vector2(minK.x, intersection.yMax());
                    if (minK.y < intersection.yMin()) newMaxK = new Vector2(minK.x, intersection.yMin());
                } else {
                    if (this.verse == Verse.POSITIVE) {
                        newExtension = Math.max(0, rect.yMin() - minK.y);
                    } else {
                        newExtension = Math.max(0, maxK.y - rect.yMax());
                    }
                    //both of the following can apply.
                    if (maxK.x > intersection.xMax()) newMinK = new Vector2(intersection.xMax(), minK.y);
                    if (minK.x < intersection.xMin()) newMaxK = new Vector2(intersection.xMin(), minK.y);
                }
                if (newExtension <= 0 && (maxK.minus(newMaxK).l1() > 1 || newMinK.minus(minK).l1() > 1)) {
                    if (newMaxK.minus(newMinK).l1() < 0) {
                        if (newMaxK.minus(minK).l1() > maxK.minus(newMinK).l1()) {
                            return new Partition(minK, newMaxK, this.verse, extension, this.direction);
                        } else {
                            return new Partition(newMinK, maxK, this.verse, extension, this.direction);
                        }
                    }

                    return new Partition(newMinK, newMaxK, this.verse, extension, this.direction);

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

    public int getExtension() {
        return extension;
    }
}
