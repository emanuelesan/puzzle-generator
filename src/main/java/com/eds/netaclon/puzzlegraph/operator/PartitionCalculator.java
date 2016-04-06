package com.eds.netaclon.puzzlegraph.operator;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * will build up a set of Rooms that can be reached from Start, without entering a given door,
 * which should act as a bridge in the puzzle graph.
 */
public class PartitionCalculator implements Operator{


    private final Room forbiddenRoom;
    private Set<Room> startPartition, endPartition;

    public PartitionCalculator(Room inRoom) {
        forbiddenRoom = inRoom;
        startPartition = new HashSet<>();
        endPartition = new HashSet<>();
    }

    @Override
    public void init(Puzzle puz) {

        startPartition.add(puz.getStart());
    }

    @Override
    public void operate(Room currentRoom, Puzzle puz, List<Room> explored, List<Room> unexplored) {

        if (! forbiddenRoom.equals(currentRoom)
                &&
               puz.getDoors(currentRoom).stream().map(door->door.getOutRoom(puz)).anyMatch(startPartition::contains)
                )
        {
            startPartition.add(currentRoom);
        }
        else
        {
            endPartition.add(currentRoom);
        }

    }

    public Set<Room> getStartPartition() {
        return startPartition;
    }

    public Set<Room> getEndPartition() {
        return endPartition;
    }
}
