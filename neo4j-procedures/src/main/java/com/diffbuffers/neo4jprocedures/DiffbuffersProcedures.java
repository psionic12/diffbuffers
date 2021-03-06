package com.diffbuffers.neo4jprocedures;

import com.diffbuffers.common.Constant;
import com.diffbuffers.common.NodeLabel;
import com.diffbuffers.common.NodeRelationship;
import com.diffbuffers.common.Records;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

public class DiffbuffersProcedures {

    @Context
    public GraphDatabaseService mService;

    @Context
    public Log mLog;

    @Procedure(name = "dbf.initSchema", mode = Mode.SCHEMA)
    public void initSchema() {
        try (Transaction tx = mService.beginTx()) {
            Schema schema = mService.schema();
            schema.constraintFor(NodeLabel.NON_ABSTRACT).assertPropertyIsUnique(Records.CompositeRecord.ID).create();
            tx.success();
            mLog.info("schema init finished");
        }
    }

    @Procedure(name = "dbf.init", mode = Mode.WRITE)
    public void init() {
        try {
            mService.getNodeById(0);
            // if there's any node exits, that means this is not a empty graph
            mLog.error("init failed: the graph is not empty");
        } catch (NotFoundException ne) {
            try (Transaction tx = mService.beginTx()) {

                // create basic nodes
                mService.createNode(NodeLabel.ARRAY);
                mService.createNode(NodeLabel.BOOL);
                mService.createNode(NodeLabel.INT8);
                mService.createNode(NodeLabel.UINT8);
                mService.createNode(NodeLabel.INT16);
                mService.createNode(NodeLabel.UINT16);
                mService.createNode(NodeLabel.INT32);
                mService.createNode(NodeLabel.UINT32);
                mService.createNode(NodeLabel.INT64);
                mService.createNode(NodeLabel.UINT64);
                mService.createNode(NodeLabel.FLOAT32);
                mService.createNode(NodeLabel.FLOAT64);

                // default NAME space;
                Node defaultNameSpace = mService.createNode(NodeLabel.NAMESPACE);
                defaultNameSpace.setProperty(Records.NameSpaceRecord.NAME, Constant.DEFAULT_NAMESPACE);
                defaultNameSpace.addLabel(NodeLabel.DEFAULT_NAMESPACE);

                // super class for all class type
                Node defaultSuperClass = mService.createNode(NodeLabel.CLASS);
                defaultSuperClass.setProperty(Records.CompositeRecord.NAME, Constant.DEFAULT_CLASS_NAME);
                defaultSuperClass.createRelationshipTo(defaultNameSpace, NodeRelationship.BELONGS_TO);
                defaultSuperClass.addLabel(NodeLabel.DEFAULT_CLASS);
                defaultSuperClass.createRelationshipTo(mService.createNode(NodeLabel.INIT_LIST),
                        NodeRelationship.HAS_CONSTRUCTOR_ARGS);

                tx.success();
            }
        }
    }

    @Procedure(name = "dbf.createNamespace", mode = Mode.WRITE)
    public Stream<Records.NameSpaceRecord> createNamespace(@Name("name") String name,
                                                           @Name("parent") Long parentId) {
        Node parent = mService.getNodeById(parentId);
        if (!isUniqueInSpace(name, parent)) {
            throw new IllegalArgumentException(name + " has already defined in "
                    + parent.getProperty(Records.NameSpaceRecord.NAME));
        }

        Records.NameSpaceRecord record = new Records.NameSpaceRecord();
        try (Transaction tx = mService.beginTx()) {
            Node space = mService.createNode(NodeLabel.NAMESPACE);
            record.name = name;
            record.nodeId = space.getId();
            space.createRelationshipTo(parent, NodeRelationship.BELONGS_TO);
            tx.success();
        }
        return Stream.of(record);
    }

    @Procedure(name = "dbf.deleteNamespace", mode = Mode.WRITE)
    public void deleteNamespace(@Name("nodeId") Long id) {
        Node space = mService.getNodeById(id);
        if (space.hasRelationship(Direction.INCOMING, NodeRelationship.BELONGS_TO)) {
            throw new IllegalArgumentException("there are references to "
                    + space.getProperty(Records.NameSpaceRecord.NAME)
                    + " cannot delete");
        }
        try (Transaction tx = mService.beginTx()) {
            for (Relationship relationship : space.getRelationships(Direction.OUTGOING)) {
                relationship.delete();
            }
            space.delete();
            tx.success();
        }
    }

