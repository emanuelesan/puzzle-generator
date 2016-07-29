package com.eds.netaclon.puzzlegraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.*;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeeder;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedContainerPlot;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedDoorPlot;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeed;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.FlockingRoomsRenderer;


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

        Random rand = new Random(12324);

        Puzzle puz = createPuzzleGraph(rand, 10);
        logger.info(puz.printInfo());
        GraphicPuzzle graphicPuzzle = new GraphicPuzzle(puz);

        TickWiseOperator initialPositioner = new InitialPositioner(graphicPuzzle,rand);



        TickWiseOperator corridorConnector = new CorridorConnector(graphicPuzzle);

        TickWiseOperator doorCreator = new DoorCreator(graphicPuzzle);
        FlockingRoomsRenderer flockingRoomsRenderer = new FlockingRoomsRenderer(graphicPuzzle,
                initialPositioner,
                new FlockingRoomsPositioner(graphicPuzzle)
           //     new SimulatedAnnealingRoomPositioner(graphicPuzzle,rand)
                ,new Clamper(graphicPuzzle)
                , corridorConnector
                ,doorCreator
                );
        flockingRoomsRenderer.show();

    }

    private static Puzzle createPuzzleGraph(Random rand, int steps) {
        List<PlotSeeder> seedBag = new LinkedList<>();
        seedBag.add(new LockedDoorPlot(rand));
        seedBag.add(new LockedContainerPlot(rand));
        PuzzleCreator creator = new PuzzleCreator(seedBag, rand);
        return creator.createPuzzle(steps);
    }

}
