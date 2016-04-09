package com.eds.netaclon.puzzlegraph.item;

public interface Lockable extends Item {
	
	void addKey(Key k);
	
	boolean locked();


}
