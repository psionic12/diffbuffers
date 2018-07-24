package com.diffbuffers.dbfclient.types;

import java.util.List;

public class StructDef extends CompositeType implements FixedSizeType {
    public StructDef(String typeName, NamespaceDef namespace, List<Identifier> fields) {
        super(typeName, namespace, fields);
    }

    public void addFiled(FieldType type, String name, int index) {
        this.fields().add(new Identifier(type, name, index, 0));
    }

    @Override
    public int size() {
        int size = 0;
        for (Identifier identifier : fields()) {
            FixedSizeType type = (FixedSizeType) identifier.type();
            size += type.size();
        }
        return size;
    }
}
