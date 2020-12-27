package com.tyoma17.hibernate.optimistic_locking.client.no_optimistic_locking;

import com.tyoma17.hibernate.optimistic_locking.entity.no_optimistic_locking.GuideNo;
import com.tyoma17.hibernate.optimistic_locking.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class NoOptimisticLockingClientIT {

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

        DbUtils.bootstrapDb_noOptLocking();

        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        GuideNo guide = em.find(GuideNo.class, 2L);
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

        GuideNo mergedGuide = em2.merge(guide);
        assertEquals(3000, guide.getSalary());
        transaction2.commit();
        completeAmendingSalaryInOtherThread(em3, transaction3); // simulating concurrent users
        em2.close();

        em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        guide = em.find(GuideNo.class, 2L);
        assertEquals(4000, guide.getSalary());
        em.close();

    }

    static void startAmendingSalaryInOtherThread(EntityManager em, EntityTransaction transaction) {
        transaction.begin();
        GuideNo guide = em.find(GuideNo.class, 2L);
        assertEquals(2000, guide.getSalary());
        guide.setSalary(4000);
    }

    static void completeAmendingSalaryInOtherThread(EntityManager em, EntityTransaction transaction) {
        GuideNo guide = em.find(GuideNo.class, 2L);
        assertEquals(4000, guide.getSalary());
        transaction.commit(); // <-- last commit wins!
        em.close();
    }
}