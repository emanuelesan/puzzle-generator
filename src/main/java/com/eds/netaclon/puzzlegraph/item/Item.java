package com.eds.netaclon.puzzlegraph.item;

import java.io.Serializable;

public interface Item extends Serializable {

	String description();
	String getName();

	static int sortByName(Item item, Item item1) {
		return item.getName().compareTo(item1.getName());
	}
}
