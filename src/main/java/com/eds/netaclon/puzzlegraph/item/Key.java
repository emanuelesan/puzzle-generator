package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.Puzzle;

public class Key implements Item {
    private  String lockableName;
    private  String name;

    public Key()
    {}

    public Key(Lockable lockedDoor, String name) {
        this();
        this.lockableName = lockedDoor.getName();
        this.name = name;
    }

    public Lockable getLockable(Puzzle puz) {
        return puz.getLockable(lockableName);
    }

    public String description() {
        return "key:" + name + " to " + lockableName;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        if (lockableName != null ? !lockableName.equals(key.lockableName) : key.lockableName != null) return false;
        return name != null ? name.equals(key.name) : key.name == null;

    }

    @Override
    public String toString() {
        return "Key{" +
                "lockableName='" + lockableName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
