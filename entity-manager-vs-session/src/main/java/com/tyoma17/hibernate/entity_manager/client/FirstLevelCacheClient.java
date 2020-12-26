package com.tyoma17.hibernate.entity_manager.client;

import com.tyoma17.hibernate.entity_manager.entity.Message;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@Log4j2
public class FirstLevelCacheClient {

    static final EntityManagerFactory ENTITY_MANAGER_FACTORY = getEntityManagerFactory();

    public static void main(String[] args) {

        saveMessage();

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Message message = em.find(Message.class, 1L);

//        select
//        message0_.ID as id1_0_0_,
//                message0_.text as text2_0_0_
//        from
//        Message message0_
//        where
//        message0_.ID=?

        Message message2 = em.find(Message.class, 1L);
        // no SQL query fired!
        em.close();

        EntityManager em2 = ENTITY_MANAGER_FACTORY.createEntityManager();
        Message message3 = em2.find(Message.class, 1L);

//        select
//        message0_.ID as id1_0_0_,
//                message0_.text as text2_0_0_
//        from
//        Message message0_
//        where
//        message0_.ID=?

        Message message4 = em2.find(Message.class, 1L);
        // no SQL query fired!
        em.close();
    }

    static void saveMessage() {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("entity-manager-vs-session");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {

            transaction.begin();

            Message message = new Message("Hello, world!");
            entityManager.persist(message);

            transaction.commit();
            log.info("Message has been persisted");
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            log.error(e);
        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("entity-manager-vs-session");
    }
}