    @Procedure(name = "dbf.createEnum", mode = Mode.WRITE)
    public Stream<Records.CompositeRecord> createEnum(@Name("name") String enumName,
                                                      @Name("space") Long namespaceId) {
        Node space = mService.getNodeById(namespaceId);
        if (!isUniqueInSpace(enumName, space)) {
            throw new IllegalArgumentException(enumName + " has already defined in name space: "
                    + space.getProperty(Records.NameSpaceRecord.NAME));
        }
        Records.CompositeRecord record = new Records.CompositeRecord();
        try (Transaction tx = mService.beginTx()) {
            Node node = mService.createNode(NodeLabel.ENUM);
            record.nodeId = node.getId();
            node.setProperty(Records.CompositeRecord.NAME, enumName);
            record.name = enumName;
            node.createRelationshipTo(space, NodeRelationship.BELONGS_TO);
            tx.success();
        }
        return Stream.of(record);
    }
    public Stream<Records.CompositeRecord> createStruct(@Name("name") String structName,
                                                     @Name("space") Long namespaceId) {
        Node space = mService.getNodeById(namespaceId);
        if (!isUniqueInSpace(structName, space)) {
            throw new IllegalArgumentException(structName + " has already defined in name space: "
                    + space.getProperty(Records.NameSpaceRecord.NAME));
        }
        Records.CompositeRecord record = new Records.CompositeRecord();
        try (Transaction tx = mService.beginTx()) {
            Node node = mService.createNode(NodeLabel.STRUCT);
            record.nodeId = node.getId();
            node.setProperty(Records.CompositeRecord.NAME, structName);
            record.name = structName;
            node.createRelationshipTo(space, NodeRelationship.BELONGS_TO);
            tx.success();
        }
        return Stream.of(record);
    }

    @Procedure(name = "dbf.createClass", mode = Mode.WRITE)
    public Stream<Records.CompositeRecord> createClass(@Name("name") String className,
                                                       @Name("parent") Long superTypeId,
                                                       @Name("space") Long namespaceId,
                                                       @Name(value = "abstract", defaultValue = "false") boolean isAbstract) {
        Records.CompositeRecord classRecord = new Records.CompositeRecord();
        try (Transaction tx = mService.beginTx()) {
            Node space = mService.getNodeById(namespaceId);
            Node superType = mService.getNodeById(superTypeId);
            if (!isUniqueInSpace(className, space)) {
                throw new IllegalArgumentException(className + " has already defined in name space: "
                        + space.getProperty(Records.NameSpaceRecord.NAME));
            }
            Node node = mService.createNode(NodeLabel.CLASS);
            classRecord.nodeId = node.getId();
            node.setProperty(Records.CompositeRecord.NAME, className);
            classRecord.name = className;
            node.createRelationshipTo(superType, NodeRelationship.IS_A);
            node.createRelationshipTo(space, NodeRelationship.BELONGS_TO);
            node.createRelationshipTo(mService.createNode(NodeLabel.INIT_LIST), NodeRelationship.HAS_CONSTRUCTOR_ARGS);
            if (!isAbstract) {
                ArrayList<Integer> list = new ArrayList<>();
                try (ResourceIterator<Node> nodes = mService.findNodes(NodeLabel.NON_ABSTRACT)) {
                    while (nodes.hasNext()) {
                        long id = (Long) nodes.next().getProperty(Records.CompositeRecord.ID);
                        list.add((int) id);
                    }
                }
                Long id = (long) getMinMissingInt(list);
                node.setProperty(Records.CompositeRecord.ID, id);
                classRecord.id = id;
                node.addLabel(NodeLabel.NON_ABSTRACT);
            }
            tx.success();
        }
        return Stream.of(classRecord);
    }

