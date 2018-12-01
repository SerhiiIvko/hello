package com.ivko.hello.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public class DBManagerTest {
    private Connection connection;
    private DBManager dbManager;

    @Before
    public void before() throws SQLException {
        if (connection == null) {
            dbManager = new DBManager();
            connection = dbManager.getDBConnection();
        }
    }

    @After
    public void after() {
        dbManager.closeConnection(connection);
    }

    @Test
    public void closeStatementShouldCloseStatement() throws SQLException {
        Statement statement = connection.createStatement();
        dbManager.closeStatement(statement);
        assertTrue(statement.isClosed());
    }

    @Test
    public void closeStatementWithNullShouldNotThrow() {
        dbManager.closeStatement(null);
    }
}