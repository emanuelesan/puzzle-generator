package com.eds.netaclon.puzzlegraph.item;

import java.util.List;

public interface Lockable extends Item {
	
	List<Key> getKeys();
	
	boolean isLocked();

	default Key addKey()
	{	Key k =new Key(this);
		this.getKeys().add(k);
		return k;
	}

	

}
