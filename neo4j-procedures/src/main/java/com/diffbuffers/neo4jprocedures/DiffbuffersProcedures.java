package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;

import java.util.stream.Stream;

public class DiffbuffersProcedures {

    private static enum Relationships implements RelationshipType {
        IS_A,
        HAS_A,
        HAS_SOME,
        LINKS_TO,
        BELONGS_TO
    }

    private static enum NodeTypes implements Label {
        GROUP, STRUCT, NAMESPACE, STRING, ARRAY,
        BOOL, INT8, UINT8, INT16, UINT16, INT32, UINT32, INT64, UINT64, FLOAT32, FLOAT64
    }

    public static class GeneralResult {
        public boolean isok;
        public String message;

        public GeneralResult(boolean isok, String msg) {
            this.isok = isok;
            message = msg;
        }

        public GeneralResult(boolean isok) {
            this.isok = isok;
            message = null;
        }
    }


    @Context
    public GraphDatabaseService mService;

    @Context
    public Log mLog;

    @Procedure(name = "dbf.init", mode = Mode.WRITE)
    public Stream<GeneralResult> init() {
        try {
            mService.getNodeById(0);
            // if there's any node exits, that means this is not a empty graph
            return Stream.of(new GeneralResult(false, "init failed: the graph is not empty"));
        } catch (NotFoundException ne) {
            try (Transaction tx = mService.beginTx()) {
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
                return Stream.of(new GeneralResult(true, "success"));
            } catch (Exception e) {
                return Stream.of(new GeneralResult(false, e.getMessage()));
            }
        }
    }
}
