package com.eds.netaclon.puzzlegraph.renderer;

import com.eds.netaclon.graphics.IntPosition;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.operator.DepthCalculator;
import com.eds.netaclon.puzzlegraph.util.BreadthFirstExplorer;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by emanuelesan on 01/04/16.
 */
public class PosMapCalculator {

    private static final Logger logger = Logger.getLogger("logger");
    private Map<IntPosition, Room> posMap = new HashMap<IntPosition, Room>();

    public Map<IntPosition, Room> getPosMap() {
        return posMap;
    }

    /**
     * note: this method works if it is proven that
     * there's a an injective function of higher depth rooms and lowerdepth nodes. in other words, there are no loops.
     * in other words, this is a plain tree.
     * doesn't work, 1/100000 of times.
     */
    public Map<Room, IntPosition> calculateRoomMap(Puzzle puz) {
        Map<Room, IntPosition> roomMap = new HashMap<Room, IntPosition>();
        BreadthFirstExplorer.attraversaPuz(puz, new DepthCalculator());
        List<Room> rooms = new LinkedList<>(puz.allRooms());
        Collections.sort(rooms, new Comparator<Room>() {

            public int compare(Room o1, Room o2) {
                int d1 = o1.getDepth();
                int d2 = o2.getDepth();
                if (d1 < d2) return -1;
                if (d1 == d2) return 0;
                return 1;
            }
        });
        List<IntPosition> candidates = new LinkedList<>();
        for (Room r : rooms) {
            if (roomMap.size() == 0) {
                roomMap.put(r, new IntPosition(0, 0));
                posMap.put(new IntPosition(0, 0), r);
            } else {
                for (Door d : puz.getDoors(r)) {
                    Room otherRoom = d.getInRoom(puz) != r ? d.getInRoom(puz) : d.getOutRoom(puz);

                    if (roomMap.containsKey(otherRoom)) {
                        IntPosition otherRoomPosition = roomMap.get(otherRoom);
                        if (!roomMap.containsValue(otherRoomPosition.up())) {
                            IntPosition up = (otherRoomPosition.up());
                            roomMap.put(r, up);
                            posMap.put(up, r);
                            break;
                        } else if (!roomMap.containsValue(otherRoomPosition.left())) {
                            IntPosition left = otherRoomPosition.left();
                            roomMap.put(r, left);
                            posMap.put(left, r);
                            break;
                        } else if (!roomMap.containsValue(otherRoomPosition.right())) {
                            IntPosition right = otherRoomPosition.right();
                            roomMap.put(r, right);
                            posMap.put(right, r);
                            break;
                        } else if (!roomMap.containsValue(otherRoomPosition.down())) {
                            IntPosition down = otherRoomPosition.down();
                            roomMap.put(r, down);
                            posMap.put(down, r);
                            break;
                        }else
                        {
                            List<Room> connectedDoors = puz.getDoors(otherRoom)
                                    .stream()
                                    .map(door -> door.getInRoomName().equals(otherRoom.getName()) ? d.getOutRoom(puz) : d.getInRoom(puz))
                                    .collect(Collectors.toList());
                            //keep a position for later.
                            Stream.of(otherRoomPosition.up(), otherRoomPosition.down(), otherRoomPosition.left(), otherRoomPosition.right())
                                    .filter(pos -> !connectedDoors.contains(posMap.get(pos))).
                                    forEach(intPosition -> candidates.add(intPosition));


                        }
                    }
                }
                if(!roomMap.containsKey(r)) {
                    logger.info("WARNING: room lost");
                    if (candidates.size()>0)
                    roomMap.put(r,candidates.get(0));
                }

            }
        }

        return roomMap;
    }

}
