package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.item.*;
import com.eds.netaclon.puzzlegraph.util.SequenceGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class Puzzle {

	private final Map<String, Room> roomMap;
	private final Map<String, Key> keyMap;
	private final Map<String, Door> doorMap;
	private final Map<String, Container> containerMap;

	private final List<Map<String,? extends Lockable>> lockables;
	private final List<Map<String,? extends Item>> items;

	private Room start;
	private Room end;

	public Puzzle()
	{
		roomMap = new HashMap<>();
		keyMap = new HashMap<>();
		doorMap = new HashMap<>();
		containerMap = new HashMap<>();
		lockables = new LinkedList<>();
		lockables.add(containerMap);
		lockables.add(doorMap);

		items = new LinkedList<>();
		items.add(containerMap);
		items.add(keyMap);

		start = new Room("Start");
		end = new Room("End");
		Door firstDoor =connectInwardRoom(start,end);
		start.setPathToEnd(firstDoor);
		roomMap.put(start.getName(), start);
		roomMap.put(end.getName(), end);
	}
	
	/** trasforms a door into a room . 
	 * @return a room which has two doors, his outRoom will be the former door's outroom,
	 * the same for the inroom.
	 *
	 * @param d the door that will be transformed into a room.
	 */
	public Room addRoom(Door d)
	{	Room newRoom = addRoom();
		
		Room outRoom = d.getOutRoom(this);
		Room inRoom = d.getInRoom(this);
		Door fromOutDoor =connectInwardRoom(outRoom,newRoom);
		Door toInDoor = connectInwardRoom(newRoom,inRoom);
		if (d.getName().equals(outRoom.getPathToEnd()))
		{outRoom.setPathToEnd(fromOutDoor);
		 newRoom.setPathToEnd(toInDoor);
		}
		removeDoor(d);
		
		return newRoom;
		
		
	}

	private void removeDoor(Door d)
	{	d.getInRoom(this).removeDoor(d);
		d.getOutRoom(this).removeDoor(d);
		doorMap.remove(d.getName());
	}

	/**@return a room */
	private Room addRoom() {
		Room newRoom = new Room("R"+SequenceGenerator.incrementAndGet("room"));
		roomMap.put(newRoom.getName(), newRoom);
		return newRoom;
	}

	/**
	 * creates a room reachable only from the given room r
	 * @param r the source room
	 * @return a new room, a secondary branch of the puzzle
	 */
	public Room carveRoomFrom(Room r)
	{	Room newRoom =addRoom();
		connectInwardRoom(r, newRoom);
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

	private Door connectInwardRoom(Room outRoom, Room inRoom) {
		Door door = new Door(outRoom, inRoom, "D"+SequenceGenerator.incrementAndGet("door"));
		doorMap.put(door.getName(),door);
		outRoom.addDoor(door);
		inRoom.addDoor(door);
		return door;
	}

	public Lockable getLockable(String name) {
		return lockables.stream().map(map->map.get(name)).filter(n->n!=null).findFirst().get();
	}

	public Container createContainer(Item item) {
		String containerName = "C" + SequenceGenerator.incrementAndGet("container");
		Container container = new Container(item, containerName);
		containerMap.put(containerName,container);
		return container;
	}

	public Key createKey(Lockable lockable) {
		String keyName = "K" + SequenceGenerator.incrementAndGet("key");
		Key key = new Key(lockable,keyName);
		keyMap.put(keyName,key);
		return key;
	}

	public List<Door> getDoors(Room room)
	{
		return
				room.getDoorNames().stream().map(doorMap::get).collect(Collectors.toList());}

	public Door getDoor(String doorName) {
		return doorMap.get(doorName);
	}

	public Map<String,Door> getDoorMap() {
		return doorMap;
	}


	public Item getItem(String itemName) {
		 	return items.stream().map(map->map.get(itemName)).filter(n->n!=null).findFirst().get();
	}

	public List<Item> getItems(List<String> itemNames) {
		return itemNames.stream().map(this::getItem).collect(Collectors.toList());
	}
}
