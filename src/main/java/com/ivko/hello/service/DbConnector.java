package com.ivko.hello.service;

import com.ivko.hello.manager.DbManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DbConnector {
    private final String SQL_QUERY = "SELECT id, name FROM contacts";
    private static final Logger LOG = Logger.getLogger(String.valueOf(DbConnector.class));

    private Map<Long, String> contacts;

    public Map<Long, String> getContacts() {
        if (contacts == null) {
            try {
                synchronized (this) {
                    contacts = readContacts();
                }
            } catch (SQLException e) {
                LOG.warning(e.getMessage());
            }
        }
        return contacts;
    }

    private Map<Long, String> readContacts() throws SQLException {
        ResultSet resultSet = getResultSet();
        if (resultSet == null) {
            throw new SQLException("Database is not available!");
        } else {
            Map<Long, String> dbCache = new ConcurrentHashMap<>();
            int idColumn = resultSet.findColumn("id");
            int nameColumn = resultSet.findColumn("name");
            while (resultSet.next()) {
                long column1 = resultSet.getLong(idColumn);
                String column2 = resultSet.getString(nameColumn);
                dbCache.put(column1, column2);
            }
            return dbCache;
        }
    }

    private ResultSet getResultSet() throws SQLException {
        ResultSet resultSet = null;
        Connection connection = DbManager.getConnection();
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
}