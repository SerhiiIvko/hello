package com.ivko.hello.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class DBManagerTest {
    private final static Logger LOG = Logger.getLogger(String.valueOf(DBManager.class));
    private Connection connection;
    private DBManager dbManager;

    @Before
    public void before() {
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
    public void closeStatementWithNullShouldNotThrow() {
        dbManager.closeStatement(null);
    }
}