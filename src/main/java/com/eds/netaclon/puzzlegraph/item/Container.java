package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.util.SequenceGenerator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple container. it's lockable.
 */
public class Container implements Lockable {
    @JsonProperty
    private List<String> keyNames;
    @JsonProperty
    private List<Item> items;
    @JsonProperty
    private String name;

    public Container(Item item, String name) {
        items = new LinkedList<>();
        keyNames = new LinkedList<>();
        this.name = name;
        items.add(item);
    }

    @Override
    public void addKey(Key k) {
        keyNames.add(k.getName());
    }

    public boolean isLocked() {
        return !keyNames.isEmpty();
    }

    public String getDescription() {
        return "container:"+getName()+" "+ items.stream().map(item->item.getDescription()).collect(Collectors.joining(",","[","]"));
    }

    @Override
    public String getName() {
        return name;
    }
}
