package com.eds.netaclon.puzzlegraph.plotseed.impl;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.item.*;
import com.eds.netaclon.puzzlegraph.operator.PartitionCalculator;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeed;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeeder;
import com.eds.netaclon.puzzlegraph.util.BreadthFirstExplorer;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * puts an item inside a container.
 */
public class LockedContainerPlot implements PlotSeeder {

    private static final Logger logger = Logger.getLogger("logger");

    private static final double germinationChance = .3;
    private final Random rand;

    public LockedContainerPlot(Random rand) {
        this.rand = rand;
    }


    public List<PlotSeed> semina(Puzzle puz) {
        return puz
                .getAllRooms()
                .stream()
                .flatMap(room -> puz.getItems(room.getItemNames()).stream().filter(item -> !(item instanceof Container)).map(item -> new LockedContainerPlotSeed(room, item, puz)))
                .filter(n -> rand.nextDouble() < germinationChance).collect(Collectors.toList());


    }

    private class LockedContainerPlotSeed implements PlotSeed {
        private final Item item;
        private final Random rand;
        private Puzzle puz;
        private final Room room;


        public LockedContainerPlotSeed(Room room, Item item, Puzzle puz) {
            this.room = room;
            this.item = item;
            this.puz = puz;
            rand = new Random();
        }

        public void germinate() {
            room.removeItem(item);
            Container container =puz.createContainer(item);
            room.addItem(container);
            lock(container);


        }

        private void lock(Container container) {


            List<Room> roomsToHideKeyIn = new LinkedList<>(puz.getAllRooms());
            roomsToHideKeyIn.remove(room);
            //container contains only one item right now.
            // if item is a key, find all rooms the bisection reachable from the start
            // for that key and randomly choose one room.
            Optional.of(item)
                    .filter(item->item instanceof Key)
                    .map(item->((Key)item).getLockable(puz))
                    .filter(lockable -> lockable instanceof Door)
                    .ifPresent(door ->
                            {
                                logger.info("starting Partition Calculator..");
                                Room inRoom = ((Door) door).getInRoom(puz);
                                PartitionCalculator partitionCalculator = new PartitionCalculator(inRoom);
                                BreadthFirstExplorer.attraversaPuz(puz, partitionCalculator);
                                roomsToHideKeyIn.removeAll(partitionCalculator.getEndPartition());

                                logger.info("finished Partition Calculator.. i'm going to select among  Partition:" + roomsToHideKeyIn.size());

                            }
                    );

            if (roomsToHideKeyIn.size()>0) {
                Room selectedRoom = roomsToHideKeyIn.get(rand.nextInt(roomsToHideKeyIn.size()));
                Key k =puz.createKey(container);
                container.addKey(k);
                selectedRoom.addItem(k);
            }
        }
    }
}
