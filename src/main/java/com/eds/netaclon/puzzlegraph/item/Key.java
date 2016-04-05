package com.eds.netaclon.puzzlegraph.item;

public class Key implements Item{

	private final Lockable lock;

	public Key(Lockable lockedDoor) {
		this.lock= lockedDoor;
	}

	public Lockable getLock() {
		return lock;
	}


	public String getDescription() {
		return "key to "+(lock==null?"nothing":lock.getName());
		
	}

	@Override
	public String getName() {
		return getDescription();
	}
}
