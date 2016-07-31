package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;
import javaslang.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final GraphicPuzzle gp;

    public CorridorConnector(GraphicPuzzle gp) {
        this.gp = gp;
        this.rectsByRoom = gp.getRectsByRoom();
        this.puz = gp.getPuzzle();
    }

    @Override
    public void tick() {
        List<Tuple2<Door, GraphicPuzzle.Direction>> corridorsNeeded = computeCorridors();
        corridorsNeeded.forEach(tuple ->
                {
                    Rectangle corridor = createCorridor(tuple._1(), tuple._2());
                    Room room = gp.getPuzzle().addRoom(tuple._1());
                    rectsByRoom.put(room.getName(), corridor);
                }
        );
    }

    private Rectangle createCorridor(Door door, GraphicPuzzle.Direction direction) {
        Rectangle inRect = rectsByRoom.get(door.getInRoom(puz).getName());
        Rectangle outRect = rectsByRoom.get(door.getOutRoom(puz).getName());
        Rectangle corridor = inRect.inBetween(outRect);

        for (Rectangle rec : rectsByRoom.values()) {
            if (rec != outRect && inRect != rec) {
                corridor = direction == GraphicPuzzle.Direction.VERTICAL ? corridor.subtractHorizontal(rec) : corridor.subtractVertical(rec);
            }
        }
        if (direction == GraphicPuzzle.Direction.VERTICAL) {
            corridor.scale(1f / corridor.width(), 1);
        } else {
            corridor.scale(1, 1f / corridor.height());
        }
        return corridor;
    }


    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return !(gp.calculateOverlappingRectangles().size()>0);
    }

    private List<Tuple2<Door, GraphicPuzzle.Direction>> computeCorridors() {
        return puz.getDoorMap()
                .values()
                .stream()
                .map(gp::areRoomsTooFar)
                .map(opt->opt.orElse(null))
                .filter(n->n!=null)
                .collect(Collectors.toList());
    }





}
