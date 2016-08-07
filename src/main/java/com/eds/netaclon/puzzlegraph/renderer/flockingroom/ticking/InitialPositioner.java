package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.graphic.IntPosition;
import com.eds.netaclon.puzzlegraph.renderer.PosMapCalculator;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class InitialPositioner implements TickWiseOperator {
    private static final Logger logger = Logger.getLogger("logger");

    private static final int X_MAX = 4;
    private static final int Y_MAX = 3;
    private final Puzzle puz;
    private final GraphicPuzzle gp;
    private boolean done = false;

    public InitialPositioner(GraphicPuzzle graphicPuzzle) {
        this.puz = graphicPuzzle.getPuzzle();
        this.gp = graphicPuzzle;
    }

    @Override
    public void tick() {
        if (!isDone()) initialRoomPositions();
    }

    private void initialRoomPositions() {
        PosMapCalculator posCa = new PosMapCalculator();
        Map<Room, IntPosition> roomIntPositionMap = posCa.calculateRoomMap(puz);
        Map<IntPosition, Room> posMap = posCa.getPosMap();

        Map<String, Rectangle> rectangleByRoomName = new HashMap<>();
        puz.allRooms()
                .stream()
                .sorted((r1, r2) -> r1.getName().compareTo(r2.getName()))
                .forEach(room -> {
                    IntPosition pos = roomIntPositionMap.get(room);
                    Rectangle rectangle;

                    if (!posMap.get(pos).equals(room)) {
                        if (room.getDoorNames().size() > 1) {
                            logger.info("a room which was fitted in a space already "
                                    + "occupied had more than one room attached. should be thrown out");
                        }
                        Room otherRoom = puz.getOtherRoom(room.getDoorNames().get(0), room);
                        IntPosition otherRoomPosition = roomIntPositionMap.get(otherRoom);
                        int xpos = (int) ((pos.x + otherRoomPosition.x) / 2.0 * 2 * Math.max(X_MAX, Y_MAX));
                        int ypos = (int) ((pos.y + otherRoomPosition.y / 2.0) * 2 * Math.max(X_MAX, Y_MAX));
                        int sizeMult = room.getItemNames().size() + room.getDoorNames().size();

                        rectangle = Rectangle.fromCenter(xpos, ypos, 2 * Math.min(sizeMult + 1, X_MAX), 2 * Math.min(sizeMult + 1, Y_MAX));
                    } else {

                        int xMin = (pos.x * 2 * Math.max(X_MAX, Y_MAX));
                        int yMin = (pos.y * 2 * Math.max(X_MAX, Y_MAX));
                        int sizeMult = room.getItemNames().size() + room.getDoorNames().size();
                        rectangle = Rectangle.fromCenter(xMin, yMin,
                                2 * Math.min(sizeMult + 1, X_MAX),
                                2 * Math.min(sizeMult + 1, Y_MAX));
                    }
                    rectangleByRoomName.put(room.getName(), rectangle);
                    if (rectangle.width() % 1f > .1 || rectangle.height() % 1f > .1) {
                        logger.info(room.getName() + " has width " + rectangle.width() + " and height " + rectangle.width());
                    }
                });

        gp.getRectsByRoom().putAll(rectangleByRoomName);
        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return true;
    }
}
