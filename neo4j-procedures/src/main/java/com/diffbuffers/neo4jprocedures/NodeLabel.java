package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.Label;

public enum NodeLabel implements Label {
    CLASS, STRUCT,
    NAMESPACE,

    // Singleton node types
    BOOL, INT8, UINT8, INT16, UINT16, INT32,
    UINT32, INT64, UINT64, FLOAT32, FLOAT64,
    ARRAY,

    DEFAULT_CLASS, DEFAULT_NAMESPACE,
    NON_ABSTRACT,
    INIT_LIST,
    ;
}
