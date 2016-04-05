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

    private Container() {
        items = new LinkedList<>();
        keys = new LinkedList<>();
        name = "container"+ SequenceGenerator.incrementAndGet("container");
    }


    public Container(Item item) {
        this();
        items.add(item);
    }


    public List<Key> getKeys() {
        return keys;
    }

    public boolean isLocked() {
        return !keys.isEmpty();
    }

    public String getDescription() {
        return getName()+" : "+ items.stream().map(item->item.getDescription()).collect(Collectors.joining(",","[","]"));
    }

    @Override
    public String getName() {
        return name;
    }
}
