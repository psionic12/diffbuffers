package com.diffbuffers.neo4jprocedures;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

public class DiffbuffersProcedures {

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
    }


    @Context
    public GraphDatabaseService mService;

    @Context
    public Log mLog;

    @Procedure (name = "dbf.init", mode = Mode.WRITE)
    public void init() {
        try(Transaction tx = mService.beginTx()) {
            Node classNode = mService.createNode();
            classNode.addLabel(BASIC_TYPES.CLASS);
            Node node = mService.createNode();
            classNode.createRelationshipTo(node, RelTypes.KNOWS);
        }
    }
}
