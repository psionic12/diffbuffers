package com.diffbuffers.common;

import org.neo4j.graphdb.RelationshipType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum NodeRelationship implements RelationshipType {
    // for extension
    IS_A,
    // for NAME space
    BELONGS_TO,

    // for init list
    HAS_CONSTRUCTOR_ARGS,

    // for fields which is fixed(STRUCT, BASIC_TYPE)
    HAS_A_BOOL, HAS_A_INT8, HAS_A_UINT8, HAS_A_INT16, HAS_A_UINT16, HAS_A_INT32,
    HAS_A_UINT32, HAS_A_INT64, HAS_A_UINT64, HAS_A_FLOAT32, HAS_A_FLOAT64, HAS_A_STRUCT, HAS_A_ENUM,

    // for array field
    HAS_SOME,

    // for fields which is flexible(CLASS)
    REFERENCES_TO,;

    public static final Map<NodeRelationship, NodeLabel> sAllTypes =
            Collections.unmodifiableMap(new HashMap<NodeRelationship, NodeLabel>() {
        {
            put(HAS_A_BOOL, NodeLabel.BOOL);
            put(HAS_A_INT8, NodeLabel.INT8);
            put(HAS_A_UINT8, NodeLabel.UINT8);
            put(HAS_A_INT16, NodeLabel.INT16);
            put(HAS_A_UINT16, NodeLabel.UINT16);
            put(HAS_A_INT32, NodeLabel.INT32);
            put(HAS_A_UINT32, NodeLabel.UINT32);
            put(HAS_A_INT64, NodeLabel.INT64);
            put(HAS_A_UINT64, NodeLabel.UINT64);
            put(HAS_A_FLOAT32, NodeLabel.FLOAT32);
            put(HAS_A_FLOAT64, NodeLabel.FLOAT64);
            put(HAS_A_STRUCT, NodeLabel.STRUCT);
            put(HAS_SOME, NodeLabel.ARRAY);
            put(REFERENCES_TO, NodeLabel.CLASS);
            put(HAS_A_ENUM, NodeLabel.ENUM);
            put(HAS_CONSTRUCTOR_ARGS, NodeLabel.INIT_LIST);
            put(IS_A, NodeLabel.CLASS);
            put(BELONGS_TO, NodeLabel.NAMESPACE);
        }
    });

    public static final Map<NodeLabel, NodeRelationship> sClassAllowedType =
            Collections.unmodifiableMap(new HashMap<NodeLabel, NodeRelationship>() {
        {
            put(NodeLabel.BOOL, HAS_A_BOOL);
            put(NodeLabel.INT8, HAS_A_INT8);
            put(NodeLabel.UINT8, HAS_A_UINT8);
            put(NodeLabel.INT16, HAS_A_INT16);
            put(NodeLabel.UINT16, HAS_A_UINT16);
            put(NodeLabel.INT32, HAS_A_INT32);
            put(NodeLabel.UINT32, HAS_A_UINT32);
            put(NodeLabel.INT64, HAS_A_INT64);
            put(NodeLabel.UINT64, HAS_A_UINT64);
            put(NodeLabel.FLOAT32, HAS_A_FLOAT32);
            put(NodeLabel.FLOAT64, HAS_A_FLOAT64);
            put(NodeLabel.STRUCT, HAS_A_STRUCT);
            put(NodeLabel.ARRAY, HAS_SOME);
            put(NodeLabel.CLASS, REFERENCES_TO);
            put(NodeLabel.ENUM, HAS_A_ENUM);
        }
    });

    public static final Map<NodeLabel, NodeRelationship> sStructAllowedType =
            Collections.unmodifiableMap(new HashMap<NodeLabel, NodeRelationship>() {
        {
            put(NodeLabel.BOOL, HAS_A_BOOL);
            put(NodeLabel.INT8, HAS_A_INT8);
            put(NodeLabel.UINT8, HAS_A_UINT8);
            put(NodeLabel.INT16, HAS_A_INT16);
            put(NodeLabel.UINT16, HAS_A_UINT16);
            put(NodeLabel.INT32, HAS_A_INT32);
            put(NodeLabel.UINT32, HAS_A_UINT32);
            put(NodeLabel.INT64, HAS_A_INT64);
            put(NodeLabel.UINT64, HAS_A_UINT64);
            put(NodeLabel.FLOAT32, HAS_A_FLOAT32);
            put(NodeLabel.FLOAT64, HAS_A_FLOAT64);
            put(NodeLabel.STRUCT, HAS_A_STRUCT);
            put(NodeLabel.ENUM, HAS_A_ENUM);
        }
    });

    public static final Map<NodeLabel, NodeRelationship> sEnumAllowedType =
            Collections.unmodifiableMap(new HashMap<NodeLabel, NodeRelationship>() {
        {
            put(NodeLabel.UINT32, HAS_A_UINT32);
        }
    });

    public static NodeRelationship[] getRelationships(Map<NodeLabel, NodeRelationship> map) {
        return map.values().toArray(new NodeRelationship[map.size()]);
    }
}
