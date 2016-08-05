package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedContainerPlot;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedDoorPlot;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by emanuelesan on 31/07/16.
 */
public class PuzzleCreatorTest {
    private static final Logger logger = Logger.getLogger(Puzzle.class.getName());

    @Test
    /**
     * this test could get an anomaly sometimes, and it will always be worth investigating it.
     */
    public void testPuzzlesAreAlwaysTheSame() throws Exception {
        logger.setLevel(Level.FINER);
        logger.info("doing what's right hell yea");
        Puzzle puzz = createPuzzle();
        for (int i =0;i<20;i++) {
            Puzzle puz = createPuzzle();
            if (!puzz.equals(puz))
            {
                System.out.println(puzz.items());
                System.out.println(puz.items());

                Assert.assertTrue("iteration "+i,false);

            }
        }
    }

    private Puzzle createPuzzle()
    {Random rand = new Random(213);
        PuzzleCreator puzzleCreator= new PuzzleCreator(Arrays.asList( new LockedDoorPlot()
                ,new LockedContainerPlot() )
                ,rand);
        return puzzleCreator.createPuzzle(11);
    }

}