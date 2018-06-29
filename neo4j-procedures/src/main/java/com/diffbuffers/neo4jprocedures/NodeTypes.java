package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.Label;

public enum NodeTypes implements Label {
    CLASS, STRUCT, NAMESPACE, ARRAY, FIXED_ARRAY,

    // UINT8, INT16...
    BASIC_TYPE
}
