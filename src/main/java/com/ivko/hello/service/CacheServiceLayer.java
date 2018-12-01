package com.ivko.hello.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheServiceLayer {

    public static Map<Long, String> createCacheFromDB(ResultSet resultSet) throws SQLException {
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
//
//    public static boolean isCacheInitialized() throws SQLException {
//        return false;
//    }
}