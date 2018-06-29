package com.diffbuffers.common.types;

import java.io.Serializable;

public final class NameSpace implements Serializable {
    private NameSpace space;
    private String identifier;

    public NameSpace(String identifier) {
        this(identifier, null);
    }

    public NameSpace(String identifier, NameSpace space) {
        this.space = space;
        this.identifier = identifier;
    }

    public String name() {
        return identifier;
    }

    public NameSpace belongsTo() {
        return space;
    }
}
