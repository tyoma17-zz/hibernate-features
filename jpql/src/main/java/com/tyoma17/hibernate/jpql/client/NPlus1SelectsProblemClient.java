package com.tyoma17.hibernate.jpql.client;

import com.tyoma17.hibernate.jpql.entity.Student;
import com.tyoma17.hibernate.jpql.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import java.util.List;

@Log4j2
public class NPlus1SelectsProblemClient {

    public static void main(String[] args) {

        DbUtils.bootstrapDb();
        DbUtils.moreDataForNPlus1SelectsProblem();

        EntityManager entityManager = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        List<Student> students = entityManager.createQuery("FROM Student")
                .getResultList();

//        select
//              student0_.id as id1_1_,
//              student0_.ENROLLMENT_ID as enrollme2_1_,
//              student0_.GUIDE_ID as guide_id4_1_,
//              student0_.name as name3_1_
//        from
//              Student student0_


//        select
//              guide0_.id as id1_0_0_,
//              guide0_.name as name2_0_0_,
//              guide0_.salary as salary3_0_0_,
//              guide0_.STAFF_ID as staff_id4_0_0_
//        from
//              Guide guide0_
//        where
//              guide0_.id=?
//        binding parameter [1] as [BIGINT] - [1]



//        select
//              guide0_.id as id1_0_0_,
//              guide0_.name as name2_0_0_,
//              guide0_.salary as salary3_0_0_,
//              guide0_.STAFF_ID as staff_id4_0_0_
//        from
//              Guide guide0_
//        where
//              guide0_.id=?
//        binding parameter [1] as [BIGINT] - [3]

//        1 Select for parent objects
//        1 select for EACH child object

//        one solution is to use lazy loading
//        another solution is to write queries based on requirements

        for (Student student : students) {
            log.info(student.getName() + ": " + student.getEnrollmentId());
        }

        entityManager.close();
    }
}
