package com.tyoma17.hibernate.transaction.client;

import com.tyoma17.hibernate.transaction.domain.Message;
import com.tyoma17.hibernate.transaction.util.DbUtils;
import com.tyoma17.hibernate.transaction.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;

@Log4j2
public class EntityUpdateClient {

    public static void main(String[] args) {

        DbUtils.createMessageTable();

        Session session = openSessionAndBeginTransaction();
        Message message = new Message("Hello, world!");
//        session.save(message); <-- will insert DB record immediately
        session.persist(message); // <-- here or after transaction.commit()
        commitTransactionAndCloseSession(session);

        log.info("Trying to update message within closed session...");

        try {
            updateMessage(session, message, "Amended text");
        } catch (IllegalStateException e) {
            log.error("Cannot update message - session is closed!", e);
        }

        session = openSessionAndBeginTransaction();
        message = session.get(Message.class, 1L);
        log.info("Message text in DB still is: '{}'", message.getText());
        log.info("Trying to update message within open session...");
        updateMessage(session, message, "Amended text");
        commitTransactionAndCloseSession(session);

        message = DbUtils.getMessage();
        log.info("Message text in DB is: '{}'", message.getText());

        session = openSessionAndBeginTransaction();
        // another instance for the same message
        Message message2 = session.get(Message.class, 1L);

        log.info("Trying to update message...");

        try {
            updateMessage(session, message, "Amended text 2");// updating old instance
        } catch (NonUniqueObjectException e) {
            log.error("Cannot update 'message' - session context already has a different object" +
                    " with the same ID - 'message2'", e);
        }

        log.info("Trying to merge message...");
        mergeMessage(session, message, "Amended text 3");
        commitTransactionAndCloseSession(session);

        message = DbUtils.getMessage();
        log.info("Message text in DB is: '{}'", message.getText());
    }

    static void updateMessage(Session session, Message message, String newText) {
        message.setText(newText);
        session.update(message);
    }

    static void mergeMessage(Session session, Message message, String newText) {
        message.setText(newText);
        session.merge(message);
    }

    static Session openSessionAndBeginTransaction() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        return session;
    }

    static void commitTransactionAndCloseSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }
}