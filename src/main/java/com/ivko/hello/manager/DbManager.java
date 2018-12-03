package com.ivko.hello.manager;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DbManager {

    private static final Logger LOG = Logger.getLogger(String.valueOf(DbManager.class));

    private DbManager() {
    }

    private static DataSource DataSource;
    private static boolean isInitialize = false;

    public static void initialize(DataSource dataSource) {
        DataSource = dataSource;
        isInitialize = true;
    }

    public static Connection getConnection() throws SQLException {
        if (!isInitialize) {
            throw new IllegalStateException("DB is not initialized yet");
        }
        return DataSource.getConnection();
    }

    public static DataSource getMySQLDataSource() throws NullPointerException, SQLException {
        MysqlDataSource mysqlDS = null;
        try {
            Properties properties = loadProperties();
            mysqlDS = new MysqlDataSource();
            mysqlDS.setUrl(properties.getProperty("MYSQL_URL"));
            mysqlDS.setUser(properties.getProperty("MYSQL_USERNAME"));
            mysqlDS.setPassword(properties.getProperty("MYSQL_PASSWORD"));
            mysqlDS.setAutoReconnect(true);
            mysqlDS.setUseSSL(false);
            mysqlDS.setServerTimezone("UTC");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return mysqlDS;
    }

    public static DataSource getH2DataSource() {
        JdbcDataSource h2Sourse = null;
        try {
            Properties properties = loadProperties();
            h2Sourse = new JdbcDataSource();
            h2Sourse.setURL(properties.getProperty("H2_URL"));
            h2Sourse.setUser(properties.getProperty("H2_USERNAME"));
            h2Sourse.setPassword(properties.getProperty("H2_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return h2Sourse;
    }

    private static Properties loadProperties() throws IOException {
        ClassLoader classLoader = DbManager.class.getClassLoader();
        File propertyFile = new File(classLoader.getResource("db.properties").getFile());
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(propertyFile);
        properties.load(fis);
        return properties;
    }
}