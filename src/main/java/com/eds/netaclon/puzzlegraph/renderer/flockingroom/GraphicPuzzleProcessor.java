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

/**
 * Created by emanuelesan on 30/07/16.
 */
public class GraphicPuzzleProcessor {
    private static final int LONG_SIDE_PIC = 800;
    private static final Logger logger = Logger.getLogger("logger");
    protected final Puzzle puz;
    protected final GraphicPuzzle graphicPuzzle;
    protected final Queue<TickWiseOperator> operators;
    protected Map<String, Rectangle> rectsByRoom;


    public GraphicPuzzleProcessor(GraphicPuzzle gp, TickWiseOperator... operators) {
        this.puz = gp.getPuzzle();
        rectsByRoom = gp.getRectsByRoom();
        this.graphicPuzzle = gp;
        this.operators = new LinkedList<>();
        Stream.of(operators).forEachOrdered(operator -> this.operators.add(operator));

    }

    public void execute() {
        while(!processingStep()) {
            System.out.print(".");
        }
        logger.info("finished!!");

    }


    protected boolean processingStep() {
        operators.peek().tick();
        if (operators.peek().isDone()) {
            if (operators.size() > 1) {
                logger.info("removed operator, welcome new operator! ");
                try {
                    String jsonMap = new GsonBuilder().create().toJson(graphicPuzzle);

                    logger.info("puz--> " + jsonMap);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                if (operators.peek().isPuzzleStillValid())
                    operators.poll();
                else
                    throw new RuntimeException("puzzle went bad.");
            } else return true;
        }
        return false;
    }
}
