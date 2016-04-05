package com.eds.netaclon.puzzlegraph.operator;

import java.util.List;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;

/**
 * Operators work on breadth first searches.
 * @author SangregorioEm
 *
 */
public interface Operator {

	void init(Puzzle puz);
	void operate(Room currentRoom, Puzzle puz, List<Room> explored,
				 List<Room> unexplored);

	
}
