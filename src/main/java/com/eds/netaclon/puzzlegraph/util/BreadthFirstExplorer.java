package com.eds.netaclon.puzzlegraph.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.operator.Operator;

public class BreadthFirstExplorer {
	
	public static void attraversaPuz(Puzzle puz, Operator op) {
		// I start from the outermost one.
		op.init(puz);
		Room currentRoom = puz.getStart();
		
		Queue<Door> doorQueue = new LinkedList<Door>();
		List<Room> unexplored = new LinkedList<Room>();
		List<Room> explored = new LinkedList<Room>();
		unexplored.addAll(puz.getAllRooms());
		while (!unexplored.isEmpty()) {
			doorQueue.addAll(currentRoom.getDoors());
			op.operate(currentRoom, puz, explored, unexplored);
			unexplored.remove(currentRoom);
			explored.add(0,currentRoom);

			while (!doorQueue.isEmpty()) {
				Door door = doorQueue.poll();
				if (unexplored.contains(door.getInRoom(puz))) {
					Room nextRoom = door.getInRoom(puz);
					currentRoom = nextRoom;
					break;
				}

			}

		}
	}
	

}
