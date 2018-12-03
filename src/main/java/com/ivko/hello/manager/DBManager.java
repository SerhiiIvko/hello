package com.ivko.hello.manager;

import java.sql.*;
import java.util.logging.Logger;

public class DBManager {
    private static final Logger LOG = Logger.getLogger(String.valueOf(DBManager.class));
    private final String DB_URL = "jdbc:mysql://localhost:3306/hello?autoReconnect=true&useSSL=false";
    private final String DB_LOGIN = "root";
    private final String DB_PASSWORD = "root";
    private final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String SQL_QUERY = "SELECT id, name FROM contacts";

    private Connection connection;
    private ResultSet resultSet;

    public Connection getDBConnection() {
        if (connection == null) {
            try {
                Class.forName(DB_DRIVER).newInstance();
                connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
            } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                LOG.info(e.getMessage());
            }
        }
        return connection;
    }

    public ResultSet createDBResultset(Connection connection) {
        try {
            Statement statement;
            if (connection != null) {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(SQL_QUERY);
            }
        } catch (SQLException e) {
            LOG.info(e.getMessage());
        }
        return resultSet;
    }

    void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            LOG.info(e.getMessage());
        }
    }

    void closeStatement(Statement stmt) {
        try {
            if (null != stmt) {
                stmt.close();
            }
        } catch (SQLException e) {
            LOG.info(e.getMessage());
        }
    }
}