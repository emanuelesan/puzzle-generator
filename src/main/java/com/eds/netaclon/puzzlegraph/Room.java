package com.eds.netaclon.puzzlegraph;

import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.item.Item;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Room implements Serializable {
    private Door pathToEnd;
    private List<Door> doors;
    private List<Item> items;
    private int depth;
    private String name;

    public Room() {
        doors = new LinkedList<>();
        items = new LinkedList<>();
    }

    public Room(String name) {
        this();
        this.setName(name);
    }

    public Door connectInwardRoom(Room room) {
        Door door = new Door(this, room);
        doors.add(door);
        room.getDoors().add(door);
        return door;

    }

    public List<Door> getDoors() {
        return doors;
    }


    public List<Item> getItems() {
        return items;
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
        for (Item item : items) {
            itemDescriptions += ", " + item.getDescription();
        }

        return name + ", depth: " + this.getDepth() + ", " + itemDescriptions;
    }

    public Door getPathToEnd() {
        return pathToEnd;
    }

    public void setPathToEnd(Door pathToEnd) {
        this.pathToEnd = pathToEnd;
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
}
