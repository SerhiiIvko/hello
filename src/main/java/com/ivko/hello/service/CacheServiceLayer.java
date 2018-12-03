package com.ivko.hello.service;

import com.ivko.hello.manager.DBManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheServiceLayer {

    public static Map<Long, String> createCacheFromDB() throws SQLException {
        DBManager dbManager = new DBManager();
        Connection connection = dbManager.getDBConnection();
        ResultSet resultSet = dbManager.createDBResultset(connection);
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

    public static boolean isCacheInitialized() throws SQLException {
        return !createCacheFromDB().isEmpty();
    }
}