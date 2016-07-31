package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.graphic.IntPosition;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.PosMapCalculator;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class InitialPositioner implements TickWiseOperator {
    private static final Logger logger = Logger.getLogger("logger");

    private static final float X_MAX = 6;
    private static final float Y_MAX = 4;
    private final Puzzle puz;
    private final GraphicPuzzle graphicPuzzle;
    private final Random rand;
    private boolean done = false;

    public InitialPositioner(GraphicPuzzle graphicPuzzle, Random rand) {
        this.puz = graphicPuzzle.getPuzzle();
        this.graphicPuzzle = graphicPuzzle;
        this.rand=rand;
    }

    @Override
    public void tick() {
        if (!isDone()) initialRoomPositions(graphicPuzzle, rand);
    }

    private void initialRoomPositions(GraphicPuzzle gp, Random random) {
        PosMapCalculator posCa = new PosMapCalculator();
        Map<Room, IntPosition> roomIntPositionMap = posCa.calculateRoomMap(puz);
        Map<IntPosition, Room> posMap = posCa.getPosMap();

        Map<String, Rectangle> rectangleByRoomName = new HashMap<>();
        for (Room room : puz.allRooms()) {
            IntPosition pos = roomIntPositionMap.get(room);
            Rectangle rectangle;
            if (!posMap.get(pos).equals(room))
            {
                if (room.getDoorNames().size()>1)
                {
                    logger.info("a room which was fitted in a space already "
                            +"occupied had more than one room attached. should be thrown out");
                }
                Room otherRoom = puz.getOtherRoom(room.getDoorNames().get(0),room);
                IntPosition otherRoomPosition = roomIntPositionMap.get(otherRoom);


                int xMin = (int) ((pos.x +otherRoomPosition.x)/2.0 * 2*Math.max(X_MAX, Y_MAX));
                int yMin = (int) ((pos.y  +otherRoomPosition.y/2.0)* 2*Math.max(X_MAX, Y_MAX));
                int sizeMult = room.getItemNames().size() + room.getDoorNames().size();
                float widthModifier = random.nextDouble() > 0.5f ? 1 : 0;
                float heightModifier = random.nextDouble() > 0.5f ? 1 : 0;
                rectangle = new Rectangle(xMin, yMin,
                        xMin + 2 * Math.min(sizeMult + 1 + widthModifier, X_MAX),
                        yMin + 2 * Math.min(sizeMult + 1 + heightModifier, Y_MAX));
            }
            else {

                int xMin = (int) (pos.x * 2* Math.max(X_MAX, Y_MAX));
                int yMin = (int) (pos.y * 2*Math.max(X_MAX, Y_MAX));
                int sizeMult = room.getItemNames().size() + room.getDoorNames().size();
                float widthModifier = random.nextDouble() > 0.5 ? 1 : 0;
                float heightModifier = random.nextDouble() > 0.5 ? 1 : 0;
                rectangle = new Rectangle(xMin, yMin,
                        xMin + 2 * Math.min(sizeMult + 1 + widthModifier, X_MAX),
                        yMin + 2 * Math.min(sizeMult + 1 + heightModifier, Y_MAX));
            }
            rectangleByRoomName.put(room.getName(),rectangle);
        }

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
