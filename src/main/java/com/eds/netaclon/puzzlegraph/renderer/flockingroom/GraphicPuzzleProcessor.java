package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class GraphicPuzzleProcessor {
    private static final Logger logger = Logger.getLogger("logger");
    protected final Puzzle puz;
    private final Queue<TickWiseOperator> operators;


    public GraphicPuzzleProcessor(GraphicPuzzle gp, TickWiseOperator... operators) {
        this.puz = gp.getPuzzle();
        this.operators = new LinkedList<>();
        Stream.of(operators).forEachOrdered(this.operators::add);

    }

    public void execute() {
        while(!processingStep()) {
            System.out.print(".");
        }
        logger.info("finished!!");

    }


     boolean processingStep() {
        operators.peek().tick();
        if (operators.peek().isDone()) {
            if (operators.size() > 1) {
                logger.info("removed operator, welcome new operator! ");
                if (operators.peek().isPuzzleStillValid())
                    operators.poll();
                else
                    throw new RuntimeException("puzzle went bad.");
            } else return true;
        }
        return false;
    }
}
