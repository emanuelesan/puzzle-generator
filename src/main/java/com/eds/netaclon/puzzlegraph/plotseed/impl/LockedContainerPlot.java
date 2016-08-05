package com.eds.netaclon.puzzlegraph.plotseed.impl;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.item.*;
import com.eds.netaclon.puzzlegraph.operator.PartitionCalculator;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeed;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeeder;
import com.eds.netaclon.puzzlegraph.util.BreadthFirstExplorer;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * puts an item inside a container.
 */
public class LockedContainerPlot implements PlotSeeder {

    private static final Logger logger = Logger.getLogger("logger");


    public LockedContainerPlot() {

    }


    public List<PlotSeed> semina(Puzzle puz) {
        List<PlotSeed> collect = puz
                .allRooms()
                .stream()
                .flatMap(room ->
                        puz
                            .getItems(room.getItemNames())
                            .stream()
                            .filter(item -> !(item instanceof Container))
                            .map(item -> new LockedContainerPlotSeed(room, item, puz))
                )
                .sorted(LockedContainerPlotSeed::sortByItemName)
                .collect(Collectors.toList());
        return collect;


    }

    private static class LockedContainerPlotSeed implements PlotSeed {
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
            Container container = puz.createContainer(item);
            room.addItem(container);
            lock(container);


        }

        private void lock(Container container) {


            List<Room> roomsToHideKeyIn = new ArrayList<>(puz.allRooms());
            roomsToHideKeyIn.remove(room);
            //container contains only one item right now.
            // if item is a key, find all rooms the bisection reachable from the start
            // for that key and randomly choose one room.
            Optional.of(item)
                    .filter(item -> item instanceof Key)
                    .map(item -> ((Key) item).getLockable(puz))
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

            if (roomsToHideKeyIn.size() > 0) {
                roomsToHideKeyIn.sort(Item::sortByName);
                Room selectedRoom = roomsToHideKeyIn.get(rand.nextInt(roomsToHideKeyIn.size()));
                Key k = puz.createKey(container);
                container.addKey(k);
                selectedRoom.addItem(k);
            }
        }

        public static int sortByItemName(LockedContainerPlotSeed seed1, LockedContainerPlotSeed seed2) {
            return Item.sortByName(seed1.item,seed2.item);
        }
    }
}
