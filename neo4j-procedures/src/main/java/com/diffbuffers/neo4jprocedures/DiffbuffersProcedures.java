package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

public class DiffbuffersProcedures {

<<<<<<< HEAD
    private static enum Relationships implements RelationshipType {
        IS_A,
        HAS_A,
        HAS_SOME,
        LINKS_TO,
        BELONGS_TO
    }

    private static enum NodeTypes implements Label {
        ENTITIY, STRUCT, NAMESPACE, STRING, ARRAY,
        BOOL, INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, UINT64, FLOAT32, FLOAT64
=======
    private static enum BASIC_TYPES implements Label
    {
        CLASS,
        INT,
        FLOAT,
        UINT8
    }

    private static enum RelTypes implements RelationshipType
    {
        KNOWS
>>>>>>> 1d1b9081f02b553f339021cfb3294902895cd828
    }


    @Context
    public GraphDatabaseService mService;

    @Context
    public Log mLog;

    @Procedure (name = "dbf.init", mode = Mode.WRITE)
    public void init() {
        try(Transaction tx = mService.beginTx()) {
<<<<<<< HEAD
            try {
                mService.getNodeById(0);
                // if there's any node exits, that means this is not a empty graph
                mLog.error("init failed: the graph is not empty");
                tx.failure();
            } catch (NotFoundException e) {
                // create basic nodes
                Node nodeBOOL = mService.createNode(NodeTypes.BOOL);
                Node nodeINT8 = mService.createNode(NodeTypes.INT8);
                Node nodeUINT8 = mService.createNode(NodeTypes.UINT8);
                Node nodeINT16 = mService.createNode(NodeTypes.INT16);
                Node nodeUINT16 = mService.createNode(NodeTypes.UINT16);
                Node nodeINT32 = mService.createNode(NodeTypes.INT32);
                Node nodeUINT32 = mService.createNode(NodeTypes.UINT32);
                Node nodeINT64 = mService.createNode(NodeTypes.INT64);
                Node nodeUINT64 = mService.createNode(NodeTypes.UINT64);
                Node nodeFLOAT32 = mService.createNode(NodeTypes.FLOAT32);
                Node nodeFLOAT64 = mService.createNode(NodeTypes.FLOAT64);

                tx.success();
            }

=======
            Node classNode = mService.createNode();
            classNode.addLabel(BASIC_TYPES.CLASS);
            Node node = mService.createNode();
            classNode.createRelationshipTo(node, RelTypes.KNOWS);
>>>>>>> 1d1b9081f02b553f339021cfb3294902895cd828
        }
    }
}
