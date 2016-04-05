package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Door implements Lockable,Serializable{
	private List<Key> keys;
	private String outRoomName;
	private String inRoomName;

	public Door(Room outRoom, Room inRoom) {
		this.outRoomName=outRoom.getName();
		this.inRoomName = inRoom.getName();
		keys = new LinkedList<>();
	}

	public Room getOutRoom(Puzzle puz) {
		return puz.getRoom( outRoomName);
	}

	public Room getInRoom(Puzzle puz) {
		return puz.getRoom(inRoomName);
	}

	public List<Key> getKeys() {
		return keys;
	}

	public boolean isLocked()
	{ return !keys.isEmpty();}

	public String getDescription() {
		return "door from " + outRoomName + " to " + inRoomName;
	}

	@Override
	public String getName() {
		return getDescription();
	}
}
