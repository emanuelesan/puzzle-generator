package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;

/**
 * Created by emanuelesan on 16/08/16.
 */
public class ForceDirectedPositioner implements TickWiseOperator {
    GraphicPuzzle gp;


    @Override
    public void tick() {

    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return false;
    }


    @Override
    public long steps() {
        return 0L;
    }
}
