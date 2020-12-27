package com.tyoma17.hibernate.jpql.client;

import com.tyoma17.hibernate.jpql.entity.Guide;
import com.tyoma17.hibernate.jpql.entity.Student;
import com.tyoma17.hibernate.jpql.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.List;

@Log4j2
public class JpqlClient {

    public static void main(String[] args) {

        DbUtils.bootstrapDb();
        EntityManager entityManager = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Guide> guides = null;

        try {

            Query query = entityManager.createQuery("FROM Guide");
            guides = query.getResultList();

            for (Guide guide : guides) {
                log.info("Persisted guide: {}", guide);
            }

            /////////////////////////////////////////////////////////////////

            query = entityManager.createQuery("SELECT s.name FROM Student as s");
            List<String> studentNames = query.getResultList();

            log.info("Students names:");
            for (String name : studentNames) {
                log.info(name);
            }

            /////////////////////////////////////////////////////////////////

            query = entityManager.createQuery("FROM Guide g WHERE g.name = :name");
            query.setParameter("name", "Ian Lamb");
            log.info("Searching for a guide with the name 'Ian Lamb'...");
            log.info("Result: {}", query.getSingleResult());

            /////////////////////////////////////////////////////////////////

            query = entityManager.createNativeQuery("SELECT * FROM STUDENT", Student.class);
//          query = entityManager.createNativeQuery("SELECT * FROM STUDENT"); <-- ClassCastException !
            List<Student> students = query.getResultList();
            log.info("Students:");

            for (Student student : students) {
                log.info(student);
            }

            /////////////////////////////////////////////////////////////////

            log.info("Searching for students with enrollmentId like %JT5%");
            students = entityManager.createNamedQuery("findByEnrollmentIdLike")
                    .setParameter("enrollmentId", "%JT5%")
                    .getResultList();

            for (Student student : students) {
                log.info(student);
            }

            /////////////////////////////////////////////////////////////////

            entityManager.clear();
            transaction.begin();

            Student student2 = entityManager.find(Student.class, 2L);
            student2.setName("John Doe");

            // before the query is executed persistence context will be synchronized/flushed
            // this behaviour can be changed: entityManager.setFlushMode(...)
            String name = (String) entityManager.createQuery("Select s.name FROM Student s WHERE s.id = :id")
                    .setParameter("id", 2L)
                    .getSingleResult();
//            update
//                    Student
//            set
//                    ENROLLMENT_ID=?,
//                    GUIDE_ID=?,
//                    name=?
//            where
//              id=?

//            select
//            student0_.name as col_0_0_
//                    from
//            Student student0_
//            where
//            student0_.id=?
            log.info("Name of student #2: {}", name);
            transaction.commit();

            /////////////////////////////////////////////////////////////////

            log.info("Testing Join Fetch...");
            entityManager.clear();
            guides = entityManager.createQuery("FROM Guide g JOIN FETCH g.students")
                    .getResultList();
//            select
//              guide0_.id as id1_0_0_,
//                    students1_.id as id1_1_1_,
//              guide0_.name as name2_0_0_,
//                    guide0_.salary as salary3_0_0_,
//              guide0_.STAFF_ID as staff_id4_0_0_,
//                    students1_.ENROLLMENT_ID as enrollme2_1_1_,
//              students1_.GUIDE_ID as guide_id4_1_1_,
//                    students1_.name as name3_1_1_,
//              students1_.GUIDE_ID as guide_id4_1_0__,
//                    students1_.id as id1_1_0__
//            from
//              Guide guide0_
//            inner join
//              Student students1_
//            on guide0_.id=students1_.GUIDE_ID

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            log.error(e.getMessage(), e);
        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }

        log.info("Listing all guides' students after entity manager is closed: ");
        guides.stream()
                .distinct()
                .map(Guide::getStudents)
                .flatMap(List::stream)
                .forEach(log::info);

    }
}