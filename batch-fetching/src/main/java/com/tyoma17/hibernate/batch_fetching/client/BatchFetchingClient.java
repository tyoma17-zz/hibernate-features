package com.tyoma17.hibernate.batch_fetching.client;

import com.tyoma17.hibernate.batch_fetching.entity.Student;
import com.tyoma17.hibernate.batch_fetching.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import java.util.List;

@Log4j2
public class BatchFetchingClient {

    public static void main(String[] args) {
        DbUtils.bootstrapDb();
        EntityManager entityManager = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        List<Student> students = entityManager.createQuery("FROM Student").getResultList();
//        select
//              student0_.id as id1_1_,
//              student0_.ENROLLMENT_ID as enrollme2_1_,
//              student0_.GUIDE_ID as guide_id4_1_,
//              student0_.name as name3_1_
//        from
//              Student student0_
        for (Student student : students) {
            log.info("{}: {}", student.getName(), student.getEnrollmentId());

            if (student.getGuide() != null) {
                log.info("Student's guide: {}", student.getGuide().getName());
//                select
//                      guide0_.id as id1_0_0_,
//                      guide0_.name as name2_0_0_,
//                      guide0_.salary as salary3_0_0_,
//                      guide0_.STAFF_ID as staff_id4_0_0_
//                from
//                      Guide guide0_
//                where
//                      guide0_.id in (
//                        ?, ?, ..., ?
//        )
//                binding parameter [1] as [BIGINT] - [1]
//                binding parameter [2] as [BIGINT] - [2]
//                ...
//                binding parameter [3] as [BIGINT] - [1000]
            }
        }

//        without @BatchSize(size = 1000)
//        60307100 nanoseconds spent preparing 10001 JDBC statements;
//        355353400 nanoseconds spent executing 10001 JDBC statements;

//        with @BatchSize(size = 1000)
//        6407200 nanoseconds spent preparing 11 JDBC statements;
//        170376300 nanoseconds spent executing 11 JDBC statements;

        entityManager.close();
    }
}
