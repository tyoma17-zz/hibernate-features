package com.tyoma17.hibernate.many_to_one.client;

import com.tyoma17.hibernate.many_to_one.entity.Guide;
import com.tyoma17.hibernate.many_to_one.entity.Student;
import com.tyoma17.hibernate.many_to_one.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class ManyToOneClientIT {

    @Test
    void createStudent() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {

            txn.begin();

            Guide guide = new Guide("2000MO10789", "Mike Lawson", 1000);
            Student student = new Student("2014JT50123", "John Smith", guide);

            session.save(guide);
            session.save(student);

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
        session.close();

        assertEquals(1L, student.getId());
        assertEquals("John Smith", student.getName());
        assertEquals("2014JT50123", student.getEnrollmentId());

        Guide guide = student.getGuide();
        assertEquals(1L, guide.getId());
        assertEquals("Mike Lawson", guide.getName());
        assertEquals("2000MO10789", guide.getStaffId());
        assertEquals(1000, guide.getSalary());
    }
}