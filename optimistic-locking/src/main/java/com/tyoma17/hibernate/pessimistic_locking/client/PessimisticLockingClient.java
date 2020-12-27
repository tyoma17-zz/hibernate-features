package com.tyoma17.hibernate.pessimistic_locking.client;

import com.tyoma17.hibernate.optimistic_locking.entity.no_optimistic_locking.GuideNo;
import com.tyoma17.hibernate.optimistic_locking.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import java.util.List;

@Log4j2
public class PessimisticLockingClient {

    public static void main(String[] args) {

        DbUtils.bootstrapDb_noOptLocking();

        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        List<Object[]> resultList = em.createQuery("SELECT g.name, g.salary FROM GuideNo g")
                .setLockMode(LockModeType.PESSIMISTIC_READ)
//                .setLockMode(LockModeType.PESSIMISTIC_WRITE) <-- if an update is needed
                .getResultList();

        for (Object[] objects : resultList) {
            log.info("Name: {}, salary: {}", objects[0], objects[1]);
        }

        amendSalaryInOtherThread(); // simulating concurrent users

        long sumOfSalaries = (long) em.createQuery("SELECT SUM(g.salary) FROM GuideNo g")
                .getSingleResult();
        log.info("The total salary of all guides is {}", sumOfSalaries);

        // without PESSIMISTIC_READ will be 7000

        transaction.commit();
        em.close();
    }

    static void amendSalaryInOtherThread() {

        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();
            GuideNo guide = em.find(GuideNo.class, 2L);
            guide.setSalary(3000);
            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);

        } finally {

            if (em != null) {
                em.close();
            }
        }
    }
}
