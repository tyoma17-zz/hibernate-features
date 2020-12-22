package com.tyoma17.hibernate.transaction.util;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j2
public final class DbUtils {

    //@formatter:off
    private static final String CREATE_MESSAGE_QUERY =
            "CREATE TABLE MESSAGE (" +
                    "ID BIGINT(20) NOT NULL AUTO_INCREMENT," +
                    "TEXT VARCHAR(255) NULL DEFAULT NULL," +
                    "PRIMARY KEY (ID));";
    //@formatter:on

    public static final String CONNECTION_URL = "jdbc:h2:mem:example;DB_CLOSE_DELAY=-1";

    private DbUtils() {
    }

    public static void createMessageTable() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE_MESSAGE_QUERY);
            log.debug("Table 'MESSAGE' was created");

        } catch (SQLException e) {
            log.error(e);
        }
    }

    // to avoid exceptions during tests
    public static void setDbCloseDelay(int seconds) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            statement.execute("SET DB_CLOSE_DELAY " + seconds);
            log.debug("Set DB_CLOSE_DELAY to {}", seconds);
        } catch (SQLException e) {
            log.error(e);
        }
    }

}
