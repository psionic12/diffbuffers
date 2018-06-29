package com.diffbuffers.common.types;

import java.io.Serializable;

public class Identifier implements Serializable {
    private int index;
    private String name;
    private Type type;

    public Identifier(Type type, String name, int index) {
        this.index = index;
        this.name = name;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
