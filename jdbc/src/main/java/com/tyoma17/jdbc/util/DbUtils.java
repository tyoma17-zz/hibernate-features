package com.tyoma17.jdbc.util;

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
                    "CODE VARCHAR(4) NOT NULL," +
                    "PUBLISHER_NAME VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (CODE)" +
                    ")";

    private static final String CREATE_BOOK_QUERY =
            "CREATE TABLE BOOK (" +
                    "ISBN VARCHAR(50) NOT NULL," +
                    "BOOK_NAME VARCHAR(100) NOT NULL," +
                    "PUBLISHER_CODE VARCHAR(4)," +
                    "PRIMARY KEY (ISBN)," +
                    "FOREIGN KEY (PUBLISHER_CODE) REFERENCES PUBLISHER (CODE)" +
                    ");";

    private static final String CREATE_CHAPTER_QUERY =
            "CREATE TABLE CHAPTER (" +
                    "BOOK_ISBN VARCHAR(50) NOT NULL," +
                    "CHAPTER_NUM INT NOT NULL," +
                    "TITLE VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (BOOK_ISBN, CHAPTER_NUM)," +
                    "FOREIGN KEY (BOOK_ISBN) REFERENCES BOOK (ISBN)" +
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
