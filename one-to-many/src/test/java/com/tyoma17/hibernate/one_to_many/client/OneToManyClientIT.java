package com.tyoma17.hibernate.one_to_many.client;

import com.tyoma17.hibernate.one_to_many.entity.Guide;
import com.tyoma17.hibernate.one_to_many.entity.Student;
import com.tyoma17.hibernate.one_to_many.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class OneToManyClientIT {

    @Test
    void updateOnOwnerSideAndInverseEnd() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Guide guide1 = new Guide("2000MO10789", "Mike Lawson", 1000);
            Guide guide2 = new Guide("2000IM10901", "Ian Lamb", 2000);

            Student student1 = new Student("2014JT50123", "John Smith", guide1);
            Student student2 = new Student("2014AL50456", "Amy Gill", guide1);

            guide1.getStudents().add(student1);
            guide1.getStudents().add(student2);

            session.persist(guide1);
            session.persist(guide2);

            txn.commit();
        } catch (Exception e) {

            if (txn != null) {
                txn.rollback();
            }

            log.error(e);
        } finally {

            if (session != null) {
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        Student student = session.get(Student.class, 1L);
        assertEquals(1L, student.getId());
        assertEquals("2014JT50123", student.getEnrollmentId());
        assertEquals("John Smith", student.getName());
        assertEquals("Mike Lawson", student.getGuide().getName());

        Student student2 = session.get(Student.class, 2L);
        assertEquals(2L, student2.getId());
        assertEquals("2014AL50456", student2.getEnrollmentId());
        assertEquals("Amy Gill", student2.getName());
        assertEquals("Mike Lawson", student2.getGuide().getName());

        Guide guide = session.get(Guide.class, 1L);
        assertEquals(1L, guide.getId());
        assertEquals(1000, guide.getSalary());
        assertEquals("Mike Lawson", guide.getName());
        assertEquals("2000MO10789", guide.getStaffId());
        assertEquals(2, guide.getStudents().size());

        Guide guide2 = session.get(Guide.class, 2L);
        assertEquals(2L, guide2.getId());
        assertEquals(2000, guide2.getSalary());
        assertEquals("Ian Lamb", guide2.getName());
        assertEquals("2000IM10901", guide2.getStaffId());
        assertEquals(0, guide2.getStudents().size());
        session.close();

        // updating inverse end in a wrong way
        // inverse end does not care about the relationship!
        log.info("Updating student's #2 guide through inverse end in a wrong way...");
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        try {

            transaction.begin();

            guide2 = session.get(Guide.class, 2L);
            student2 = session.get(Student.class, 2L);
            guide2.getStudents().add(student2);

            transaction.commit();
        } catch (Exception e) {

            if (txn != null) {
                txn.rollback();
            }

            log.error(e);
        } finally {

            if (session != null) {
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        student = session.get(Student.class, 1L);
        assertEquals(1L, student.getId());
        assertEquals("2014JT50123", student.getEnrollmentId());
        assertEquals("John Smith", student.getName());
        assertEquals("Mike Lawson", student.getGuide().getName());

        student2 = session.get(Student.class, 2L);
        assertEquals(2L, student2.getId());
        assertEquals("2014AL50456", student2.getEnrollmentId());
        assertEquals("Amy Gill", student2.getName());
        assertEquals("Mike Lawson", student2.getGuide().getName());

        guide = session.get(Guide.class, 1L);
        assertEquals(1L, guide.getId());
        assertEquals(1000, guide.getSalary());
        assertEquals("Mike Lawson", guide.getName());
        assertEquals("2000MO10789", guide.getStaffId());
        assertEquals(2, guide.getStudents().size());

        guide2 = session.get(Guide.class, 2L);
        assertEquals(2L, guide2.getId());
        assertEquals(2000, guide2.getSalary());
        assertEquals("Ian Lamb", guide2.getName());
        assertEquals("2000IM10901", guide2.getStaffId());
        assertEquals(0, guide2.getStudents().size());
        session.close();

        // updating inverse end in a correct way
        // inverse end does not care about the relationship!
        log.info("Updating student's #2 guide through inverse end in a right way...");
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();

        try {

            transaction.begin();

            guide2 = session.get(Guide.class, 2L);
            student2 = session.get(Student.class, 2L);
            guide2.addStudent(student2);

            transaction.commit();
        } catch (Exception e) {

            if (txn != null) {
                txn.rollback();
            }

            log.error(e);
        } finally {

            if (session != null) {
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        student = session.get(Student.class, 1L);
        assertEquals(1L, student.getId());
        assertEquals("2014JT50123", student.getEnrollmentId());
        assertEquals("John Smith", student.getName());
        assertEquals("Mike Lawson", student.getGuide().getName());

        student2 = session.get(Student.class, 2L);
        assertEquals(2L, student2.getId());
        assertEquals("2014AL50456", student2.getEnrollmentId());
        assertEquals("Amy Gill", student2.getName());
        assertEquals("Ian Lamb", student2.getGuide().getName());

        guide = session.get(Guide.class, 1L);
        assertEquals(1L, guide.getId());
        assertEquals(1000, guide.getSalary());
        assertEquals("Mike Lawson", guide.getName());
        assertEquals("2000MO10789", guide.getStaffId());
        assertEquals(1, guide.getStudents().size());

        guide2 = session.get(Guide.class, 2L);
        assertEquals(2L, guide2.getId());
        assertEquals(2000, guide2.getSalary());
        assertEquals("Ian Lamb", guide2.getName());
        assertEquals("2000IM10901", guide2.getStaffId());
        assertEquals(1, guide2.getStudents().size());
        session.close();
    }
}