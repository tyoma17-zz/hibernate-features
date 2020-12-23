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
        DbUtils.createMessageTable();
        DbUtils.saveMessage();
        Message message = DbUtils.getMessage();
        log.info("Saved message: {}", message);

        changeMessageTextWithinTransaction("Amended text 1", true);
        message = DbUtils.getMessage();
        log.info("Message after failure during transaction: {}", message);

        changeMessageTextWithinTransaction("Amended text 2", false);
        message = DbUtils.getMessage();
        log.info("Message after successful transaction: {}", message);
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
}