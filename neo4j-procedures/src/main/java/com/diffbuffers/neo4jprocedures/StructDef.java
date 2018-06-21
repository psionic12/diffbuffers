package com.diffbuffers.neo4jprocedures;

import java.util.TreeMap;

public class StructDef extends FieldDef {
    private NameSpaceDef mBelongsTo;
    private TreeMap<Integer, FieldDef> mFileds;
}