    @Procedure(name = "dbf.deleteComposite", mode = Mode.WRITE)
    public void deleteComposite(@Name("nodeId") Long nodeId) {
        try (Transaction tx = mService.beginTx()) {
            Node node = mService.getNodeById(nodeId);
            if (!node.hasLabel(NodeLabel.CLASS) && !node.hasLabel(NodeLabel.STRUCT) && !node.hasLabel(NodeLabel.ENUM)) {
                throw new IllegalArgumentException("Only class type or struct or enum can be deleted");
            }
            if (node.hasRelationship(Direction.INCOMING)) {
                throw new IllegalArgumentException("There are references to this composite exist");
            }
            Node initList = getInitList(node);
            for (Relationship relationship : node.getRelationships(Direction.OUTGOING)) {
                relationship.delete();
            }
            node.delete();
            initList.delete();
            tx.success();
        }
    }

    @Procedure(name = "dbf.addField", mode = Mode.WRITE)
    public void addField(@Name("nodeId") Long nodeId,
                         @Name("type") Long type,
                         @Name("name") String name,
                         @Name(value = "counts", defaultValue = "1") Long counts,
                         @Name(value = "defaultValue", defaultValue = "") String defaultValue) {
        Node fieldNode = mService.getNodeById(type);
        NodeRelationship relationType = null;



        Node node = mService.getNodeById(nodeId);
        if (!isUniqueInComposite(name, node))
            throw new IllegalArgumentException(name + "has already defined in this class");

        HashSet<Integer> fieldSet = new HashSet<>();
        if (node.hasLabel(NodeLabel.STRUCT)) {
            for (NodeLabel label : NodeRelationship.sStructAllowedType.keySet()) {
                if (fieldNode.hasLabel(label)) {
                    relationType = NodeRelationship.sStructAllowedType.get(label);
                }
            }

            if (counts != 0) {
                throw new IllegalArgumentException("array is not allowed in struct");
            }
            for (Relationship relationship
                    : node.getRelationships(Direction.OUTGOING, NodeRelationship.getRelationships(NodeRelationship.sStructAllowedType))) {
                int id = (int) relationship.getProperty(Records.FieldRelationship.FIELD_ID);
                fieldSet.add(id);
            }
        } else if (node.hasLabel(NodeLabel.CLASS)) {
            for (NodeLabel label : NodeRelationship.sClassAllowedType.keySet()) {
                if (fieldNode.hasLabel(label)) {
                    relationType = NodeRelationship.sClassAllowedType.get(label);
                }
            }

            // get the subtree of the node and calculate the field id
            Traverser traverserUp = mService.traversalDescription().depthFirst()
                    .relationships(NodeRelationship.IS_A, Direction.OUTGOING)
                    .evaluator(Evaluators.all()).traverse(node);
            for (Path path : traverserUp) {
                for (Relationship relationship : path.endNode()
                        .getRelationships(Direction.OUTGOING, NodeRelationship.getRelationships(NodeRelationship.sClassAllowedType))) {
                    int id = (int) relationship.getProperty(Records.FieldRelationship.FIELD_ID);
                    fieldSet.add(id);
                }
            }
            Traverser traverserDown = mService.traversalDescription().depthFirst()
                    .relationships(NodeRelationship.IS_A, Direction.INCOMING)
                    .evaluator(Evaluators.excludeStartPosition()).traverse(node);
            for (Path path : traverserDown) {
                for (Relationship relationship : path.endNode()
                        .getRelationships(Direction.OUTGOING, NodeRelationship.getRelationships(NodeRelationship.sClassAllowedType))) {
                    int id = (int) relationship.getProperty(Records.FieldRelationship.FIELD_ID);
                    fieldSet.add(id);
                }
            }
        } else if (node.hasLabel(NodeLabel.ENUM)) {
            for (NodeLabel label : NodeRelationship.sEnumAllowedType.keySet()) {
                if (fieldNode.hasLabel(label)) {
                    relationType = NodeRelationship.sEnumAllowedType.get(label);
                }
            }
            for (Relationship relationship
                    : node.getRelationships(Direction.OUTGOING, NodeRelationship.getRelationships(NodeRelationship.sEnumAllowedType))) {
                int id = (int) relationship.getProperty(Records.FieldRelationship.FIELD_ID);
                fieldSet.add(id);
            }
        } else {
            throw new IllegalArgumentException("node id: " + nodeId + "is not a class or struct");
        }

        if (relationType == null)
            throw new IllegalArgumentException("invalid type " + fieldNode.getProperty(Records.FieldRelationship.NAME));

        int id = getMinMissingInt(new ArrayList<>(fieldSet));
        try (Transaction tx = mService.beginTx()) {
            Relationship relationship = node.createRelationshipTo(fieldNode, relationType);
            relationship.setProperty(Records.FieldRelationship.NAME, name);
            relationship.setProperty(Records.FieldRelationship.FIELD_ID, id);
            relationship.setProperty(Records.FieldRelationship.COUNTS, counts);
            if (node.hasLabel(NodeLabel.CLASS)) {
                getInitList(node).setProperty("" + relationship.getId(), defaultValue);
            }
            tx.success();
        }
    }

