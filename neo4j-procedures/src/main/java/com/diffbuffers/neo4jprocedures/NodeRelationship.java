package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.HashMap;

public enum NodeRelationship implements RelationshipType {
    // for extension
    IS_A,
    // for NAME space
    BELONGS_TO,

    // for init list
    HAS_CONSTRUCTOR_ARGS,

    // for fields which is fixed(STRUCT, BASIC_TYPE)
    HAS_A_BOOL, HAS_A_INT8, HAS_A_UINT8, HAS_A_INT16, HAS_A_UINT16, HAS_A_INT32,
    HAS_A_UINT32, HAS_A_INT64, HAS_A_UINT64, HAS_A_FLOAT32, HAS_A_FLOAT64, HAS_A_STRUCT,

    // for array field
    HAS_SOME,

    // for fields which is flexible(CLASS)
    REFERENCES_TO,;

    private static final HashMap<NodeLabel, NodeRelationship> sTypeMap = new HashMap<NodeLabel, NodeRelationship>() {
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

        }
    };

    public static NodeLabel[] getFieldTypes() {
        return sTypeMap.keySet().toArray(new NodeLabel[sTypeMap.size()]);
    }

    public static RelationshipType[] getFieldRelationships() {
        return sTypeMap.values().toArray(new RelationshipType[sTypeMap.size()]);
    }

    public static NodeRelationship getFieldRelationByType(NodeLabel label) {
        return sTypeMap.get(label);
    }
}
