package com.eds.netaclon.puzzlegraph.item;

import com.eds.netaclon.puzzlegraph.Puzzle;
import org.codehaus.jackson.annotate.JsonProperty;

public class Key implements Item {
    @JsonProperty
    private  String lockableName;
    @JsonProperty
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
}
