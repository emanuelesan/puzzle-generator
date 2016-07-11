package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

import java.util.Map;

/**
 * creates doors between adjacent rooms.
 */
public class DoorCreator implements TickWiseOperator {
    public DoorCreator(GraphicPuzzle rectsByRoom) {
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return true;
    }
}
