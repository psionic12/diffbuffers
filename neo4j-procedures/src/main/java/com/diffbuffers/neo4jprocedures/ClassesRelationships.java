package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.RelationshipType;

public enum ClassesRelationships implements RelationshipType {
    // for extension
    IS_A,

    // for fields which is fixed(STRUCT, BASIC_TYPE)
    HAS_A_BOOL, HAS_A_INT8, HAS_A_UINT8, HAS_A_INT16, HAS_A_UINT16, HAS_A_INT32,
    HAS_A_UINT32, HAS_A_INT64, HAS_A_UINT64, HAS_A_FLOAT32, HAS_A_FLOAT64, HAS_A_STRUCT,

    // for array
    HAS_SOME,

    // for fields which is flexible(CLASS)
    LINKS_TO,

    // for NAME space
    BELONGS_TO
}
