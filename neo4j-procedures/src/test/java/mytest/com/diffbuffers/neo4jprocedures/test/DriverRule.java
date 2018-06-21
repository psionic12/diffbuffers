package mytest.com.diffbuffers.neo4jprocedures.test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.neo4j.driver.v1.*;

import java.net.URI;

public class DriverRule implements TestRule {
    private Driver mDriver;
    private URI mUri;
    private Config mConfig;
    private AuthToken mAuthToken;
    
    public DriverRule( String uri )
    {
        this( uri, Config.defaultConfig() );
    }


    public DriverRule( URI uri )
    {
        this( uri, Config.defaultConfig() );
    }

    public DriverRule( URI uri, Config config )
    {
        this( uri, AuthTokens.none(), config );
    }


    public DriverRule( String uri, Config config )
    {
        this( URI.create( uri ), config );
    }


    public DriverRule( String uri, AuthToken authToken )
    {
        this( uri, authToken, Config.defaultConfig() );
    }


    public DriverRule( URI uri, AuthToken authToken )
    {
        this( uri, authToken, Config.defaultConfig() );
    }


    public DriverRule( String uri, AuthToken authToken, Config config )
    {
        this( URI.create( uri ), authToken, config );
    }


    public DriverRule( URI uri, AuthToken authToken, Config config )
    {
        mUri = uri;
        mAuthToken = authToken;
        mConfig = config;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try (Driver driver = GraphDatabase.driver(mUri, mAuthToken, mConfig)) {
                    mDriver = driver;
                    base.evaluate();
                }
            }
        };
    }

    public Driver getDriver() {
        return mDriver;
    }
}
