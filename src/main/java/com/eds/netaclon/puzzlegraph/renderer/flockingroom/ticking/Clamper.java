package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;

/**
 * Created by emanuelesan on 11/07/16.
 */
public class Clamper implements TickWiseOperator {
    private final GraphicPuzzle gp;

    public Clamper(GraphicPuzzle graphicPuzzle) {
        this.gp = graphicPuzzle;
    }

    @Override
    public void tick() {
        gp.getRectsByRoom().values().forEach(rectangle->{
            snapToGrid(rectangle, 1);
            rectangle.move();
        });
    }

    private void snapToGrid(Rectangle rec, float snapFactor) {
        Vector2 push = new Vector2(-rec.x() + Math.round(rec.x()),
                -rec.y() + Math.round(rec.y()));
        rec.push(push.times(snapFactor));
    }


    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return true;
    }

    @Override
    public long steps() {
        return 0L;
    }
}
