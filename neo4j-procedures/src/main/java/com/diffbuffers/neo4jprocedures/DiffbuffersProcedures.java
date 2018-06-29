package com.diffbuffers.neo4jprocedures;

import org.neo4j.cypher.InvalidArgumentException;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.ArrayList;
import java.util.stream.Stream;

import static com.diffbuffers.neo4jprocedures.ClassesRelationships.BELONGS_TO;
import static com.diffbuffers.neo4jprocedures.ClassesRelationships.IS_A;
import static com.diffbuffers.neo4jprocedures.NodeTypes.NON_ABSTRACT;

public class DiffbuffersProcedures {

    @Context
    public GraphDatabaseService mService;

    @Context
    public Log mLog;

    @Procedure(name = "dbf.initSchema", mode = Mode.SCHEMA)
    public void initSchema() {
        try (Transaction tx = mService.beginTx()) {
            Schema schema = mService.schema();
            schema.constraintFor(NON_ABSTRACT).assertPropertyIsUnique(Records.ClassRecord.ID).create();
            tx.success();
            mLog.info("schema init finished");
        }
    }

    @Procedure(name = "dbf.init", mode = Mode.WRITE)
    public void init(@Name(value = "defaultNamespaceName", defaultValue = "diffbuffers") String defaultNamespaceName,
                     @Name(value = "defaultSuperClassName", defaultValue = "Entity") String defaultSuperClassName) {
        try {
            mService.getNodeById(0);
            // if there's any node exits, that means this is not a empty graph
            mLog.error("init failed: the graph is not empty");
        } catch (NotFoundException ne) {
            try (Transaction tx = mService.beginTx()) {

                // create basic nodes
                Node basicType = mService.createNode(NodeTypes.BASIC_TYPE);

                // default NAME space;
                Node defaultNameSpace = mService.createNode(NodeTypes.NAMESPACE);
                defaultNameSpace.setProperty(Records.NameSpaceRecord.NAME, defaultNamespaceName);
                defaultNameSpace.addLabel(NodeTypes.DEFAULT_NAMESPACE);

                // super class for all class type
                Node defaultSuperClass = mService.createNode(NodeTypes.CLASS);
                defaultSuperClass.setProperty(Records.ClassRecord.NAME, defaultSuperClassName);
                defaultSuperClass.createRelationshipTo(defaultNameSpace, BELONGS_TO);
                defaultSuperClass.addLabel(NodeTypes.DEFAULT_CLASS);

                tx.success();
            }
        }
    }

    @Procedure(name = "dbf.createClass", mode = Mode.WRITE)
    public Stream<Records.ClassRecord> createClass(@Name("name") String className,
                                                   @Name("parent") Long superTypeId,
                                                   @Name("space") Long nameSpaceId,
                                                   @Name(value = "abstract", defaultValue = "false") boolean isAbstract) {
        Records.ClassRecord classRecord = new Records.ClassRecord();
        try (Transaction tx = mService.beginTx()) {

            Node node = mService.createNode(NodeTypes.CLASS);
            classRecord.nodeId = node.getId();
            node.setProperty(Records.ClassRecord.NAME, className);
            classRecord.name = className;
            node.createRelationshipTo(mService.getNodeById(superTypeId), IS_A);
            node.createRelationshipTo(mService.getNodeById(nameSpaceId), BELONGS_TO);
            if (!isAbstract) {
                ArrayList<Integer> list = new ArrayList<>();
                try (ResourceIterator<Node> nodes = mService.findNodes(NON_ABSTRACT)) {
                    while (nodes.hasNext()) {
                        list.add((int) nodes.next().getProperty(Records.ClassRecord.ID));
                    }
                }
                Long id = (long)getMinMissingInt(list);
                node.setProperty(Records.ClassRecord.ID, id);
                classRecord.id = id;
                node.addLabel(NON_ABSTRACT);
            }
            tx.success();
        }
        return Stream.of(classRecord);
    }

    private boolean isUniqeInSpace(String name, Node space) {
        mService.traversalDescription().breadthFirst()
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
}
