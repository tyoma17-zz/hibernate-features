package com.tyoma17.hibernate.pessimistic_locking.client;

import com.tyoma17.hibernate.optimistic_locking.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class PessimisticLockingClientIT {

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

        List<Object[]> resultList = em.createQuery("SELECT g.name, g.salary FROM GuideNo g")
                .setLockMode(LockModeType.PESSIMISTIC_READ)
//                .setLockMode(LockModeType.PESSIMISTIC_WRITE) <-- if an update is needed
                .getResultList();

        assertEquals("Mike Lawson", resultList.get(0)[0]);
        assertEquals(1000, resultList.get(0)[1]);
        assertEquals("Ian Lamb", resultList.get(1)[0]);
        assertEquals(2000, resultList.get(1)[1]);
        assertEquals("David Crow", resultList.get(2)[0]);
        assertEquals(3000, resultList.get(2)[1]);

        PessimisticLockingClient.amendSalaryInOtherThread(); // simulating concurrent users

        long sumOfSalaries = (long) em.createQuery("SELECT SUM(g.salary) FROM GuideNo g")
                .getSingleResult();

        assertEquals(6000, sumOfSalaries);

        // without PESSIMISTIC_READ will be 7000

        transaction.commit();
        em.close();
    }
}