package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.item.*;
import com.eds.netaclon.puzzlegraph.util.SequenceGenerator;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Puzzle {
	private static final Logger logger = Logger.getLogger(Puzzle.class.getName());
	private static final String KEY = "key";
	private static final String CONTAINER = "container";
	private static final String ROOM = "room";
	public static final String START = "Start";
	public static final String END = "End";


	private final Map<String, Room> roomMap;
	private final Map<String, Key> keyMap;
	private final Map<String, Door> doorMap;
	private final Map<String, Container> containerMap;

	private final List<Map<String,? extends Lockable>> lockables;
	private final List<Map<String,? extends Item>> items;

	private Room start;
	private Room end;

	private SequenceGenerator sequenceGenerator = new SequenceGenerator();

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

		start = new Room(START);
		end = new Room(END);
		Door firstDoor =connectInwardRoom(start,end);
		start.setPathToEnd(firstDoor);
		roomMap.put(start.getName(), start);
		roomMap.put(end.getName(), end);
	}
	
	/** trasforms a door into a room . 
	 * @return a room which has two doors, his outRoom will be the former door's outroom,
	 * the same for the inroom.
	 *
	 * @param d the door that will be transformed into a room. keys will be preserved for the inward door.
	 */
	public Room addRoom(Door d)
	{	Room newRoom = addRoom();
		
		Room outRoom = d.getOutRoom(this);
		Room inRoom = d.getInRoom(this);
		Door fromOutDoor =connectInwardRoom(outRoom,newRoom);
		Door toInDoor = connectInwardRoom(newRoom,inRoom);
		d.getKeyNames().forEach(keyName->toInDoor.addKey(keyMap.get(keyName)));
		if (d.getName().equals(outRoom.getPathToEndName()))
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
		Room newRoom = new Room("R"+sequenceGenerator.incrementAndGet(ROOM));
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

	public Collection<Room> allRooms() {
		return roomMap.values();
	}


	 String printInfo() {
		StringBuilder stringAppender= new StringBuilder();
		for (Room room : roomMap.values()) {
			stringAppender.append(room.description()).append("\n");
		}
		return stringAppender.toString();
	}

	public Room getRoom(String roomName) {
		return roomMap.get(roomName);
	}

	public Room getOtherRoom(String doorName,Room room)
    {
        Door door = this.getDoor(doorName);

        return door.getInRoomName().equals(room.getName())?
                door.getOutRoom(this)
                :door.getInRoom(this);
    }

	private Door connectInwardRoom(Room outRoom, Room inRoom) {
		Door door = new Door(outRoom, inRoom, "D"+sequenceGenerator.incrementAndGet("door"));
		doorMap.put(door.getName(),door);
		outRoom.addDoor(door);
		inRoom.addDoor(door);
		return door;
	}

	public Lockable getLockable(String name) {
		return lockables.stream().map(map->map.get(name)).filter(n->n!=null).findFirst().get();
	}

	public Container createContainer(Item item) {
		String containerName = "C" + sequenceGenerator.incrementAndGet(CONTAINER);
		Container container = new Container(item, containerName);
		containerMap.put(containerName,container);
		return container;
	}

	public Key createKey(Lockable lockable) {
		String keyName = "K" + sequenceGenerator.incrementAndGet(KEY);
		Key key = new Key(lockable,keyName);
		keyMap.put(keyName,key);
		return key;
	}

	public List<Door> getDoors(Room room)
	{
		return
				room.getDoorNames().stream().map(doorMap::get).collect(Collectors.toList());}

	 private Door getDoor(String doorName) {
		return doorMap.get(doorName);
	}

	public Map<String,Door> getDoorMap() {
		return doorMap;
	}


	 private Item getItem(String itemName) {
		 	return items.stream().map(map->map.get(itemName)).filter(n->n!=null).findFirst().get();
	}

	public List<Item> getItems(List<String> itemNames) {
		return itemNames.stream().map(this::getItem).collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Puzzle)) return false;

		Puzzle puzzle = (Puzzle) o;

		if (roomMap != null ? !roomMap.equals(puzzle.roomMap) : puzzle.roomMap != null) {
			logger.info("roommap : \n"+roomMap+" != \n"+puzzle.roomMap);
			return false;}
		if (keyMap != null ? !keyMap.equals(puzzle.keyMap) : puzzle.keyMap != null) {
			logger.info("keyMap : \n"+keyMap+" != \n"+puzzle.keyMap);
			return false;}
		if (doorMap != null ? !doorMap.equals(puzzle.doorMap) : puzzle.doorMap != null) {
			logger.info("doorMap : \n"+doorMap+" != \n"+puzzle.doorMap);
			return false;}
		if (containerMap != null ? !containerMap.equals(puzzle.containerMap) : puzzle.containerMap != null) {
			logger.info("containerMap : \n"+containerMap+" != \n"+puzzle.containerMap);
			return false;
		}
		if (lockables != null ? !lockables.equals(puzzle.lockables) : puzzle.lockables != null){
			logger.info("lockables : \n"+lockables+" != \n"+puzzle.lockables);
			return false;
		}
		if (items != null ? !items.equals(puzzle.items) : puzzle.items != null)
		{
			logger.info("items : \n"+items+" != \n"+puzzle.items);
			return false;
		}
//		if (start != null ? !start.equals(puzzle.start) : puzzle.start != null) return false;
//		return end != null ? end.equals(puzzle.end) : puzzle.end == null;
		return true;

	}

	List<Map<String, ? extends Item>> items() {
		return items;
	}
}
