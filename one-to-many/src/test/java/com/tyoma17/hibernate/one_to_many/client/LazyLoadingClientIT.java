package com.tyoma17.hibernate.one_to_many.client;

import com.tyoma17.hibernate.one_to_many.entity.Guide;
import com.tyoma17.hibernate.one_to_many.entity.Student;
import com.tyoma17.hibernate.one_to_many.util.DbUtils;
import com.tyoma17.hibernate.one_to_many.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class LazyLoadingClientIT {

    @AfterEach
    void tearDown() {
            try {
                DbUtils.clearDatabase();
            } catch (Exception e) {
                fail(e.getMessage());
            }
    }

    @Test
    void testLazyLoadingOutsideSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Guide guide = new Guide("2000MO10789", "Mike Lawson", 1000);

            Student student1 = new Student("2014JT50123", "John Smith", guide);
            Student student2 = new Student("2014AL50456", "Amy Gill", guide);

            guide.addStudent(student1);
            guide.addStudent(student2);

            session.persist(guide);

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
        Guide guide = session.get(Guide.class, 1L);
        session.close();
        log.info("Loading students within an closed session...");
        List<Student> students = guide.getStudents();

        assertThrows(LazyInitializationException.class, () -> {
            for (Student student : students) {
                log.info("Saved student: {}", student);
            }
        });
    }
}