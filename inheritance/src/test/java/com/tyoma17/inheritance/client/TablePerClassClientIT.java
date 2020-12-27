package com.tyoma17.inheritance.client;

import com.tyoma17.inheritance.util.DbUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TablePerClassClientIT {

    @AfterEach
    void tearDown() {
        try {
            DbUtils.clearDatabase();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testTablePerClass() {
        DbUtils.bootstrapDb_TPC();
        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        List<Object[]> resultList = em.createNativeQuery("SELECT ID, NAME FROM CAT_TPC").getResultList();
        assertEquals(1L, ((BigInteger) resultList.get(0)[0]).longValue());
        assertEquals("Lucy", resultList.get(0)[1]);

        resultList = em.createNativeQuery("SELECT ID, NAME FROM DOG_TPC").getResultList();
        assertEquals(2L, ((BigInteger) resultList.get(0)[0]).longValue());
        assertEquals("Oliver", resultList.get(0)[1]);
        em.close();
    }
}