package com.eds.netaclon.puzzlegraph.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.operator.MinimumSpanningTreeCalculator;

/**	Tarjan's Bridge-finding algorithm
 * 
 *  - Find a spanning forest of G (PuzzleExpander can only generate minimum spanning trees)
 *  - Create a rooted forest F from the spanning tree
 *  - Traverse the forest F in preorder and number the nodes. Parent nodes in the forest now have lower numbers than child nodes.
 *  - For each node v in preorder, do:
 *    *  Compute the number of forest descendants ND(v) for this node, by adding one to the sum of its children's descendants.
 *    *  Compute L(v), the lowest preorder label reachable from v by a path for which all but the 
 *    	 last edge stays within the subtree rooted at v. This is the minimum of the set consisting 
 *    	 of the values of L(w) at child nodes of v and of the preorder labels of nodes reachable
 *    	 from v by edges that do not belong to F.
 *    *  Similarly, compute H(v), the highest preorder label reachable by a path for which all but the last edge stays within the subtree rooted at v. This is the maximum of the set consisting of the values of H(w) at child nodes of v and of the preorder labels of nodes reachable from v by edges that do not belong to F.
 *    *  For each node w with parent node v, if L(w) = w and H(w) < w + ND(w) then the edge from v to w is a bridge.
 *
 * @author SangregorioEm
 *
 */
public class BridgeFinder {

	private final Puzzle puz;
	private Map<MetaNode, Integer> preorderLabel = new HashMap<MetaNode, Integer>();
	private Map<MetaNode, Integer> subTreeSize = new HashMap<MetaNode, Integer>();
	private Map<MetaNode, Integer> Lv = new HashMap<MetaNode, Integer>();
	private Map<MetaNode, Integer> Hv = new HashMap<MetaNode, Integer>();
	private MinimumSpanningTreeCalculator spanningTreeCalculator;
	private List<Door> bridges = new LinkedList<Door>();
	Integer labelNumber=0;

	public BridgeFinder(Puzzle puz) {
		this.puz =puz;
	}

	public List<Door> findBridges() {

		spanningTreeCalculator = new MinimumSpanningTreeCalculator();
		BreadthFirstExplorer.attraversaPuz(puz, spanningTreeCalculator);

		MetaNode root = spanningTreeCalculator.getRoot();
		calculatePreorderLabelAndSize(root);
		calculateLowerHigher(root);
		calculateBridges(spanningTreeCalculator.getRoot());
		return bridges;
	}


	private  int calculatePreorderLabelAndSize(MetaNode root) {
		int size = 1;
		preorderLabel.put(root,labelNumber++);
		for (MetaNode mn: root.getConnectedNodes())
		{
			size+=calculatePreorderLabelAndSize(mn);
		}
		subTreeSize.put(root, size);
		return size;
		
	}
	
	
	private void calculateLowerHigher(MetaNode node) {
		int lowerLabel = preorderLabel.get(node);
		int higherLabel = preorderLabel.get(node);
		for (MetaNode mn: node.getConnectedNodes())
		{	calculateLowerHigher(mn);
			lowerLabel = Math.min(Lv.get(mn),lowerLabel);
			higherLabel = Math.max(Hv.get(mn),higherLabel);
		}
		for (Door d:node.getRoom().getDoors())
		{	Room otherRoom = d.getInRoom(puz)!=node.getRoom()? d.getInRoom(puz):d.getOutRoom(puz);
			MetaNode associatedMn=spanningTreeCalculator.getMetaNodes().get(otherRoom);
			if (!associatedMn.getConnectedNodes().contains(node))
			{	lowerLabel =  Math.min(preorderLabel.get(associatedMn),lowerLabel);
				higherLabel = Math.max(preorderLabel.get(associatedMn),higherLabel);
			}
		}
		Hv.put(node,higherLabel);
		Lv.put(node,lowerLabel);
	}
	
	/**
	 *  For each node w with parent node v, if L(w) = w and H(w) < w + ND(w) then the edge from v to w is a bridge.
	 * @param node v
	 */
	private void calculateBridges(MetaNode node)
	{	
		for(MetaNode w:node.getConnectedNodes())
		{	if( Lv.get(w)==preorderLabel.get(w) && Hv.get(w)<preorderLabel.get(w)+subTreeSize.get(w))
			{
				Room vRoom = node.getRoom();
				Room wRoom = w.getRoom();
				
				for (Door d:vRoom.getDoors())
				{	if ((d.getOutRoom(puz)==vRoom && d.getInRoom(puz)==wRoom)
						||(d.getOutRoom(puz)==wRoom && d.getInRoom(puz)==vRoom))
					{bridges.add(d); break;
						
					}
					
				}
			}
			calculateBridges(w);
			
		}
	}



	
}
