package com.eds.netaclon.puzzlegraph.graphic;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.CorridorConnector;
import javaslang.Tuple2;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by emanuelesan on 10/04/16.
 */
public class GraphicPuzzle {
    private Puzzle puzzle;
    private Map<String, Rectangle> rectsByRoom;
    private Map<String, Rectangle> rectsByItems;

    public GraphicPuzzle()
    {}

    public GraphicPuzzle(Puzzle puz) {
      this();
        this.puzzle=puz;
        rectsByRoom = new HashMap<>();
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
                (inRect.width() + outRect.width()) / 2D,
                (inRect.height() + outRect.height()) / 2D);
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

    public List<Tuple2<Rectangle, Rectangle>> calculateTooDistantRectangles() {
        return getPuzzle().getDoorMap().values().stream().map(door->this.areRoomsTooFar(door).orElse(null))
                .filter(f->f!=null)
                .map(t->new Tuple2<>(rectsByRoom.get(t._1().getInRoomName()), rectsByRoom.get(t._1().getOutRoomName())))
                .collect(Collectors.toList());
    }

    public enum Direction {
        HORIZONTAL, VERTICAL
    }
}
