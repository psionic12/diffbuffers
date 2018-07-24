package com.diffbuffers.dbfclient.types;

public class Identifier {
    private int mIndex;
    private String mName;
    private FieldType mType;
    private int mCounts;

    public Identifier(FieldType type, String name, int index, int counts) {
        this.mIndex = index;
        this.mName = name;
        this.mType = type;
        this.mCounts = counts;
    }

    public int index() {
        return mIndex;
    }

    public String name() {
        return mName;
    }

    public FieldType type() {
        return mType;
    }

    public int counts() {
        return mCounts;
    }
}
