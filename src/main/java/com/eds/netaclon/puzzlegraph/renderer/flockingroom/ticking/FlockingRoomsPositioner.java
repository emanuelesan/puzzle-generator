package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * reduces distance between each connected room until
 * rooms are close enough to draw a door betwen each of them.
 */
public class FlockingRoomsPositioner implements TickWiseOperator {
    private static final float ALPHA = .1f;
    private static final int EXTRA_STEPS = 1000;// 15 * 60;
    private final Puzzle puz;
    private final GraphicPuzzle gp;
    private int step = 0;
    private Map<String, Rectangle> rectsByRoom;
    private int maxIterations;
    private float startPushClose;
    private float startPull;
    private float endPushClose;
    private float endPull;

    public FlockingRoomsPositioner(GraphicPuzzle gp) {
        this.puz = gp.getPuzzle();
        this.gp = gp;
        rectsByRoom = gp.getRectsByRoom();
        startPushClose = .551f;
        startPull = 1;
        endPushClose = 0;
        endPull = .3f;
        maxIterations = 2000 * 6 * (int) (Math.floor(rectsByRoom.size()) + 1);
    }

    /**
     * look for rooms intersecting the space between them.
     */
    private List<Rectangle> searchRooms(Map<String, Rectangle> rectsByRoom, Rectangle rect1, Rectangle rect2) {
        Rectangle bbox = rect1.intersection(rect2);
        return rectsByRoom.values().stream().filter(r -> bbox.intersects(r) && !r.equals(rect2) && !r.equals(rect1)).collect(Collectors.toList());
    }

    /**
     * swap rectangle positions
     */
    private void swapRectangles(Rectangle r1, Rectangle r2) {
        Vector2 toR1Movement = r1.minusPos(r2);
        Vector2 toR2Movement = r2.minusPos(r1);
        r1.push(toR2Movement);
        r2.push(toR1Movement);
        r1.move();
        r2.move();
    }

    @Override
    public void tick() {
        float pushCloseFactor = Math.max(startPushClose + (endPushClose - startPushClose) * ((float) step / maxIterations), 0);
        float pullFactor = Math.max(startPull + (endPull - startPull) * ((float) step / maxIterations), 0);
        float snapFactor = step > maxIterations ? 1 : Math.max(0, (float) (step - maxIterations) / maxIterations);
        balanceDistances(pushCloseFactor, pullFactor, snapFactor);
        step++;
    }

    public boolean isDone() {
        return step >= maxIterations + EXTRA_STEPS;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return DoorCreator.canApply(gp);
    }

    private void balanceDistances(float pushCloseFactor, float pullFactor, float snapFactor) {

        rectsByRoom.entrySet()
                .stream()
                .sorted((entry1, entry2) ->
                        Integer.compare(puz.getRoom(entry1.getKey()).getDepth(),
                                puz.getRoom(entry2.getKey()).getDepth())
                )
                .forEach(entry ->
                        {
                            Rectangle rec = entry.getValue();
                            Room room = puz.getRoom(entry.getKey());
                            Vector2 push = pushDoorsClose(rec, room, pushCloseFactor);
                            Vector2 pull = pull(rec, pullFactor);
                            rec.push(push);
                            rec.push(pull);
                            rec.move();
                            snapToGrid(rec, snapFactor);
                            rec.move();
                        }
                );
    }


    private void snapToGrid(Rectangle rec, float snapFactor) {
        Vector2 push = new Vector2(-rec.x() + Math.round(rec.x()),
                -rec.y() + Math.round(rec.y()));
        rec.push(push.times(snapFactor));
    }

    private Vector2 pull(Rectangle rec, float fact) {
        return rectsByRoom.values().stream()
                .map(rec1 -> rec.minusPos(rec1)
                        .times(ALPHA * fact * rec1.intersectionArea(rec)))
                .reduce(Vector2::plus)
                .orElse(Vector2.ZERO);
    }

    private Vector2 pushDoorsClose(Rectangle rec, Room room, float factor) {
        return puz.getDoors(room)
                .stream()
                .map(door -> door.getInRoomName().equals(room.getName()) ? door.getOutRoomName() : door.getInRoomName())
                .map(rectsByRoom::get)
                .map(rec1 -> {
                    //push torwards rec1 above,below,left or right
                    Vector2 roomDims = new Vector2(
                            (rec.width() + rec1.width()) / 2,
                            (rec.height() + rec1.height()) / 2);
                    Vector2 direction = rec1.minusPos(rec);
                    Vector2 targetPosition = roomDims.point(new Vector2(direction.x > 0 ? 1 : -1,
                            direction.y > 0 ? 1 : 0))
                            .plus(rec1.x(), rec1.y());
                    Vector2 targetDirection = targetPosition.minus(rec.x(), rec.y());
                    return targetDirection.times(ALPHA * factor);
                })
                .reduce(Vector2::plus)
                .orElse(Vector2.ZERO);
    }


    @Override
    public long steps() {
        return 0L;
    }
}
