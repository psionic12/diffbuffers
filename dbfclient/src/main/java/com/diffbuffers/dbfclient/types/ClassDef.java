package com.diffbuffers.dbfclient.types;

import java.util.List;

public class ClassDef extends CompositeType implements FlexibleSizeType {
    private static int sClassFieldLength = 0;
    private ClassDef mParent;
    private int mId;
    private boolean mAbstract;

    public ClassDef(String typeName, ClassDef parent, NamespaceDef namespace, List<Identifier> fields, int id) {
        this(typeName, parent, namespace, fields);
        mId = id;
        int length = 32 - Integer.numberOfLeadingZeros(id);
        sClassFieldLength = Integer.max(sClassFieldLength, length);
        mAbstract = true;
    }

    public ClassDef(String typeName, ClassDef parent, NamespaceDef namespace, List<Identifier> fields) {
        super(typeName, namespace, fields);
        mParent = parent;
    }

    public void addField(FieldType type, String name, int index, int counts) {
        this.fields().add(new Identifier(type, name, index, counts));
    }

    public ClassDef parent() {
        return mParent;
    }
}
