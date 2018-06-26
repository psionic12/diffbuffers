package com.diffbuffers.neo4jprocedures.test;

import com.diffbuffers.neo4jprocedures.DiffbuffersProcedures;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.junit.Neo4jRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestWithHarness {
    @ClassRule
    public static Neo4jRule sNeo4j = new Neo4jRule().withProcedure(DiffbuffersProcedures.class);

    @Test
    public void initTest() {
        try (Driver driver = GraphDatabase.driver(sNeo4j.boltURI(), Config.build().withoutEncryption().toConfig())) {

            Session session = driver.session();

            session.run("CREATE (p {name:\"junk\"})");

            Record record = session.run("CALL dbf.init()").single();

            assertFalse(record.get("message").asString(), record.get("isok").asBoolean());

            session.run("MATCH (p) DETACH DELETE p");

            record = session.run("CALL dbf.init()").single();

            assertTrue(record.get("message").asString(), record.get("isok").asBoolean());

            StatementResult results = session.run("MATCH (p:UINT8) RETURN p");

            assertTrue(results.keys().size() != 0);

        }
    }
}
