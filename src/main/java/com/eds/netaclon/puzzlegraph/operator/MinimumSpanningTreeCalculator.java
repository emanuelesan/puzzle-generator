package com.eds.netaclon.puzzlegraph.operator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.util.MetaNode;

/**
 * creates the minimum spanning tree using a BreadthFirstSearch.
 * note, this will not necessarily result into a BFS minimum spanning tree.
 * @author SangregorioEm
 *
 */
public class MinimumSpanningTreeCalculator implements Operator {

	private List<MetaNode>  rooms = new LinkedList<>();
	private Map<Room,MetaNode> metaNodes  = new HashMap<>();
	private MetaNode root;
	private Puzzle puz;

	public void init(Puzzle puz) {
		this.puz = puz;
	}

	public void operate(Room currentRoom, Puzzle puz, List<Room> explored,
			List<Room> unexplored) {
		MetaNode mn = new MetaNode(currentRoom); 
		if (rooms.isEmpty())
		{
			root = mn;
		}
		else
		{
			MetaNode relative =findRelative(currentRoom);
			if (relative==null)
			{	
				throw new RuntimeException("relative metanode not found");
			}
			relative.getConnectedNodes().add(mn);
		}
		metaNodes.put(currentRoom, mn);
		rooms.add(0,mn);
	}

	private MetaNode findRelative(Room currentRoom) {
		for (Door d:puz.getDoors(currentRoom))
		{	Room otherRoom = d.getOutRoom(puz);
			if(metaNodes.containsKey(otherRoom))
			{	return metaNodes.get(otherRoom);
			}
		}
		return null;
	}



	public MetaNode getRoot() {
		return root;
	}


	public Map<Room,MetaNode> getMetaNodes()
	{
		return metaNodes;
	}
	
	

}
