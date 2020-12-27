package com.tyoma17.hibernate.optimistic_locking.client.no_optimistic_locking;

import com.tyoma17.hibernate.optimistic_locking.entity.no_optimistic_locking.GuideNo;
import com.tyoma17.hibernate.optimistic_locking.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Log4j2
public class NoOptimisticLockingClient {

    public static void main(String[] args) {
        DbUtils.bootstrapDb_noOptLocking();

        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        GuideNo guide = em.find(GuideNo.class, 2L);
        transaction.commit();
        em.close();

        guide.setSalary(3000);

        EntityManager em2 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction2 = em2.getTransaction();
        transaction2.begin();

        EntityManager em3 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction3 = em3.getTransaction();
        startAmendingSalaryInOtherThread(em3, transaction3); // simulating concurrent users

        GuideNo mergedGuide = em2.merge(guide);
        transaction2.commit();
        completeAmendingSalaryInOtherThread(em3, transaction3); // simulating concurrent users
        em2.close();

        em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        guide = em.find(GuideNo.class, 2L);
        log.info("Guide's salary after concurrent modifications: {}", guide.getSalary());
        em.close();
    }

    private static void startAmendingSalaryInOtherThread(EntityManager em, EntityTransaction transaction) {
        transaction.begin();
        GuideNo guide = em.find(GuideNo.class, 2L);
        guide.setSalary(4000);
    }

    private static void completeAmendingSalaryInOtherThread(EntityManager em, EntityTransaction transaction) {
        transaction.commit(); // <-- last commit wins!
        em.close();
    }
}