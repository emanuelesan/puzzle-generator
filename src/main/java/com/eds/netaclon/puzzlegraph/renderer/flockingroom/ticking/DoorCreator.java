package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

import java.util.Map;

/**
 * creates doors between adjacent rooms.
 */
public class DoorCreator implements TickWiseOperator {
    public DoorCreator(Map<String, Rectangle> rectsByRoom) {
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean isDone() {
        return false;
    }
}
