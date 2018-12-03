package com.ivko.hello.manager;

import org.h2.tools.DeleteDbFiles;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbManagerTest {
    private Connection connection;

    @BeforeClass
    public static void init() throws SQLException {
        org.h2.tools.Server.createTcpServer().start();
        DbManager.initialize(DbManager.getH2DataSource());
    }

    @Before
    public void before() throws SQLException {
        DeleteDbFiles.execute("~", "test", false);
        if (connection == null) {
            connection = DbManager.getConnection();
        }
    }

    @After
    public void after() throws SQLException {
        connection.close();
    }
}