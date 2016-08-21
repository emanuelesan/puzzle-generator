package com.eds.netaclon.puzzlegraph.graphic;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;
import javaslang.Tuple2;
import javaslang.Tuple3;

import java.util.*;
import java.util.stream.Collectors;

public class GraphicPuzzle {
    private final long seed;
    private Puzzle puzzle;
    private Map<String, Rectangle> rectsByRoom;
    private Map<String, Rectangle> rectsByItem;
    private Map<String, Rectangle> rectsByDoor;


    public GraphicPuzzle(Puzzle puz, long seed) {
        this.puzzle = puz;
        rectsByRoom = new HashMap<>();
        rectsByDoor = new HashMap<>();
        rectsByItem = new HashMap<>();
        this.seed = seed;

    }

    public List<Tuple2<Rectangle, Rectangle>> calculateOverlappingRectangles() {
        List<Tuple2<Rectangle, Rectangle>> overlappingRooms = new ArrayList<>();
        for (Map.Entry<String, Rectangle> rec1 : rectsByRoom.entrySet()) {
            for (Map.Entry<String, Rectangle> rec2 : rectsByRoom.entrySet())

            {
                if (!rec1.getKey().equals(rec2.getKey())) {
                    double intersectionArea = rec1.getValue().intersectionArea(rec2.getValue());

                    if (intersectionArea > 1) {
//                        logger.info("intersection area between " + rec1.getKey() + " && " + rec2.getKey() + "= " + intersectionArea);
                        overlappingRooms.add(new Tuple2<>(rec1.getValue(), rec2.getValue()));
                    }

                }
            }


        }
        return overlappingRooms;
    }

    public Optional<Tuple2<Door, Direction>> areRoomsTooFar(Door door) {
        Rectangle inRect = rectsByRoom.get(door.getInRoom(puzzle).getName());
        Rectangle outRect = rectsByRoom.get(door.getOutRoom(puzzle).getName());
        Vector2 roomDims = new Vector2(
                (inRect.width() + outRect.width()) / 2f,
                (inRect.height() + outRect.height()) / 2f);
        Vector2 distance =
                new Vector2(
                        Math.abs(inRect.x() - outRect.x()),
                        Math.abs(inRect.y() - outRect.y()));
        Vector2 deficit = distance.minus(roomDims);

        if (Math.round(deficit.x) > 0 && Math.round(deficit.y) < 0) {
            return Optional.of(new Tuple2<>(door, Direction.HORIZONTAL));
        }


        if (Math.round(deficit.x) < 0 && Math.round(deficit.y) > 0) {
            return Optional.of(new Tuple2<>(door, Direction.VERTICAL));
        }

        return Optional.empty();
    }

    public Map<String, Rectangle> getRectsByRoom() {
        return rectsByRoom;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public List<Tuple3<Rectangle, Rectangle, Door>> calculateTooDistantRectangles() {
        return getPuzzle().getDoorMap().values().stream()
                .map(door -> {
                    Rectangle inRect = rectsByRoom.get(door.getInRoom(puzzle).getName());
                    Rectangle outRect = rectsByRoom.get(door.getOutRoom(puzzle).getName());
                    return new Tuple3<>(inRect, outRect, door);
                })
                .filter(t -> {
                    Rectangle intersection = t._1().intersection(t._2());
                    return (intersection.width() <= 0 && intersection.height() <= 0);
                }).collect(Collectors.toList());

    }

    public void add(Door door, Rectangle doorRect) {
        rectsByDoor.put(door.getName(), doorRect);
    }

    public Map<String, Rectangle> getRectsByDoor() {
        return rectsByDoor;
    }

    public long getSeed() {
        return seed;
    }

    public Map<String, Rectangle> getRectsByItem() {
        return rectsByItem;
    }


    public enum Direction {
        HORIZONTAL, VERTICAL
    }
}
