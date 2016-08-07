package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.PuzzleCreator;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.GraphicPuzzleProcessor;
import org.junit.Test;

import java.util.Random;

/**
 * Created by emanuelesan on 06/08/16.
 */
public class FlockingRoomsPositionerTest {


    @Test
    public void testAlotOfCombinations() throws Exception {
        int count = 0;

        for (int i = 0; i < 100; i++) {
            Random rand = new Random(i);

            GraphicPuzzle gp = new GraphicPuzzle(PuzzleCreator.createPuzzleGraph(rand, 20));
            TickWiseOperator flockingRoomsPositioner = new FlockingRoomsPositioner(gp);
            GraphicPuzzleProcessor graphicPuzzleProcessor = new GraphicPuzzleProcessor(gp, new InitialPositioner(gp), flockingRoomsPositioner);
            graphicPuzzleProcessor.execute();

            if (!flockingRoomsPositioner.isPuzzleStillValid())
                count++;

        }
        System.out.println("\n\ncount : " + count);
    }

}