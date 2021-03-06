package com.diffbuffers.neo4jprocedures.test;

import com.diffbuffers.neo4jprocedures.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
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

            try (Transaction tx = session.beginTransaction()) {
                int parent = tx.run("MATCH (p:DEFAULT_CLASS) RETURN ID(p) as id").single().get("id").asInt();
                Node node = tx.run("MATCH (p:DEFAULT_CLASS) RETURN p").single().get("p").asNode();
                for (String key : node.keys()) {
                    System.out.println(key + ":" + node.get(key));
                }
                int space = tx.run("MATCH (p:DEFAULT_NAMESPACE) RETURN ID(p) as id").single().get("id").asInt();
                Record record = tx.run("CALL dbf.createClass({name}, {parent}, {namespace})",
                        parameters("name", "TestClass",
                                "parent", parent, "namespace", space)).single();
                for (String key : record.asMap().keySet()) {
                    System.out.println(key + ":" + record.get(key));
                }

//                tx.run("CALL dbf.createClass({name}, {parent}, {namespace})",
//                        parameters("name", "TestClass",
//                                "parent", parent, "namespace", space));

//                tx.run("CALL dbf.deleteComposite({nodeId})", parameters("nodeId", record.get("nodeId")));
                tx.success();
            }
        }
    }
}
