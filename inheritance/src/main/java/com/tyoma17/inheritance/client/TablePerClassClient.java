package com.tyoma17.inheritance.client;

import com.tyoma17.inheritance.entity.table_per_class.Animal_TPC;
import com.tyoma17.inheritance.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Log4j2
public class TablePerClassClient {

    public static void main(String[] args) {

        DbUtils.bootstrapDb_TPC();
        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        Query query = em.createQuery("FROM Animal_TPC");
        List<Animal_TPC> animals = query.getResultList();

//        select
//              animal_tpc0_.id as id1_2_,
//              animal_tpc0_.name as name2_2_,
//              animal_tpc0_.clazz_ as clazz_
//        from
//              ( select
//                  id,
//                  name,
//                  1 as clazz_
//              from
//                Dog_TPC
//              union
//              all select
//                  id,
//                  name,
//                  2 as clazz_
//              from
//                  Cat_TPC
//    ) animal_tpc0_

        // not good for polymorphic queries
        // good performance for derived class queries

        for (Animal_TPC animal : animals) {
            log.info(animal);
        }

        em.close();
    }
}