    @Procedure(name = "dbf.removeField", mode = Mode.WRITE)
    public void removeField(@Name("RelationshipId") Long id) {
        Relationship relationship = mService.getRelationshipById(id);
        Node node = relationship.getStartNode();
        if (node.hasLabel(NodeLabel.STRUCT) || node.hasLabel(NodeLabel.ENUM)) {
            try (Transaction tx = mService.beginTx()) {
                relationship.delete();
                tx.success();
            }
        } else if (node.hasLabel(NodeLabel.CLASS)) {
            // remove a field from a class is a little complicated. we should remove the all default values as well.
            Long fieldId = (Long) relationship.getProperty(Records.FieldRelationship.FIELD_ID);
            Traverser traverser = mService.traversalDescription().depthFirst()
                    .relationships(NodeRelationship.IS_A, Direction.INCOMING)
                    .evaluator(Evaluators.all()).traverse(node);
            try (Transaction tx = mService.beginTx()) {
                for (Path path : traverser) {
                    getInitList(path.endNode()).removeProperty(fieldId + "");
                }
                relationship.delete();
                tx.success();
            }

        } else throw new IllegalStateException("the start node of the relationship is not a class or struct or Enum, weird.");
    }

    @Procedure(name = "dbf.updateField", mode = Mode.WRITE)
    public void updateField(@Name("relationshipId") Long id,
                            @Name("defaultValue") String defaultValue) {
        Relationship relationship = mService.getRelationshipById(id);
        Node classNode = relationship.getStartNode();
        if (!classNode.hasLabel(NodeLabel.CLASS)) {
            throw new IllegalArgumentException("only class can update field default value");
        }
        try (Transaction tx = mService.beginTx()) {
            getInitList(classNode).setProperty(id + "", defaultValue);
            tx.success();
        }
    }



    private boolean isUniqueInSpace(String name, Node space) {
        for (Relationship relationship : space.getRelationships(Direction.INCOMING, NodeRelationship.BELONGS_TO)) {
            Node node = relationship.getStartNode();
            if (node.hasLabel(NodeLabel.CLASS) && name.equals(node.getProperty(Records.CompositeRecord.NAME))
                    || (node.hasLabel(NodeLabel.STRUCT) && name.equals(node.getProperty(Records.CompositeRecord.NAME)))) {
                return false;
            }
        }
        return true;
    }

    private boolean isUniqueInComposite(String name, Node composite) {
        for (Relationship relationship : composite.getRelationships(Direction.OUTGOING,
                NodeRelationship.getRelationships(NodeRelationship.sClassAllowedType))) {
            if (name.equals(relationship.getProperty(Records.FieldRelationship.NAME))) {
                return false;
            }
        }
        return true;
    }

    public static int getMinMissingInt(ArrayList<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            int t = list.get(i);
            while (t < list.size() - 1 && t != list.get(t)) {
                int tmp = list.get(t);
                list.set(t, t);
                t = tmp;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != i) {
                return i;
            }
        }
        return list.size();
    }

    private Node getInitList(Node node) {
        return node.getSingleRelationship(NodeRelationship.HAS_CONSTRUCTOR_ARGS, Direction.OUTGOING).getEndNode();
    }
}
