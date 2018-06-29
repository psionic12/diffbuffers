package com.diffbuffers.common.types;

public class StructDef extends CompositeType implements FixedSizeType {
    public StructDef(String typeName) {
        super(typeName);
    }

    public StructDef(String typeName, NameSpace nameSpace) {
        super(typeName, nameSpace);
    }

    public void addFiled(FixedSizeType type, String name, int index) {
        this.getFields().add(new Identifier(type, name, index));
    }

    @Override
    public int size() {
        int size = 0;
        for (Identifier identifier : getFields()) {
            size += identifier.getType().size();
        }
        return size;
    }
}
