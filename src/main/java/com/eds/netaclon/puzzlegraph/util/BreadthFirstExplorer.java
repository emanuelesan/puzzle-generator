package com.eds.netaclon.puzzlegraph.util;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.operator.Operator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstExplorer {
	
	public static void attraversaPuz(Puzzle puz, Operator op) {
		// I start from the outermost one.
		op.init(puz);
		Room currentRoom = puz.getStart();

		Queue<Door> doorQueue = new LinkedList<>();
		List<Room> unexplored = new LinkedList<>();
		List<Room> explored = new LinkedList<>();
		unexplored.addAll(puz.allRooms());
		while (!unexplored.isEmpty()) {
			doorQueue.addAll(puz.getDoors(currentRoom));
			op.operate(currentRoom, puz, explored, unexplored);
			unexplored.remove(currentRoom);
			explored.add(0,currentRoom);

			while (!doorQueue.isEmpty()) {
				Door door = doorQueue.poll();
				if (unexplored.contains(door.getInRoom(puz))) {
					currentRoom = door.getInRoom(puz);
					break;
				}

			}

		}
	}
	

}
