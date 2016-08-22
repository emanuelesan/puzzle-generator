package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class GraphicPuzzleProcessor {
    static final Logger logger = Logger.getLogger("logger");
    protected final Puzzle puz;
    final Queue<TickWiseOperator> operators;
    private final Function<TickWiseOperator, Boolean> terminationFunction;
    private boolean correct = true;


    public GraphicPuzzleProcessor(GraphicPuzzle gp, TickWiseOperator... operators) {
        this(gp, operators, (i) -> false);

    }

    public GraphicPuzzleProcessor(GraphicPuzzle gp, TickWiseOperator[] operators, Function<TickWiseOperator, Boolean> terminate) {
        this.puz = gp.getPuzzle();
        this.operators = new LinkedList<>();
        Stream.of(operators).forEachOrdered(this.operators::add);
        terminationFunction = terminate;
    }

    public boolean execute() {
        while (!processingStep()) ;
        logger.finer("finished!!");
        return correct;

    }


     boolean processingStep() {
         TickWiseOperator currentOperator = operators.peek();
         currentOperator.tick();
         if (terminationFunction.apply(currentOperator)) {
             logger.fine("puzzle creation failed.");
             correct = false;
             return true;
         }
         if (currentOperator.isDone()) {
            if (operators.size() > 1) {
                logger.finer("removed operator, welcome new operator! ");
                if (currentOperator.isPuzzleStillValid())
                    operators.poll();
                else {
                    logger.fine("puzzle creation failed.");
                    correct = false;
                    return true;

                }
            } else return true;
        }
        return false;
    }
}
