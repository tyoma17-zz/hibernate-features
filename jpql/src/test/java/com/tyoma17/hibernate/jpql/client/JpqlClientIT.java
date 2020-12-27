package com.tyoma17.hibernate.jpql.client;

import com.tyoma17.hibernate.jpql.entity.Guide;
import com.tyoma17.hibernate.jpql.entity.Student;
import com.tyoma17.hibernate.jpql.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class JpqlClientIT {

    @AfterEach
    void tearDown() {
        try {
            DbUtils.clearDatabase();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testJpql() {
        DbUtils.bootstrapDb();
        EntityManager entityManager = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Guide> guides = null;

        try {

            Query query = entityManager.createQuery("FROM Guide");
            guides = query.getResultList();

            Guide guide1 = guides.get(0);
            assertEquals(1L, guide1.getId());
            assertEquals("Mike Lawson", guide1.getName());
            assertEquals("2000MO10789", guide1.getStaffId());
            assertEquals(1000, guide1.getSalary());
            assertEquals(2, guide1.getStudents().size());

            Guide guide2 = guides.get(1);
            assertEquals(2L, guide2.getId());
            assertEquals("Ian Lamb", guide2.getName());
            assertEquals("2000IM10901", guide2.getStaffId());
            assertEquals(2000, guide2.getSalary());

            Student student1 = guide1.getStudents().get(0);
            Student student2 = guide1.getStudents().get(1);

            /////////////////////////////////////////////////////////////////

            query = entityManager.createQuery("SELECT s.name FROM Student as s");
            List<String> studentNames = query.getResultList();

            log.info("Students names:");
            assertEquals("John Smith", studentNames.get(0));
            assertEquals("Amy Gill", studentNames.get(1));

            /////////////////////////////////////////////////////////////////

            query = entityManager.createQuery("FROM Guide g WHERE g.name = :name");
            query.setParameter("name", "Ian Lamb");
            log.info("Searching for a guide with the name 'Ian Lamb'...");
            assertEquals(guide2, query.getSingleResult());

            /////////////////////////////////////////////////////////////////

            query = entityManager.createNativeQuery("SELECT * FROM STUDENT", Student.class);
//          query = entityManager.createNativeQuery("SELECT * FROM STUDENT"); <-- ClassCastException !
            List<Student> students = query.getResultList();
            assertEquals(student1, students.get(0));
            assertEquals(student2, students.get(1));

            /////////////////////////////////////////////////////////////////

            log.info("Searching for students with enrollmentId like %JT5%");
            students = entityManager.createNamedQuery("findByEnrollmentIdLike")
                    .setParameter("enrollmentId", "%JT5%")
                    .getResultList();

            assertEquals(student1, students.get(0));

            /////////////////////////////////////////////////////////////////

            entityManager.clear();
            transaction.begin();

            student2 = entityManager.find(Student.class, 2L);
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
            transaction.commit();
            entityManager.clear();
            student2 = entityManager.find(Student.class, 2L);
            assertEquals("John Doe", student2.getName());

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

            fail(e.getMessage(), e);
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