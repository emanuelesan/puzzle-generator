package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeed;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeeder;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedContainerPlot;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedDoorPlot;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.FlockingRoomsRenderer;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.GraphicPuzzleProcessor;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


public class PuzzleCreator {
    private static final Logger logger = Logger.getLogger("logger");


    private final List<PlotSeeder> seedBag;
    private final Random rand;

    private PuzzleCreator(List<PlotSeeder> seedBag, Random rand) {
        this.seedBag = seedBag;
        this.rand = rand;
    }

    private Puzzle createPuzzle(int steps) {
        Puzzle puz = new Puzzle();
        for (int step = 0; step < steps; step++) {

            List<PlotSeed> seeds = seedBag.get(rand.nextInt(seedBag.size())).semina(puz);
            if (!seeds.isEmpty()) {
                seeds.get(rand.nextInt(seeds.size())).germinate();
            }
        }
        return puz;
    }

    public static void main(String[] args) {

        Random rand = new Random(31);//31

        Puzzle puz = createPuzzleGraph(rand, 50);
        logger.info(puz.printInfo());
        GraphicPuzzle graphicPuzzle = new GraphicPuzzle(puz);

        TickWiseOperator[] operators = new TickWiseOperator[]{
                new InitialPositioner(graphicPuzzle)
                , new FlockingRoomsPositioner(graphicPuzzle)
                , new Clamper(graphicPuzzle)
                , new DoorCreator(graphicPuzzle)
        };

        graphicProcessing(graphicPuzzle, operators);

    }

    /**
     * tries 2 times to create a graphical puzzle. if not, it will throw an happy wtf exception.
     */
    public static GraphicPuzzle createPuzzle(int seed, int complexity) {
        Random rand = new Random(seed);

        Puzzle puz = createPuzzleGraph(rand, complexity);
        logger.info(puz.printInfo());
        GraphicPuzzle graphicPuzzle = new GraphicPuzzle(puz);
        TickWiseOperator[] operators = new TickWiseOperator[]{
                new InitialPositioner(graphicPuzzle)
                , new FlockingRoomsPositioner(graphicPuzzle)
                , new Clamper(graphicPuzzle)
                , new DoorCreator(graphicPuzzle)

        };
        GraphicPuzzleProcessor graphicPuzzleProcessor = new GraphicPuzzleProcessor(graphicPuzzle, operators);

        graphicPuzzleProcessor.execute();

        return graphicPuzzle;


    }

    private static void graphicProcessing(GraphicPuzzle gp, TickWiseOperator... operators) {
        FlockingRoomsRenderer flockingRoomsRenderer = new FlockingRoomsRenderer(gp,
                operators
        );
        flockingRoomsRenderer.show();

    }

    public static Puzzle createPuzzleGraph(Random rand, int steps) {
        List<PlotSeeder> seedBag = new LinkedList<>();
        seedBag.add(new LockedDoorPlot());
        seedBag.add(new LockedContainerPlot());
        PuzzleCreator creator = new PuzzleCreator(seedBag, rand);
        return creator.createPuzzle(steps);
    }

}
