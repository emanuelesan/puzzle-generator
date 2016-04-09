package com.eds.netaclon.puzzlegraph.plotseed.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.item.Key;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.PuzzleCreationConstants;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeed;
import com.eds.netaclon.puzzlegraph.plotseed.PlotSeeder;
import com.eds.netaclon.puzzlegraph.util.BridgeFinder;

/**
 * a door-item relationship (lock-key). generates a partition of reachable and
 * unreachable rooms. it's appliable whenever a room has another door going deep
 * which is different from the path to the End.
 * this way, a partition is created in the graph between start-connected nodes and end-connected ones.
 * 
 * alternative way: find a bridge, create a door.
 * 
 * @author SangregorioEm
 *
 */
public class LockedDoorPlot implements PlotSeeder {


    private static final double germinationChance = .5;
    private final Random rand;

    public LockedDoorPlot(Random rand) {
        this.rand = rand;
    }
	/**
	 * returns true whether the seed can germinate.
	 */
	public List<PlotSeed> semina(Puzzle puz) {
		BridgeFinder bFinder = new BridgeFinder(puz);
		return bFinder
                .findBridges()
                .stream()
                .filter(door->canGerminate(door, puz))
                .map(bridge -> new LockedDoorPlotSeed(puz, bridge))
                .filter(n -> rand.nextDouble() < germinationChance)
                .collect(Collectors.toList());
	}
	
	private boolean canGerminate(Door d, Puzzle puz)
	{

		Room r = d.getOutRoom(puz);
		return (r.getDoorNames().size()<PuzzleCreationConstants.MAX_DOORS_PER_ROOM && !d.locked());
	}

	private class  LockedDoorPlotSeed implements PlotSeed
	{	private Key k;
		private Puzzle puzzle;
		private Door lockedDoor;

		public LockedDoorPlotSeed(Puzzle puz, Door bridge) {
			this.puzzle = puz;
			lockedDoor = bridge;
		}

		public void germinate() {
			reshapePuzzle();
			k =puzzle.createKey(lockedDoor);
			lockedDoor.addKey(k);
			hideKey();
		}


		private void hideKey() {
			Room r = lockedDoor.getOutRoom(puzzle);
			Room toHideRoom=puzzle.carveRoomFrom(r);
			toHideRoom.addItem(k);
		}
		/**add another room where the lockedDoor is. new lockedDoor is the innermost.
		 the key will be hidden in an adjacent room.
		 */
		private void reshapePuzzle() {
			Room r =puzzle.addRoom(lockedDoor);
			for (Door d:puzzle.getDoors(r))
			{
				if (d.getInRoom(puzzle) != r)
				{	lockedDoor=d;
					break;
				}
			}
		}

	}
}
