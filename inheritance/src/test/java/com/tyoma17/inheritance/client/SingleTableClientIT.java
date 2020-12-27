package com.tyoma17.inheritance.client;

import com.tyoma17.inheritance.util.DbUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class SingleTableClientIT {

    @AfterEach
    void tearDown() {
        try {
            DbUtils.clearDatabase();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testSingleTableInheritance() {
        DbUtils.bootstrapDb_ST();
        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        List<Object[]> resultList = em.createNativeQuery("SELECT ID, NAME, DTYPE FROM Animal_ST").getResultList();
        assertEquals(1L, ((BigInteger) resultList.get(0)[0]).longValue());
        assertEquals("Lucy", resultList.get(0)[1]);
        assertEquals("Cat_ST", resultList.get(0)[2]);
        assertEquals(2L, ((BigInteger) resultList.get(1)[0]).longValue());
        assertEquals("Oliver", resultList.get(1)[1]);
        assertEquals("Dog_ST", resultList.get(1)[2]);
        em.close();
    }
}