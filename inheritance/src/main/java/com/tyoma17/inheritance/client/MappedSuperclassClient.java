package com.tyoma17.inheritance.client;

import com.tyoma17.inheritance.entity.mapped_superclass.Cat_MS;
import com.tyoma17.inheritance.entity.mapped_superclass.Dog_MS;
import com.tyoma17.inheritance.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Log4j2
public class MappedSuperclassClient {

    public static void main(String[] args) {

        DbUtils.bootstrapDb_MS();
        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        Query query = em.createQuery("FROM Dog_MS");
        List<Dog_MS> dogs = query.getResultList();

//        select
//          dog_ms0_.id as id1_7_,
//          dog_ms0_.name as name2_7_
//        from
//          Dog_MS dog_ms0_

        // polymorphic queries cannot be used with @MappedSuperclass

        log.info(dogs.get(0));


        query = em.createQuery("FROM Cat_MS");
        List<Cat_MS> cats = query.getResultList();
        log.info(cats.get(0));

        em.close();
    }
}