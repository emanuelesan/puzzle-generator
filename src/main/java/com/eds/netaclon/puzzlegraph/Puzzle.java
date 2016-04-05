package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.item.Door;

import java.util.*;

public class Puzzle {

	private final Map<String, Room> roomMap;
//	List<Room> allRooms;


	private Room start;
	private Room end;
	
	public Puzzle()
	{
		start = new Room("Start");
		end = new Room("End");
		Door firstDoor =start.connectInwardRoom(end);
		start.setPathToEnd(firstDoor);
		roomMap = new HashMap<>();
		roomMap.put(start.getName(), start);
		roomMap.put(end.getName(), end);

	}
	
	/** trasforms a door into a room . 
	 * @return a room which has two doors, his outRoom will be the former door's outroom,
	 * the same for the inroom.
	 *
	 * @param d
	 */
	public Room addRoom(Door d)
	{	Room newRoom = addRoom();
		
		Room outRoom = d.getOutRoom(this);
		Room inRoom = d.getInRoom(this);
		Door fromOutDoor =outRoom.connectInwardRoom(newRoom);
		Door toInDoor = newRoom.connectInwardRoom(inRoom);
		if (outRoom.getPathToEnd()==d)
		{outRoom.setPathToEnd(fromOutDoor);
		 newRoom.setPathToEnd(toInDoor);
		}
		inRoom.getDoors().remove(d);
		outRoom.getDoors().remove(d);
		
		return newRoom;
		
		
	}

	/**a room, a secondary branch of the puzzle */
	private Room addRoom() {
		Room newRoom = new Room("R"+roomMap.size());
		roomMap.put(newRoom.getName(), newRoom);

		return newRoom;
	}

	/**
	 * creates a room reachable only from the given room r
	 * @param r
	 * @return
	 */
	public Room carveRoomFrom(Room r)
	{	Room newRoom =addRoom();
		r.connectInwardRoom(newRoom);
		return newRoom;
		
	}
	
	public Room getStart() {
		return start;
	}

	public Room getEnd() {
		return end;
	}

	public Collection<Room> getAllRooms() {
		return roomMap.values();
	}


	public String printInfo() {
		StringBuilder stringAppender= new StringBuilder();
		for (Room room : roomMap.values()) {
			stringAppender.append(room.getDescription()).append("\n");
		}
		return stringAppender.toString();
	}

	public Room getRoom(String roomName) {
		return roomMap.get(roomName);
	}
}
