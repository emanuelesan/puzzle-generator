package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;

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
            rectangle.roundCoords();

        });
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
