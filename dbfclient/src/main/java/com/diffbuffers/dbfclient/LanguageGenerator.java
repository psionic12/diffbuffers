package com.diffbuffers.dbfclient;

import com.diffbuffers.common.NodeLabel;
import com.diffbuffers.common.NodeRelationship;
import com.diffbuffers.common.Records;
import com.diffbuffers.dbfclient.language.Builder;
import com.diffbuffers.dbfclient.types.*;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

import java.io.File;
import java.util.*;

public class LanguageGenerator {
    private Session mSession;
    private Map<Long, Type> mTypes = new HashMap<>();

    public LanguageGenerator(Session session) {
        mSession = session;
    }

    public void prepare() {
        String statement = String.format("MATCH (p:%s) RETURN ID(p) AS id",
                NodeLabel.DEFAULT_NAMESPACE);
        StatementResult result = mSession.run(statement);
        Record record = result.single();
        travelNode(record.get("id").asLong());
    }

    public void build(Builder builder, File dir) {
        for (Type type : mTypes.values()) {
            builder.build(type);
        }
    }

    private void travelNode(long id) {
        if (mTypes.containsKey(id)) return;
        NamespaceDef namespaceDef = null;
        List<Identifier> identifiers = new ArrayList<>();
        ClassDef parent = null;
        Map<String, Object> initList = null;


        // deal with the relationships
        StatementResult result = mSession.run(String.format("match (p)-[r]->() where id(p) = %d return r", id));
        while (result.hasNext()) {
            Relationship relationship = result.next().get("r").asRelationship();
            if (relationship.type().equals(NodeRelationship.HAS_CONSTRUCTOR_ARGS.name())) {
                result = mSession.run(String.format("match (p) where id(p) = %d return properties(p)", relationship.endNodeId()));
                initList = result.single().get("properties(p)").asMap();
            } else {
                travelNode(relationship.endNodeId());
                if (relationship.type().equals(NodeRelationship.IS_A.name())) {
                    parent = (ClassDef) mTypes.get(id);
                } else if (relationship.type().equals(NodeRelationship.BELONGS_TO.name())) {
                    namespaceDef = (NamespaceDef) mTypes.get(id);
                } else if (relationship.type().equals(NodeRelationship.HAS_A_BOOL.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_INT8.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_UINT8.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_INT16.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_UINT16.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_INT32.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_UINT32.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_INT64.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_UINT64.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_FLOAT32.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_FLOAT64.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_STRUCT.name())
                        || relationship.type().equals(NodeRelationship.HAS_A_ENUM.name())
                        || relationship.type().equals(NodeRelationship.HAS_SOME.name())
                        || relationship.type().equals(NodeRelationship.REFERENCES_TO.name())) {
                    FieldType fieldType = (FieldType) mTypes.get(id);
                    String name = relationship.get(Records.FieldRelationship.NAME).asString();
                    int counts = relationship.get(Records.FieldRelationship.COUNTS).asInt();
                    int fieldId = relationship.get(Records.FieldRelationship.FIELD_ID).asInt();
                    identifiers.add(new Identifier(fieldType, name, fieldId, counts));
                }
            }
        }

        //deal the node itself
        Node node = mSession.run(String.format("match (p) where id(p) = %d return p", id)).single().get("p").asNode();
        Type type;
        if (node.hasLabel(NodeLabel.CLASS.name())) {
            String name = node.get(Records.CompositeRecord.NAME).asString();
            int classId = node.get(Records.CompositeRecord.ID).asInt(-1);
            if (classId == -1) {
                type = new ClassDef(name, parent, namespaceDef, identifiers);
            } else {
                type = new ClassDef(name, parent, namespaceDef, identifiers, classId);
            }
            ((ClassDef) type).setInitList(initList);
        } else if (node.hasLabel(NodeLabel.STRUCT.name())) {
            String name = node.get(Records.CompositeRecord.NAME).asString();
            type = new StructDef(name, namespaceDef, identifiers);
        } else if (node.hasLabel(NodeLabel.ENUM.name())) {
            String name = node.get(Records.CompositeRecord.NAME).asString();
            type = new EnumDef(name, namespaceDef, identifiers);
        } else if (node.hasLabel(NodeLabel.NAMESPACE.name())) {
            String name = node.get(Records.NameSpaceRecord.NAME).asString();
            type = new NamespaceDef(name, namespaceDef);
            result = mSession.run(String.format("match (p)<-[:%s]-(q) where id(p) = %d" +
                    "return id(q)", NodeRelationship.BELONGS_TO, id));
            while (result.hasNext()) {
                travelNode(result.next().get("id(q)").asLong());
            }

        } else if (node.hasLabel(NodeLabel.BOOL.name())) {
            type = new BoolDef();
        } else if (node.hasLabel(NodeLabel.INT8.name())) {
            type = new Int8Def();
        } else if (node.hasLabel(NodeLabel.UINT8.name())) {
            type = new Uint8Def();
        } else if (node.hasLabel(NodeLabel.INT16.name())) {
            type = new Int16Def();
        } else if (node.hasLabel(NodeLabel.UINT16.name())) {
            type = new Uint16Def();
        } else if (node.hasLabel(NodeLabel.INT32.name())) {
            type = new Int32Def();
        } else if (node.hasLabel(NodeLabel.UINT32.name())) {
            type = new Uint32Def();
        } else if (node.hasLabel(NodeLabel.INT64.name())) {
            type = new Int64Def();
        } else if (node.hasLabel(NodeLabel.UINT64.name())) {
            type = new Uint64Def();
        } else if (node.hasLabel(NodeLabel.FLOAT32.name())) {
            type = new Float32Def();
        } else if (node.hasLabel(NodeLabel.FLOAT64.name())) {
            type = new Float64Def();
        } else if (node.hasLabel(NodeLabel.ARRAY.name())) {
            type = new ArrayDef();
        } else {
            throw new IllegalStateException(id + "'s label is unknown");
        }
        mTypes.put(id, type);
    }
}
