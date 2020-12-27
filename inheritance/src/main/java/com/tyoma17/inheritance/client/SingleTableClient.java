package com.tyoma17.inheritance.client;

import com.tyoma17.inheritance.entity.single_table.Animal_ST;
import com.tyoma17.inheritance.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Log4j2
public class SingleTableClient {

    public static void main(String[] args) {

        DbUtils.bootstrapDb_ST();
        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        Query query = em.createQuery("FROM Animal_ST");
        List<Animal_ST> animals = query.getResultList();
//        select
//              animal_st0_.id as id2_0_,
//              animal_st0_.name as name3_0_,
//              animal_st0_.DTYPE as dtype1_0_
//        from
//              Animal_ST animal_st0_

        // good for polymorphic queries, no joins required
        // all properties in subclasses must not have not-null constraint
        // Good performance for derived class queries; no joins required

        for (Animal_ST animal : animals) {
            log.info(animal);
        }

        em.close();
    }
}