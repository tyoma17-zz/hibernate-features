package com.tyoma17.inheritance.client;

import com.tyoma17.inheritance.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class JoinedClientIT {

    @AfterEach
    void tearDown() {
        try {
            DbUtils.clearDatabase();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testJoinedInheritance() {
        DbUtils.bootstrapDb_J();
        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        List<Object[]> resultList = em.createNativeQuery("SELECT a.ID ID1, a.NAME, c.ID ID2 FROM ANIMAL_J a " +
                "INNER JOIN CAT_J c on a.id = c.id").getResultList();
        assertEquals(1L, ((BigInteger) resultList.get(0)[0]).longValue());
        assertEquals("Lucy", resultList.get(0)[1]);
        assertEquals(1L, ((BigInteger) resultList.get(0)[2]).longValue());

        resultList = em.createNativeQuery("SELECT a.ID ID1, a.NAME, c.ID ID2 FROM ANIMAL_J a " +
                "INNER JOIN DOG_J c on a.id = c.id").getResultList();
        assertEquals(2L, ((BigInteger) resultList.get(0)[0]).longValue());
        assertEquals("Oliver", resultList.get(0)[1]);
        assertEquals(2L, ((BigInteger) resultList.get(0)[2]).longValue());
        em.close();
    }
}