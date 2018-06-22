package mytest;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.neo4j.driver.v1.*;

public class MyTest {
    public static Driver sDriver;
    @ClassRule
    public static TestRule sRule = new TestRule() {
        @Override
        public Statement apply(Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try (Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                            AuthTokens.basic("neo4j", "psionic12"))) {
                        sDriver = driver;
                        base.evaluate();
                        sDriver = null;
                    }
                }
            };
        }
    };

<<<<<<< HEAD
=======
    static class TestClass {

    }

    private enum TestClasses implements TestClassInterface {
        A, B
    }

    interface TestClassInterface {

    }

    private void test(TestClassInterface instance) {

    }

    @Test
    public void test2() {
        test(TestClasses.A);
    }

>>>>>>> 1d1b9081f02b553f339021cfb3294902895cd828


    @Test
    public void test1() {
        try(Session session = sDriver.session()) {
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
<<<<<<< HEAD
//                    StatementResult result = tx.run(
//                            "create (a:PERSON)" +
//                                    "set a.name = \"Tom Hanks\" "
//                    );
                    session.
=======
                    StatementResult result = tx.run(
                            "create (a:PERSON)" +
                                    "set a.name = \"Tom Hanks\" "
                    );
>>>>>>> 1d1b9081f02b553f339021cfb3294902895cd828
                    return null;
                }
            });
        }
    }
}
