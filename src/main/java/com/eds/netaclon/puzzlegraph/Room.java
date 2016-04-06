package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.item.Item;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Room implements Serializable {
    private String pathToEnd;
    private List<String> doorNames;
    private List<String> itemNames;
    private int depth;
    private String name;

    public Room() {
        doorNames = new LinkedList<>();
        itemNames = new LinkedList<>();
    }

    public Room(String name) {
        this();
        this.setName(name);
    }

    public List<String> getDoorNames() {

        return doorNames;
    }


    public List<String> getItemNames() {
        return itemNames;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        String itemDescriptions = "";
        for (String item : itemNames) {
            itemDescriptions += ", " + item;
        }

        return name + ", depth: " + this.getDepth() + ", " + itemDescriptions;
    }

    public String getPathToEnd() {
        return pathToEnd;
    }

    public void setPathToEnd(Door pathToEnd) {
        this.pathToEnd = pathToEnd.getName();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Room) {
            return getName().equals(((Room) obj).getName());
        }
        return super.equals(obj);
    }

    public void addDoor(Door door) {
        doorNames.add(door.getName());
    }

    public void removeDoor(Door d) {
        doorNames.remove(d.getName());
    }

    public void addItem(Item k) {
        itemNames.add(k.getName());
    }

    public void removeItem(Item item) {
        itemNames.remove(item.getName());
    }
}
