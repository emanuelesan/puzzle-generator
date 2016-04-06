package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.util.SequenceGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple container. it's lockable.
 */
public class Container implements Lockable {
    private List<Key> keys;
    private List<Item> items;
    private String name;

    public Container(Item item, String name) {
        items = new LinkedList<>();
        keys = new LinkedList<>();
        this.name = name;
        items.add(item);
    }

    @Override
    public void addKey(Key k) {
        keys.add(k);
    }

    public boolean isLocked() {
        return !keys.isEmpty();
    }

    public String getDescription() {
        return "container:"+getName()+" "+ items.stream().map(item->item.getDescription()).collect(Collectors.joining(",","[","]"));
    }

    @Override
    public String getName() {
        return name;
    }
}
