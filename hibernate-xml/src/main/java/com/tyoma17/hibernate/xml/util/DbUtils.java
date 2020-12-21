package com.tyoma17.hibernate.xml.util;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j2
public final class DbUtils {

    //@formatter:off
    private static final String CREATE_PUBLISHER_QUERY =
            "CREATE TABLE PUBLISHER (" +
                    "ID BIGINT(20) NOT NULL AUTO_INCREMENT, " +
                    "CODE VARCHAR(4) NOT NULL," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (ID)" +
                    ")";

    private static final String CREATE_BOOK_QUERY =
            "CREATE TABLE BOOK (" +
                    "ID BIGINT(20) NOT NULL AUTO_INCREMENT, " +
                    "ISBN VARCHAR(50) NOT NULL," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "PUBLISHER_ID BIGINT(20)," +
                    "PRIMARY KEY (ID)," +
                    "FOREIGN KEY (PUBLISHER_ID) REFERENCES PUBLISHER (ID)" +
                    ");";

    private static final String CREATE_CHAPTER_QUERY =
            "CREATE TABLE CHAPTER (" +
                    "ID BIGINT(20) NOT NULL AUTO_INCREMENT, " +
                    "BOOK_ID VARCHAR(50)," +
                    "CHAPTER_NUMBER INT NOT NULL," +
                    "TITLE VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (ID)," +
                    "FOREIGN KEY (BOOK_ID) REFERENCES BOOK (ID)" +
                    ")";
    //@formatter:on

    public static final String CONNECTION_URL = "jdbc:h2:mem:example;DB_CLOSE_DELAY=-1";

    private DbUtils() {
    }

    public static void createBookStoreTables() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE_PUBLISHER_QUERY);
            log.debug("Table 'PUBLISHER' was created");
            statement.executeUpdate(CREATE_BOOK_QUERY);
            log.debug("Table 'BOOK' was created");
            statement.executeUpdate(CREATE_CHAPTER_QUERY);
            log.debug("Table 'CHAPTER' was created");

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
