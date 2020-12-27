package com.tyoma17.inheritance.client;

import com.tyoma17.inheritance.entity.joined.Animal_J;
import com.tyoma17.inheritance.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Log4j2
public class JoinedClient {


    public static void main(String[] args) {

        DbUtils.bootstrapDb_J();
        EntityManager em = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        Query query = em.createQuery("FROM Animal_J");
        List<Animal_J> animals = query.getResultList();

//        select
//              animal_j0_.id as id1_0_,
//              animal_j0_.name as name2_0_,
//              case
//                  when animal_j0_1_.id is not null then 1
//                  when animal_j0_2_.id is not null then 2
//                  when animal_j0_.id is not null then 0
//              end as clazz_
//        from
//              Animal_J animal_j0_
//        left outer join
//              Dog_J animal_j0_1_
//                  on animal_j0_.id=animal_j0_1_.id
//        left outer join
//              Cat_J animal_j0_2_
//                  on animal_j0_.id=animal_j0_2_.id

        // poor performance for polymorphic queries
        // all properties in subclasses may have not-null constraint
        // not too bad performance for derived class queries

        for (Animal_J animal : animals) {
            log.info(animal);
        }

        em.close();
    }
}