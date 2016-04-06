package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.Puzzle;

public class Key implements Item{

	private final Lockable lockable;
	private final String name;

	public Key(Lockable lockedDoor,String name) {
		this.lockable= lockedDoor;
		this.name = name;
	}

	public Lockable getLockable(Puzzle puz) {
		return puz.getLockable(lockable.getName());
	}


	public String getDescription() {
		return "key:"+name+" to "+(lockable==null?"nothing":lockable.getName());
		
	}

	@Override
	public String getName() {
		return name;
	}
}
