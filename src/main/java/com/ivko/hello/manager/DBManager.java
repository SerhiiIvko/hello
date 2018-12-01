package com.ivko.hello.manager;

import java.sql.*;

class DBManager {
    private final String DB_URL = "jdbc:mysql://localhost:3306/hello";
    private final String DB_LOGIN = "root";
    private final String DB_PASSWORD = "root";
    private final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String SQL_QUERY = "SELECT id, name FROM contacts";

    private Connection connection;
    private ResultSet resultSet;

    Connection getDBConnection() {
        if (connection == null) {
            try {
                Class.forName(DB_DRIVER).newInstance();
                connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
            } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.getMessage();
            }
        }
        return connection;
    }

    ResultSet createDBStatement(Connection connection) throws SQLException {
        Statement statement;
        if (connection != null) {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_QUERY);
        }
        return resultSet;
    }

    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void closeStatement(Statement stmt) {
        try {
            if (null != stmt) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}