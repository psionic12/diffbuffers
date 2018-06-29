package com.diffbuffers.common.types;

public class PointorDef extends BasicType {
    private int size = 0;

    // size should be told by neo4j
    public PointorDef(int size) {
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }
}
