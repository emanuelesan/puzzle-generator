package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.PuzzleCreator;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.GraphicPuzzleProcessor;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

public class FlockingRoomsPositionerTest {


    @Test
    @Ignore
    public void testAlotOfCombinations() throws Exception {
        int count = 0;

        for (int i = 0; i < 10000; i++) {
            Random rand = new Random(i);

            GraphicPuzzle gp = new GraphicPuzzle(PuzzleCreator.createPuzzleGraph(rand, 20), i);
            GraphicPuzzleProcessor graphicPuzzleProcessor = new GraphicPuzzleProcessor(gp, new RecursivePositioner(gp));
            graphicPuzzleProcessor.execute();

            if (!DoorCreator.canApply(gp)) {
                System.out.println("!!! " + i);
                count++;
            }
        }
        System.out.println("\n\nnot valid : " + count);
    }

//    @Test
//    @Ignore
//    public void testIncreasingNumber() throws Exception {
//        int count = 0;
//
//        for (int i = 0; i < 30; i++) {
//            Random rand = new Random(26);
//
//            GraphicPuzzle gp = new GraphicPuzzle(PuzzleCreator.createPuzzleGraph(rand, i), 26);
//            TickWiseOperator flockingRoomsPositioner = new FlockingRoomsPositioner(gp);
//            GraphicPuzzleProcessor graphicPuzzleProcessor = new GraphicPuzzleProcessor(gp, new RecursivePositioner(gp));
//            graphicPuzzleProcessor.execute();
//
//            if (!DoorCreator.canApply(gp)) {
//                System.out.println("!!! " + i);
//                count++;
//            }
//        }
//        System.out.println("\n\nnot valid : " + count);
//    }

}