package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.Label;

public enum NodeTypes implements Label {
<<<<<<< dbf32005c0226d8a6443290a1c9bc8f9b93d1175
    CLASS, STRUCT, NAMESPACE, ARRAY, FIXED_ARRAY,

    // UINT8, INT16...
    BASIC_TYPE
=======
    CLASS, STRUCT,
    NAMESPACE,
    ARRAY, FIXED_ARRAY,
    BASIC_TYPE,

    DEFAULT_CLASS, DEFAULT_NAMESPACE,
    NON_ABSTRACT,

>>>>>>> diffbufffers
}
