package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;
import javaslang.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by emanuelesan on 26/07/16.
 * * simulated annealing style.
 */
public class SimulatedAnnealingRoomPositioner implements TickWiseOperator {

    private final Puzzle puz;
    private final Random rand;
    private final GraphicPuzzle gp;
    int step = 0;


    private Map<String, Rectangle> rectsByRoom;


    public SimulatedAnnealingRoomPositioner(GraphicPuzzle gp, Random rand) {
        this.rand = rand;

        this.puz = gp.getPuzzle();

        rectsByRoom = gp.getRectsByRoom();
        this.gp = gp;

    }

    @Override
    public void tick() {

        if (1.0 / ++step > rand.nextDouble() || 0.1 > rand.nextDouble()) {
            List<Tuple2<Rectangle, Rectangle>> overlappingRectangles = gp.calculateTooDistantRectangles();
            if (overlappingRectangles.size() > 0) {
                Tuple2<Rectangle, Rectangle> rects = overlappingRectangles.get(rand.nextInt(overlappingRectangles.size()));

                List<Rectangle> intersectingRooms = searchRooms(rectsByRoom, rects._1(), rects._2());
                swapRectangles(rects._1(), intersectingRooms.get(rand.nextInt(intersectingRooms.size()))
                );
            }

        } else balanceDistances();


    }

    private List<Rectangle> searchRooms(Map<String, Rectangle> rectsByRoom, Rectangle rect1, Rectangle rect2) {
        Rectangle bbox = rect1.bbox(rect2);
        return rectsByRoom.values().stream().filter(r -> bbox.intersects(r) && !r.equals(rect2) && !r.equals(rect1)).collect(Collectors.toList());
    }

    @Override
    public boolean isDone() {
        return false;
    }

    private Vector2 pushDoorsClose(Rectangle rec, Room room) {
        return puz.getDoors(room)
                .stream()
                .map(door -> door.getInRoomName().equals(room.getName()) ? door.getOutRoomName() : door.getInRoomName())
                .map(rectsByRoom::get)
                .filter(rec1 -> !rec1.intersects(rec))
                .map(rec1 -> {
                    //push torwards rec1 above,below,left or right
                    Vector2 roomDims = new Vector2(
                            (rec.width() + rec1.width()) / 2D,
                            (rec.height() + rec1.height()) / 2D);
                    Vector2 direction = rec1.minusPos(rec);
                    Vector2 targetPosition = roomDims.point(new Vector2(Math.signum(direction.x),
                            Math.signum(direction.y)))
                            .plus(rec1.x(), rec1.y());
                    Vector2 targetDirection = targetPosition.minus(rec.x(), rec.y());
                    return targetDirection;

                })
                .reduce(Vector2::plus).orElse(Vector2.ZERO);
    }

    private void balanceDistances() {
        rectsByRoom.entrySet().forEach(entry ->
                {
                    Rectangle rec = entry.getValue();
                    Room room = puz.getRoom(entry.getKey());

                    Vector2 push = versorize(pushDoorsClose(rec, room));
                    rec.push(push);
                    rec.move();

                    Vector2 pull = versorize(pull(rec));
                    rec.push(pull);
                    rec.move();

                }
        );
    }

    private Vector2 pull(Rectangle rec) {
        return rectsByRoom.values().stream()
                .filter(r -> rec.intersectionArea(r) >= 1)
                .map(rec::minusPos)
                .reduce(Vector2::plus)
                .orElse(Vector2.ZERO);
    }

    /**
     * transforms it to its principal component versor
     */
    private Vector2 versorize(Vector2 vec) {
        return new Vector2(Math.signum(vec.x), Math.signum(vec.y));
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
    public boolean isPuzzleStillValid() {
        return true;
    }

}
