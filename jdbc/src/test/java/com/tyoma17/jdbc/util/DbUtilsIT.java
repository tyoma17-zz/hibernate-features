package com.tyoma17.jdbc.util;

import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DbUtilsIT {

    private static final String SELECT_PUBLISHER_NAME =
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_NAME = 'PUBLISHER'";

    private static final String SELECT_CHAPTER_NAME =
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_NAME = 'CHAPTER'";

    private static final String SELECT_BOOK_NAME =
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_NAME = 'BOOK'";

    @Test
    void createBookStoreTables() {

        DbUtils.createBookStoreTables();
        try (Connection connection = DriverManager.getConnection(DbUtils.CONNECTION_URL)) {

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SELECT_PUBLISHER_NAME);
            String actualTableName = nextValue(resultSet);
            assertEquals("PUBLISHER", actualTableName);

            resultSet = statement.executeQuery(SELECT_BOOK_NAME);
            actualTableName = nextValue(resultSet);
            assertEquals("BOOK", actualTableName);

            resultSet = statement.executeQuery(SELECT_CHAPTER_NAME);
            actualTableName = nextValue(resultSet);
            assertEquals("CHAPTER", actualTableName);

            DbUtils.setDbCloseDelay(0);

        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    private String nextValue(ResultSet resultSet) throws SQLException {
        String nextValue = null;

        if (resultSet.next()) {
            nextValue = resultSet.getString("TABLE_NAME");
        } else {
            fail();
        }

        return nextValue;
    }
}