package com.eds.netaclon.puzzlegraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.InitialPositioner;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeeder;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedContainerPlot;
import com.eds.netaclon.puzzlegraph.plotseed.impl.LockedDoorPlot;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeed;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.FlockingRoomsPositioner;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.FlockingRoomsRenderer;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.Clamper;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.CorridorConnector;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.DoorCreator;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;


public class PuzzleCreator {
    private static final Logger logger = Logger.getLogger("logger");


    private final List<PlotSeeder> seedBag;
    private final Random rand;

     private PuzzleCreator(List<PlotSeeder> seedBag, Random rand) {
        this.seedBag = seedBag;
        this.rand = rand;
    }

     private Puzzle createPuzzle(int expansionCycles) {
        Puzzle puz = new Puzzle();
        for (int cycle = 0; cycle < expansionCycles; cycle++) {

            List<PlotSeed> seeds = seedBag.get(rand.nextInt(seedBag.size())).semina(puz);
            if (!seeds.isEmpty()) {
                seeds.get(rand.nextInt(seeds.size())).germinate();
            }
        }
        return puz;
    }

    public static void main(String[] args) {

        Random rand = new Random(1232134);

        Puzzle puz = createPuzzleGraph(rand, 20);
        logger.info(puz.printInfo());
        GraphicPuzzle graphicPuzzle = new GraphicPuzzle(puz);

        TickWiseOperator initialPositioner = new InitialPositioner(graphicPuzzle,rand);

        TickWiseOperator positioner = new FlockingRoomsPositioner(graphicPuzzle);

        TickWiseOperator clamper = new Clamper(graphicPuzzle);

        TickWiseOperator corridorConnector = new CorridorConnector(graphicPuzzle);

        TickWiseOperator doorCreator = new DoorCreator(graphicPuzzle);
        FlockingRoomsRenderer flockingRoomsRenderer = new FlockingRoomsRenderer(graphicPuzzle,initialPositioner, positioner, clamper, corridorConnector, doorCreator);
        flockingRoomsRenderer.show();

    }

    private static Puzzle createPuzzleGraph(Random rand, int complexity) {
        List<PlotSeeder> seedBag = new LinkedList<>();
        seedBag.add(new LockedDoorPlot(rand));
        seedBag.add(new LockedContainerPlot(rand));
        PuzzleCreator creator = new PuzzleCreator(seedBag, rand);
        return creator.createPuzzle(complexity);
    }

}
