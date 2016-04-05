package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.Puzzle;

public class Key implements Item{

	private final Lockable lockable;

	public Key(Lockable lockedDoor) {
		this.lockable= lockedDoor;
	}

	public Lockable getLockable(Puzzle puz) {
		return puz.getLockable(lockable.getName());
	}


	public String getDescription() {
		return "key to "+(lockable==null?"nothing":lockable.getName());
		
	}

	@Override
	public String getName() {
		return getDescription();
	}
}
