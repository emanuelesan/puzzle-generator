package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import javaslang.Tuple2;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by emanuelesan on 31/07/16.
 */
public class PuzzleCreatorTest {
    @Test
    public void testCreatePuzzle() throws Exception {
        double durations = 0;
        int iterations = 100;
        for (int i = 0; i < iterations; i++) {
            Instant start = Instant.now();
            PuzzleCreator.createPuzzle(20);
            Duration duration = Duration.between(start, Instant.now());
            durations += duration.toMillis();
        }
        logger.info("duration : " + durations / iterations);
    }

    @Test
    @Ignore
    public void testMainCall() throws Exception {
        Instant start = Instant.now();
        for (int i = 0; i < 3000; i++) {
            GraphicPuzzle puzzle = PuzzleCreator.createPuzzle(20);
            //  System.out.println(puzzle.getSeed());
        }
        Duration between = Duration.between(start, Instant.now());
        System.out.println("\n\ntotal time: " + between);
    }

    private static final Logger logger = Logger.getLogger(Puzzle.class.getName());

    @Test
    @Ignore
    /**
     * this test could get an anomaly sometimes, and it will always be worth investigating it.
     */
    public void testPuzzlesAreAlwaysTheSame() throws Exception {
        logger.setLevel(Level.FINER);
        logger.info("doing what's right hell yea");
        Tuple2<Puzzle, Random> puzz = createPuzzle();
        long nextRand = puzz._2().nextLong();
        for (int i = 0; i < 2500; i++) {
            Tuple2<Puzzle, Random> puz = createPuzzle();
            if (!puzz._1().equals(puz._1())) {
                System.out.println(puzz._1().items());
                System.out.println(puz._1().items());

                Assert.assertTrue("iteration " + i, false);

            }
            if (nextRand != puz._2().nextLong()) {
                Assert.assertTrue("random state was inconsistent at iteration " + i, false);
            }

        }
    }

    private Tuple2<Puzzle, Random> createPuzzle() {
        Random rand = new Random(213);

        return new Tuple2<>(PuzzleCreator.createPuzzleGraph(rand, 11)
                , rand);
    }


}