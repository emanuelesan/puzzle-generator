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
	private String name;

	public Door(Room outRoom, Room inRoom, String name) {
		this.outRoomName=outRoom.getName();
		this.inRoomName = inRoom.getName();
		this.name = name;
		keys = new LinkedList<>();
	}

	public Room getOutRoom(Puzzle puz) {
		return puz.getRoom( outRoomName);
	}

	public Room getInRoom(Puzzle puz) {
		return puz.getRoom(inRoomName);
	}


	@Override
	public void addKey(Key k) {
		keys.add(k);
	}

	public boolean isLocked()
	{ return !keys.isEmpty();}

	public String getDescription() {
		return "door "+name+" from " + outRoomName + " to " + inRoomName;
	}

	@Override
	public String getName() {
		return  name;
	}

	public String getInRoomName() {
		return inRoomName;
	}

	public String getOutRoomName() {
		return outRoomName;
	}
}
