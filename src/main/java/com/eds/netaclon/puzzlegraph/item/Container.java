package com.eds.netaclon.puzzlegraph.item;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple container. it's lockable.
 */
public class Container implements Lockable {
    private List<String> keyNames;
    private List<String> itemNames;
    private String name;

    public Container() {
    }

    public Container(Item item, String name) {
        this();
        itemNames = new LinkedList<>();
        keyNames = new LinkedList<>();
        this.name = name;
        itemNames.add(item.getName());
    }

    @Override
    public void addKey(Key k) {
        keyNames.add(k.getName());
    }

    public boolean locked() {
        return !keyNames.isEmpty();
    }

    public String description() {
        return "container:" + getName() + " " + itemNames.stream().collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public String getName() {
        return name;
    }
}
