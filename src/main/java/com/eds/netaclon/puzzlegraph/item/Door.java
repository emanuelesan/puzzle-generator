package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;

import java.util.LinkedList;
import java.util.List;

public class Door implements Lockable{
    private List<String> keyNames;
    private String outRoomName;
    private String inRoomName;
    private String name;


	public Door(Room outRoom, Room inRoom, String name) {
		this.outRoomName=outRoom.getName();
		this.inRoomName = inRoom.getName();
		this.name = name;
		keyNames = new LinkedList<>();
	}

	public Room getOutRoom(Puzzle puz) {
		return puz.getRoom( outRoomName);
	}

	public Room getInRoom(Puzzle puz) {
		return puz.getRoom(inRoomName);
	}


	@Override
	public void addKey(Key k) {
		keyNames.add(k.getName());
	}

	public boolean locked()
	{ return !keyNames.isEmpty();}

	public String description() {
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

	public List<String> getKeyNames() {
		return keyNames;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Door door = (Door) o;

		if (keyNames != null ? !keyNames.equals(door.keyNames) : door.keyNames != null) return false;
		if (outRoomName != null ? !outRoomName.equals(door.outRoomName) : door.outRoomName != null) return false;
		if (inRoomName != null ? !inRoomName.equals(door.inRoomName) : door.inRoomName != null) return false;
		return name != null ? name.equals(door.name) : door.name == null;

	}


	public Room getOtherRoom(Room start, Puzzle puz) {
		return puz.getRoom(start.getName().equals(inRoomName) ? outRoomName : inRoomName);
	}
}
