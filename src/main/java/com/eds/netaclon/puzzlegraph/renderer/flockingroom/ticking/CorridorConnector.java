package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;
import javaslang.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * creates corridors, which are new rooms actually, added to the puzzle,
 * between rooms that have a door connecting them but are not adjacent.
 */
public class CorridorConnector implements TickWiseOperator {
    private static final Logger logger = Logger.getLogger("logger");

    private final Map<String, Rectangle> rectsByRoom;
    private final Puzzle puz;

    public CorridorConnector(GraphicPuzzle gp) {
        this.rectsByRoom = gp.getRectsByRoom();
        this.puz = gp.getPuzzle();
    }

    @Override
    public void tick() {
        List<Tuple2<Door, Direction>> corridorsNeeded = computeCorridors();
        corridorsNeeded.forEach(tuple ->
                {
                    Rectangle corridor = createCorridor(tuple._1(), tuple._2());
                    Room room = puz.addRoom(tuple._1());
                    rectsByRoom.put(room.getName(), corridor);
                }
        );
    }

    private Rectangle createCorridor(Door door, Direction direction) {
        Rectangle inRect = rectsByRoom.get(door.getInRoom(puz).getName());
        Rectangle outRect = rectsByRoom.get(door.getOutRoom(puz).getName());
        Rectangle corridor = inRect.inBetween(outRect);

        for (Rectangle rec : rectsByRoom.values()) {
            if (rec != outRect && inRect != rec) {
                corridor = direction == Direction.VERTICAL ? corridor.subtractHorizontal(rec) : corridor.subtractVertical(rec);
            }
        }
        if (direction == Direction.VERTICAL) {
            corridor.scale(1D / corridor.width(), 1);
        } else {
            corridor.scale(1, 1D / corridor.height());
        }
        return corridor;
    }


    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return !doRoomsOverlap();
    }

    private boolean doRoomsOverlap() {
        boolean result = false;
        for (Map.Entry<String, Rectangle> rec1 : rectsByRoom.entrySet()) {
            for (Map.Entry<String, Rectangle> rec2 : rectsByRoom.entrySet())

            {
                if (!rec1.getKey().equals(rec2.getKey())) {
                    double intersectionArea = rec1.getValue().intersectionArea(rec2.getValue());

                    if (intersectionArea > 1) {
                        logger.info("intersection area between " + rec1.getKey() + " && " + rec2.getKey() + "= " + intersectionArea);
                    }

                    result = result || (intersectionArea > 1);
                }
            }


        }
        return result;
    }

    private List<Tuple2<Door, Direction>> computeCorridors() {
        return puz.getDoorMap()
                .values()
                .stream()
                .flatMap(this::areRoomsTooFar)
                .collect(Collectors.toList());
    }

    private Stream<Tuple2<Door, Direction>> areRoomsTooFar(Door door) {
        Rectangle inRect = rectsByRoom.get(door.getInRoom(puz).getName());
        Rectangle outRect = rectsByRoom.get(door.getOutRoom(puz).getName());
        Vector2 roomDims = new Vector2(
                (inRect.width() + outRect.width()) / 2D,
                (inRect.height() + outRect.height()) / 2D);
        Vector2 distance =
                new Vector2(
                        Math.abs(inRect.x() - outRect.x()),
                        Math.abs(inRect.y() - outRect.y()));
        Vector2 deficit = distance.minus(roomDims);

        if (Math.round(deficit.x) > 0 && Math.round(deficit.y) < 0) {
            return Stream.of(new Tuple2<>(door, Direction.HORIZONTAL));
        }


        if (Math.round(deficit.x) < 0 && Math.round(deficit.y) > 0) {
            return Stream.of(new Tuple2<>(door, Direction.VERTICAL));
        }

        return Stream.empty();
    }

    private enum Direction {
        HORIZONTAL, VERTICAL
    }

}
