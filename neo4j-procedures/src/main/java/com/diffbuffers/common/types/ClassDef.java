package com.diffbuffers.common.types;

import java.util.ArrayList;

public class ClassDef extends CompositeType implements FlexibleSizeType {
    private ClassDef parent;
    private int id;

    public ClassDef(String typeName) {
        super(typeName);
    }

    public ClassDef(String typeName, NameSpace nameSpace) {
        super(typeName, nameSpace);
    }

    public ClassDef(String typeName, NameSpace nameSpace, ArrayList<Identifier> fields) {
        super(typeName, nameSpace, fields);
    }

    public void addField(Type type, String name, int index) {
        this.getFields().add(new Identifier(type, name, index));
    }
}
