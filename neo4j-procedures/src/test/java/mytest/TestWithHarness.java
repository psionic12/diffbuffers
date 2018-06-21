package mytest;

import com.diffbuffers.neo4jprocedures.DiffbuffersProcedures;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.harness.junit.Neo4jRule;

public class TestWithHarness {
    public static Driver sDriver;
    @ClassRule
    public static Neo4jRule sNeo4j = new Neo4jRule().withProcedure(DiffbuffersProcedures.class);
    public static TestRule sRule = new TestRule() {
        @Override
        public Statement apply(Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try (Driver driver = GraphDatabase.driver(sNeo4j.boltURI())) {
                        sDriver = driver;
                        base.evaluate();
                        sDriver = null;
                    }
                }
            };
        }
    };

}
