package com.tyoma17.hibernate.transaction.util;

import com.tyoma17.hibernate.transaction.domain.Message;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;

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
    public static final String CONNECTION_URL_TEST = "jdbc:h2:mem:example";

    private DbUtils() {
    }

    public static Message getMessage() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Message message = session.get(Message.class, 1L);
        session.close();
        return message;
    }

    public static void saveMessage() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();

        Message message = new Message("Hello, world!");
        session.save(message);

        session.getTransaction().commit();
        session.close();
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

    public static void dropAllDbObjects() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
