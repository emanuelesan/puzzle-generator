package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class GraphicPuzzleProcessor {
    static final Logger logger = Logger.getLogger("logger");
    protected final Puzzle puz;
    final Queue<TickWiseOperator> operators;
    private boolean correct = true;


    public GraphicPuzzleProcessor(GraphicPuzzle gp, TickWiseOperator... operators) {
        this.puz = gp.getPuzzle();
        this.operators = new LinkedList<>();
        Stream.of(operators).forEachOrdered(this.operators::add);

    }

    public boolean execute() {
        while(!processingStep()) {
        }
        logger.finer("finished!!");
        return correct;

    }


     boolean processingStep() {
        operators.peek().tick();
        if (operators.peek().isDone()) {
            if (operators.size() > 1) {
                logger.finer("removed operator, welcome new operator! ");
                if (operators.peek().isPuzzleStillValid())
                    operators.poll();
                else {
                    correct = false;
                    return true;

                }
            } else return true;
        }
        return false;
    }
}
