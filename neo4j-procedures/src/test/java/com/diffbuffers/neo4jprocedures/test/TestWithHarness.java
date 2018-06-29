package com.diffbuffers.neo4jprocedures.test;

import com.diffbuffers.neo4jprocedures.DiffbuffersProcedures;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.junit.Neo4jRule;

import static org.junit.Assert.assertTrue;
import static org.neo4j.driver.v1.Values.parameters;

public class TestWithHarness {
    @ClassRule
    public static Neo4jRule sNeo4j = new Neo4jRule().withProcedure(DiffbuffersProcedures.class);

    @Test
    public void initTest() {
        try (Driver driver = GraphDatabase.driver(sNeo4j.boltURI(), Config.build().withoutEncryption().toConfig())) {

            Session session = driver.session();

            try (Transaction tx = session.beginTransaction()) {

                tx.run("CALL dbf.initSchema()");
                tx.success();
            }


            try (Transaction tx = session.beginTransaction()) {
                tx.run("CALL dbf.init()");

                StatementResult results = tx.run("MATCH (p:UINT8) RETURN p");
                tx.success();

                assertTrue(results.keys().size() != 0);

            }
        }
    }

    @Test
    public void createClassTest() {
        try (Driver driver = GraphDatabase.driver(sNeo4j.boltURI(), Config.build().withoutEncryption().toConfig())) {
            Session session = driver.session();
            session.run("CALL dbf.createClass()", parameters());
        }
    }
}
