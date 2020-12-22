package com.tyoma17.hibernate.transaction.client;

import com.tyoma17.hibernate.transaction.domain.Message;
import com.tyoma17.hibernate.transaction.util.DbUtils;
import com.tyoma17.hibernate.transaction.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class HelloWorldClient {

    public static void main(String[] args) {
        saveMessage();
        Message message = getMessage();
        log.info("Saved message: {}", message);

        changeMessageTextWithinTransaction("Amended text 1", true);
        message = getMessage();
        log.info("Message after failure during transaction: {}", message);

        changeMessageTextWithinTransaction("Amended text 2", false);
        message = getMessage();
        log.info("Message after successful transaction: {}", message);
    }

    static Message getMessage() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Message message = session.get(Message.class, 1L);
        session.close();
        return message;
    }

    static void changeMessageTextWithinTransaction(String newText, boolean throwException) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();

        try {

            transaction.begin();
            Message message = session.get(Message.class, 1L);
            message.setText(newText);

            if (throwException) {
                throw new Exception("Intentional exception");
            }

            transaction.commit();

        } catch (Exception e) {
            log.error(e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    static void saveMessage() {
        DbUtils.createMessageTable();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Message message = new Message("Hello, world!");
        session.save(message);
        session.close();
    }
}