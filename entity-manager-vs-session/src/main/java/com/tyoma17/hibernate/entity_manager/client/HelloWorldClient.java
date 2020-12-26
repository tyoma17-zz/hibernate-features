package com.tyoma17.hibernate.entity_manager.client;

import com.tyoma17.hibernate.entity_manager.entity.Message;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@Log4j2
public class HelloWorldClient {

    public static void main(String[] args) {
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

        entityManager = entityManagerFactory.createEntityManager();
        Message message = entityManager.find(Message.class, 1L);
        log.info("Persisted message: {}", message);
        entityManager.close();
    }
}