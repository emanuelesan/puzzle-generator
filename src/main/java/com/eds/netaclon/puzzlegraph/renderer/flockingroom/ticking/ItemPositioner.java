package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.item.Container;
import com.eds.netaclon.puzzlegraph.item.Item;
import com.eds.netaclon.puzzlegraph.item.Key;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by emanuelesan on 21/08/16.
 */
public class ItemPositioner implements TickWiseOperator {


    private final GraphicPuzzle gp;
    private final Random rand;
    private long stepCount;
    private boolean done = false;
    private final Queue<Room> roomQueue;


    public ItemPositioner(GraphicPuzzle gp) {
        this.rand = new Random(0);
        this.stepCount = 0l;
        this.gp = gp;
        roomQueue = new LinkedList<>(gp.getPuzzle().allRooms());

    }

    @Override
    public void tick() {

        if (roomQueue.isEmpty()) {
            done = true;
            return;
        }

        final Room currentRoom = roomQueue.poll();

        List<Item> items = gp.getPuzzle().getItems(currentRoom.getItemNames());
        items.forEach(item ->
                {
                    if (item instanceof Key || item instanceof Container) {
                        putSmallItemInRoom(item, currentRoom);
                    }
                }
        );
    }

    /**
     * avoid room borders.
     */
    private void putSmallItemInRoom(Item item, Room currentRoom) {
        Rectangle rectangle = gp.getRectsByRoom().get(currentRoom.getName());
        Rectangle possible;
        do {
            int xMin = (int) (rand.nextInt((int) rectangle.width() - 2) + rectangle.xMin() + 1);
            int yMin = (int) (rand.nextInt((int) rectangle.height() - 2) + rectangle.yMin() + 1);

            possible = new Rectangle(xMin, yMin, xMin + 1, yMin + 1);
        }
        while (overlapsAnything(possible));
        gp.getRectsByItem().put(item.getName(), possible);
    }


    private boolean overlapsAnything(final Rectangle possible) {
        return Stream.concat(gp.getRectsByDoor().values().stream(), gp.getRectsByItem().values().stream())
                .anyMatch(n -> possible.intersects(n) && possible.intersectionArea(n) >= 1);
    }


    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return true;
    }

    @Override
    public long steps() {
        return stepCount;
    }
}
