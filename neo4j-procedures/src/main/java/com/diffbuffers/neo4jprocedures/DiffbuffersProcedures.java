package com.diffbuffers.neo4jprocedures;

import org.neo4j.cypher.InvalidArgumentException;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import static com.diffbuffers.neo4jprocedures.ClassesRelationships.BELONGS_TO;

public class DiffbuffersProcedures {

    @Context
    public GraphDatabaseService mService;

    @Context
    public Log mLog;

    private Node defaultNameSpace;
    private Node defaultSuperClass;

    @Procedure(name = "dbf.initSchema", mode = Mode.SCHEMA)
    public void initSchema() {
        try (Transaction tx = mService.beginTx()) {
            Schema schema = mService.schema();
            schema.indexFor(NodeTypes.CLASS).on(Tables.ClassTable.NAME).on(Tables.ClassTable.ID).create();
            schema.constraintFor(NodeTypes.CLASS).assertPropertyIsUnique(Tables.ClassTable.NAME).create();
            schema.constraintFor(NodeTypes.CLASS).assertPropertyIsUnique(Tables.ClassTable.ID).create();
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
                defaultNameSpace = mService.createNode(NodeTypes.NAMESPACE);
                defaultNameSpace.setProperty(Tables.NameSpaceTable.NAME, defaultNamespaceName);

                // super class for all class type
                defaultSuperClass = mService.createNode(NodeTypes.CLASS);
                defaultSuperClass.setProperty(Tables.ClassTable.NAME, defaultSuperClassName);
                defaultSuperClass.createRelationshipTo(defaultNameSpace, BELONGS_TO);

                tx.success();
            }
        }
    }

    @Procedure(name = "dbf.createClass", mode = Mode.WRITE)
    public void createClass(@Name("NAME") String className,
                            @Name(value = "parent", defaultValue = "") String superType,
                            @Name(value = "space", defaultValue = "") String nameSpace) {
        try (Transaction tx = mService.beginTx()) {
            Node node = mService.createNode(NodeTypes.CLASS);
            node.setProperty("NAME", className);
            if ("".equals(superType)) {
                node.createRelationshipTo(defaultNameSpace, BELONGS_TO);
            } else {
                Node targetNamespace = mService.findNode(NodeTypes.NAMESPACE, Tables.NameSpaceTable.NAME, nameSpace);
                if (targetNamespace == null) {
                    throw new InvalidArgumentException("nameSpace " + nameSpace + "does not exist", null);
                }
            }
            tx.success();
        }
    }
}
