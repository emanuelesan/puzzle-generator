package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeed;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeeder;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedContainerPlot;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedDoorPlot;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.FlockingRoomsRenderer;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.GraphicPuzzleProcessor;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.DoorCreator;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.RecursivePositioner;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;

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

    private Puzzle process(int steps) {
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

        Random rand = new Random(26);//31

        Puzzle puz = createPuzzleGraph(rand, 30);
        logger.finer(puz.printInfo());
        GraphicPuzzle graphicPuzzle = new GraphicPuzzle(puz, 26);

        TickWiseOperator[] operators = new TickWiseOperator[]{
                new RecursivePositioner(graphicPuzzle)

                //   , new FlockingRoomsPositioner(graphicPuzzle)
                //    , new Clamper(graphicPuzzle)
                , new DoorCreator(graphicPuzzle)
        };

        graphicProcessing(graphicPuzzle, operators);

    }

    /**
     * tries n times to create a graphical puzzle. if not, it will throw an happy wtf exception.
     */
    public static GraphicPuzzle createPuzzle(int complexity) {
        do {
            long seed = (long) (Math.random() * 1000000);
            Random rand = new Random(seed);

            Puzzle puz = createPuzzleGraph(rand, complexity);
            logger.finer(puz.printInfo());
            GraphicPuzzle graphicPuzzle = new GraphicPuzzle(puz, seed);
            TickWiseOperator[] operators = new TickWiseOperator[]{
                    //new InitialPositioner(graphicPuzzle)
                    new RecursivePositioner(graphicPuzzle)
                    //  , new FlockingRoomsPositioner(graphicPuzzle)
                    //  , new Clamper(graphicPuzzle)
                    , new DoorCreator(graphicPuzzle)

            };
            GraphicPuzzleProcessor graphicPuzzleProcessor = new GraphicPuzzleProcessor(graphicPuzzle, operators);

            if (graphicPuzzleProcessor.execute())
                return graphicPuzzle;
        } while (true);
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
        return creator.process(steps);
    }

}
