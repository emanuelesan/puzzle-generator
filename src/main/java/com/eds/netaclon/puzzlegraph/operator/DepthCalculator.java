package com.eds.netaclon.puzzlegraph.operator;

import java.util.List;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;

public class DepthCalculator implements Operator {


	public void init(Puzzle puz) {
		for (Room r : puz.allRooms()) {
			r.setDepth(Integer.MAX_VALUE);
		}
		puz.getStart().setDepth(0);

	}

	public void operate(Room currentRoom, Puzzle puz, List<Room> explored,
			List<Room> unexplored) {
		for (Door d :puz.getDoors( currentRoom)) {
			Room otherRoom = d.getInRoom(puz) != currentRoom ? d.getInRoom(puz) : d
					.getOutRoom(puz);
			int otherRoomDepth = otherRoom.getDepth();
			if (otherRoomDepth < currentRoom.getDepth()) {
				currentRoom.setDepth(otherRoomDepth + 1);
			}
		}

	}

}
