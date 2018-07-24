package com.diffbuffers.neo4jprocedures.test;

import com.diffbuffers.common.NodeRelationship;
import org.junit.Test;
import org.neo4j.driver.v1.*;

public class Client {

    @Test
    public void test() {
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456"))) {
            Session session = driver.session();
            StatementResult result = session.run("match(p:Person) return labels(p) as labels, ID(p) AS id");
            while (result.hasNext()) {
                Record record = result.next();
                for (String key : record.keys()) {
                    System.out.println(key + "/" + record.get(key));
                }


            }
        }
    }

    private void buildFrom(Session session, Long spaceNodeId) {
        String statement = String.format("MATCH (p)-[:%s*]->(s) WHERE ID(s) = %d " +
                        "RETURN ID(p) AS id, labels(p) AS labels, properties(p) AS properties",
                NodeRelationship.BELONGS_TO, spaceNodeId);
        StatementResult result = session.run(statement);
        while (result.hasNext()) {
            Record record = result.next();
            long id = record.get("id").asLong();
        }
    }

}
