package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

import java.util.Map;

/**
 * creates doors between adjacent rooms.
 * doors are 1x2 large.
 */
public class DoorCreator implements TickWiseOperator {

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
     * @param door
     */
    private void createDoor(Door door) {
        Map<String, Rectangle> rectsByRoom = gp.getRectsByRoom();
        Rectangle r1 = rectsByRoom.get(door.getInRoomName());
        Rectangle r2 = rectsByRoom.get(door.getOutRoomName());

        float xMin = Math.max(r1.xMin(), r2.xMin()),
                yMin = Math.max(r1.yMin(), r2.yMin()),
                xMax = Math.min(r1.xMax(), r2.xMax()),
                yMax = Math.min(r1.yMax(), r2.yMax());

        Rectangle doorRect;
        if (yMax - yMin > xMax - xMin) {
            doorRect=Rectangle.fromCenter((int)((xMin + xMax) / 2.0f),
                    (int)((yMin + yMax) / 2.0f)+.5f,
                    2, 1);
        }
        else
        {
            doorRect=Rectangle.fromCenter((int)((xMin + xMax) / 2.0f)+.5f,
                    (int)((yMin + yMax) / 2.0f),
                    1, 2);

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
}
