package com.eds.netaclon.puzzlegraph.operator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;

public class PuzzleExpander implements Operator {

	private static final Logger logger = Logger.getLogger("logger");
	private static final double NEW_ROOM_CHANCE = .5;
	private static final double DOOR_TO_ROOM_CHANCE = .1;
	private final Random rand;


	public PuzzleExpander(Random rand)
	{this.rand = rand;}

	public void init(Puzzle puz) {
	}

	
	/**
	 * una ipotetica chiave per una porta fra quelle in questa stanza potrebbe
	 * trovarsi nelle stanze unexplored raggiungibili senza usare la stessa
	 * porta. Il modo migliore per effettuare questo sarebbe quello di creare
	 * una serie di partizioni asimmetriche del grafo partendo dai nodi pi�
	 * vicini all'end e andando a ritroso, lasciando sempre di meno.
	 * 
	 * @param currentRoom
	 * @param puz
	 * @param explored
	 * @param unexplored
	 */
	public void operate(Room currentRoom, Puzzle puz, List<Room> explored,
			List<Room> unexplored) {
		if (rand.nextDouble() < NEW_ROOM_CHANCE && puz.getEnd() != currentRoom) {
			logger.info("created room :)");
			puz.carveRoomFrom(currentRoom);
		}
		LinkedList<Door> doorListCopy = new LinkedList<Door>();
		doorListCopy.addAll(currentRoom.getDoors());// using the actual list
													// would cause a concurrency
													// problem.
		for (Door d : doorListCopy) {// only change already explored doors
			if (rand.nextDouble() < DOOR_TO_ROOM_CHANCE
					+ Math.log(Math.E + (d.getInRoom(puz).getDepth() - d.getOutRoom(puz).getDepth())) - 1
					&& explored.contains(d.getOutRoom(puz))) {
				logger.info("transformed door to room, depth difference: "+ (d.getInRoom(puz).getDepth() - d.getOutRoom(puz).getDepth()));
				puz.addRoom(d);
			}
		}
	}
	
	

}
