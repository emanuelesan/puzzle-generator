package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.graphics.IntPosition;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.PosMapCalculator;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class InitialPositioner implements TickWiseOperator {

     private static final double X_MAX = 6;
    private static final double Y_MAX = 4;
    private final Puzzle puz;
    private final GraphicPuzzle graphicPuzzle;
    private final Random rand;

    public InitialPositioner(GraphicPuzzle graphicPuzzle, Random rand) {
        this.puz = graphicPuzzle.getPuzzle();
        this.graphicPuzzle = graphicPuzzle;
        this.rand=rand;
    }

    @Override
    public void tick() {
        initialRoomPositions(graphicPuzzle, rand);
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
                    throw new RuntimeException("a room which was fitted in a space already "
                            +"occupied had more than one room attached");
                }
                Room otherRoom = puz.getOtherRoom(room.getDoorNames().get(0),room);
                IntPosition otherRoomPosition = roomIntPositionMap.get(otherRoom);


                int xMin = (int) ((pos.x +otherRoomPosition.x)/2.0 * Math.max(X_MAX, Y_MAX));
                int yMin = (int) ((pos.y  +otherRoomPosition.y/2.0)* Math.max(X_MAX, Y_MAX));
                int sizeMult = room.getItemNames().size() + room.getDoorNames().size();
                double widthModifier = random.nextDouble() > 0.5 ? 1 : 0;
                double heightModifier = random.nextDouble() > 0.5 ? 1 : 0;
                rectangle = new Rectangle(xMin, yMin,
                        xMin + 2 * Math.min(sizeMult + 1 + widthModifier, X_MAX),
                        yMin + 2 * Math.min(sizeMult + 1 + heightModifier, Y_MAX));
            }
            else {

                int xMin = (int) (pos.x * Math.max(X_MAX, Y_MAX));
                int yMin = (int) (pos.y * Math.max(X_MAX, Y_MAX));
                int sizeMult = room.getItemNames().size() + room.getDoorNames().size();
                double widthModifier = random.nextDouble() > 0.5 ? 1 : 0;
                double heightModifier = random.nextDouble() > 0.5 ? 1 : 0;
                rectangle = new Rectangle(xMin, yMin,
                        xMin + 2 * Math.min(sizeMult + 1 + widthModifier, X_MAX),
                        yMin + 2 * Math.min(sizeMult + 1 + heightModifier, Y_MAX));
            }
            rectangleByRoomName.put(room.getName(),rectangle);
        }

        gp.getRectsByRoom().putAll(rectangleByRoomName);
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return true;
    }
}
