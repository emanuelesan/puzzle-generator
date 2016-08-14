package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.PuzzleCreator;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.GraphicPuzzleProcessor;
import org.junit.Test;

import java.util.Random;

public class FlockingRoomsPositionerTest {


    @Test
    public void testAlotOfCombinations() throws Exception {
        int count = 0;

        for (int i = 0; i < 1000; i++) {
            Random rand = new Random(i);

            GraphicPuzzle gp = new GraphicPuzzle(PuzzleCreator.createPuzzleGraph(rand, 20), i);
            TickWiseOperator flockingRoomsPositioner = new FlockingRoomsPositioner(gp);
            GraphicPuzzleProcessor graphicPuzzleProcessor = new GraphicPuzzleProcessor(gp, new RecursivePositioner(gp));
            graphicPuzzleProcessor.execute();

            if (!DoorCreator.canApply(gp)) {
                System.out.println("!!! " + i);
                count++;
            }
        }
        System.out.println("\n\nnot valid : " + count);
    }

}