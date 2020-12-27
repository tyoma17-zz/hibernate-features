package com.tyoma17.hibernate.optimistic_locking.client;

import com.tyoma17.hibernate.optimistic_locking.entity.Guide;
import com.tyoma17.hibernate.optimistic_locking.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class OptimisticLockingClientIT {

    @AfterEach
    void tearDown() {
        try {
            DbUtils.clearDatabase();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void test() {
        DbUtils.bootstrapDb_withOptLocking();

        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Guide guide = em.find(Guide.class, 2L);
        transaction.commit();
        em.close();

        assertEquals(2000, guide.getSalary());
        guide.setSalary(3000);

        EntityManager em2 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction2 = em2.getTransaction();
        transaction2.begin();

        EntityManager em3 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction3 = em3.getTransaction();
        startAmendingSalaryInOtherThread(em3, transaction3); // simulating concurrent users

        Guide mergedGuide = em2.merge(guide);
        transaction2.commit();
        assertEquals(3000, guide.getSalary());
        completeAmendingSalaryInOtherThread(em3, transaction3); // simulating concurrent users
//        update
//              Guide
//        set
//              name=?,
//              salary=?,
//              STAFF_ID=?,
//              version=?
//        where
//              id=?
//        and
//              version=?
//        binding parameter [4] as [INTEGER] - [1]
        em2.close();

        em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        guide = em.find(Guide.class, 2L);
        assertEquals(3000, guide.getSalary());
        em.close();
    }

    private static void startAmendingSalaryInOtherThread(EntityManager em, EntityTransaction transaction) {
        transaction.begin();
        Guide guide = em.find(Guide.class, 2L);
        assertEquals(2000, guide.getSalary());
        guide.setSalary(4000);
    }

    private static void completeAmendingSalaryInOtherThread(EntityManager em, EntityTransaction transaction) {
        try {
            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);
            log.info("User #2, please retry!");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}