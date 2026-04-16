package com.beemobi.rongthanonline.service;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
    private final static Logger logger = Logger.getLogger(MySQLConnect.class);

    public static String URL_DATABASE;

    public static String USERNAME_DATABASE;

    public static String PASSWORD_DATABASE;

    private static Connection connection;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            connection = DriverManager.getConnection(URL_DATABASE, USERNAME_DATABASE, PASSWORD_DATABASE);
        } catch (Exception e) {
            logger.error("Connect MySQL Fail", e);
        }
        return connection;
    }

    public static void closeConnection() {
        logger.debug("Close connection to database");
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Close connect MySQL Fail", e);
        }
    }
}
