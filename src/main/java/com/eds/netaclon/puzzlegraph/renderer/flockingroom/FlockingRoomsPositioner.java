package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.graphics.IntPosition;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.PosMapCalculator;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;

import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * it makes pretty crystal dungeons by cooling a puzzle.
 * basically, you put a start, an end and you've made a full fledged rpg !
 */
public class FlockingRoomsPositioner implements TickWiseOperator {

    private static final Logger logger = Logger.getLogger("logger");

    private static final double ALPHA = .1;
    private final Puzzle puz;
    int step = 0;


    private Map<String, Rectangle> rectsByRoom;
    private int maxIterations;

    double startPushClose ;
    double startPushAway  ;

    double endPushClose  ;
    double endPushAway  ;

    public FlockingRoomsPositioner(GraphicPuzzle gp) {
        this.puz = gp.getPuzzle();

        rectsByRoom =gp.getRectsByRoom();


        startPushClose = .5;
        startPushAway = 1;

        endPushClose = 0;
        endPushAway = .1;
        maxIterations = 20 * 60*(int)(Math.floor(rectsByRoom.size()/10)+1);
    }



    @Override
    public void tick() {

        double pushCloseFactor = Math.max(startPushClose + (endPushClose - startPushClose) * ((double) step / maxIterations), 0);
        double pushAwayFactor =  Math.max(startPushAway + (endPushAway - startPushAway) * ((double) step / maxIterations), 0);
        double snapFactor = Math.max(0,(double)( step - maxIterations)/maxIterations);
        balanceDistances(pushCloseFactor, pushAwayFactor,snapFactor);

        step++;

    }

    public boolean isDone() {
        return step>=maxIterations+15*60;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return true;
    }

    public Map<String, Rectangle> getRectsByRoom() {
        return rectsByRoom;
    }

    private void balanceDistances(double pushCloseFactor, double pushAwayFactor,double snapFactor) {
        rectsByRoom.entrySet().forEach(entry ->
                {
                    Rectangle rec = entry.getValue();
                    Room room = puz.getRoom(entry.getKey());

                    pushDoorsClose(rec, room, pushCloseFactor);
                    pushAwayOverlapped(rec, pushAwayFactor);

                    rec.move();
                    snapToGrid(rec, snapFactor);
                    rec.move();
                }
        );
    }

    private void snapToGrid(Rectangle rec,double snapFactor) {
        Vector2 push = new Vector2(-rec.x() + Math.round(rec.x()),
                -rec.y() + Math.round(rec.y()));
        rec.push(push.times(ALPHA * snapFactor));
    }

    private void pushAwayOverlapped(Rectangle rec, double fact) {
        rectsByRoom.values().stream()
                .map(rec1 -> rec.minusPos(rec1)
                        .times(ALPHA * fact * rec1.intersectionArea(rec)))
                .forEach(rec::push);
    }

    private void pushDoorsClose(Rectangle rec, Room room, double factor) {
       puz.getDoors( room)
                .stream()
                .map(door -> door.getInRoomName() .equals( room.getName()) ? door.getOutRoomName() : door.getInRoomName())
                .map(rectsByRoom::get)
                .forEach(rec1 -> {
                    //push torwards rec1 above,below,left or right
                    Vector2 roomDims = new Vector2(
                            (rec.width() + rec1.width()) / 2D,
                            (rec.height() + rec1.height()) / 2D);
                    Vector2 direction = rec1.minusPos(rec);
                    Vector2 targetPosition = roomDims.point(new Vector2(Math.signum(direction.x),
                            Math.signum(direction.y)))
                            .plus(rec1.x(), rec1.y());
                    Vector2 targetDirection = targetPosition.minus(rec.x(), rec.y());

                    rec.push(targetDirection.times(ALPHA * factor));
                });
    }



}
