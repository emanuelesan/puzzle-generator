package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

/**
 * Created by emanuelesan on 04/04/16.
 */
public interface TickWiseOperator {

    /**
     * make a single tick of the whole processing.
     * returns true if the operation is completed.
     *
     * @return
     */
    void tick();

    boolean isDone();

    boolean isPuzzleStillValid();

}
