package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

import java.util.Map;
import java.util.logging.Logger;

/**
 * creates doors between adjacent rooms.
 * doors are 1x2 large.
 */
public class DoorCreator implements TickWiseOperator {
    private static final Logger logger = Logger.getLogger("logger");

    private final GraphicPuzzle gp;
    private boolean done = false;

    public DoorCreator(GraphicPuzzle gp) {
        this.gp = gp;
    }

    @Override
    public void tick() {
        createAllDoors();

        done = true;
    }

    private void createAllDoors() {
        gp.getPuzzle().getDoorMap().values().forEach(this::createDoor);

    }

    /**
     * creates a graphical representation of a door.
     *
     */
    private void createDoor(Door door) {
        Map<String, Rectangle> rectsByRoom = gp.getRectsByRoom();
        Rectangle r1 = rectsByRoom.get(door.getInRoomName());
        Rectangle r2 = rectsByRoom.get(door.getOutRoomName());

        Rectangle intersection = r1.intersection(r2);

        Rectangle doorRect;
        if (intersection.height() > intersection.width()) {
            doorRect = Rectangle.fromCenter((int) (intersection.x()),
                    (float) (Math.floor(intersection.y()) + .5f),
                    2, 1);
        }
        else
        {
            doorRect = Rectangle.fromCenter((float) (Math.floor(intersection.x()) + .5f),
                    (int) (intersection.y()), 1, 2);
        }
        gp.add(door,doorRect);
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return true;
    }

    public static boolean canApply(GraphicPuzzle puzzle) {
        return puzzle.getPuzzle().getDoorMap().values().stream().map(door ->
                {
                    Rectangle r1 = puzzle.getRectsByRoom().get(door.getInRoomName());
                    Rectangle r2 = puzzle.getRectsByRoom().get(door.getOutRoomName());
                    if (r1 == null || r2 == null) {
                        return false;
                    }
                    Rectangle intersection = r1.intersection(r2);
                    if
                            (
                            (Float.compare(Math.round(intersection.height()), 0) == 0 && Math.round(intersection.width()) >= 1)
                                    ||
                                    (Float.compare(Math.round(intersection.width()), 0) == 0 && Math.round(intersection.height()) >= 1)
                            ) {
                        return true;
                    } else {
                        logger.info(() ->
                                "width: " + intersection.width() + ", height: " + intersection.height() +
                                        " between " + door.getInRoomName() + " and " + door.getOutRoomName());
                        return false;
                    }
                }
        ).reduce(Boolean::logicalAnd).orElse(true);
    }
}
