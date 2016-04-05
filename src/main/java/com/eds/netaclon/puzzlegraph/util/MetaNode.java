package com.eds.netaclon.puzzlegraph.util;

import java.util.LinkedList;
import java.util.List;

import com.eds.netaclon.puzzlegraph.Room;

public class MetaNode {
	
	private Room room;
	private List<MetaNode> connectedNodes = new LinkedList<MetaNode>();
	
	
	public MetaNode(Room r) {
		this.room = r;
	}


	public Room getRoom() {
		return room;
	}


	public void setRoom(Room room) {
		this.room = room;
	}


	public List<MetaNode> getConnectedNodes() {
		return connectedNodes;
	}


	public void setConnectedNodes(List<MetaNode> connectedNodes) {
		this.connectedNodes = connectedNodes;
	}


	
	

}
