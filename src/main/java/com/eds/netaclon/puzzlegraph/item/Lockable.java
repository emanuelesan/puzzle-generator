package com.eds.netaclon.puzzlegraph.item;

import java.util.List;

public interface Lockable extends Item {
	
	void addKey(Key k);
	
	boolean isLocked();


}
