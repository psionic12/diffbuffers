package com.diffbuffers.dbfclient.types;

import java.util.List;

public class EnumDef extends CompositeType implements FixedSizeType {

    public EnumDef(String name, NamespaceDef space, List<Identifier> fields) {
        super(name, space, fields);
    }

}
