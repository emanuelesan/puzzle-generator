package com.eds.netaclon.puzzlegraph.item;

import java.util.List;

public interface Lockable extends Item {
	
	List<Key> getKeys();
	
	boolean isLocked();

	default Key addKey(Key k)
	{	this.getKeys().add(k);
		return k;
	}

	

}
