package com.diffbuffers.common.types;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeType implements Type {
    private NameSpace space;
    private String type;

    private List<Identifier> fields =
            new ArrayList<>();

    public CompositeType(String type) {
        this(type, null, null);
    }

    public CompositeType(String type, NameSpace space) {
        this(type, space, null);
    }

    public CompositeType(String type, NameSpace space, ArrayList<Identifier> fields) {
        this.type = type;
        this.space = space;
        if (fields != null) {
            this.fields.addAll(fields);
        }
    }

    public NameSpace nameSpace() {
        return space;
    }

    public String typeName() {
        return type;
    }

    protected void addFiled(Identifier identifier) {
        this.fields.add(identifier);
    }

    protected List<Identifier> getFields() {
        return fields;
    }

    public int count() {
        return fields.size();
    }
}
