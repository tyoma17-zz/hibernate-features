package com.tyoma17.hibernate.one_to_many.client;

import com.tyoma17.hibernate.one_to_many.entity.Guide;
import com.tyoma17.hibernate.one_to_many.entity.Student;
import com.tyoma17.hibernate.one_to_many.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class OneToManyClient {

    public static void main(String[] args) {

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
        log.info("Saved student #1: {}", student);
        Student student2 = session.get(Student.class, 2L);
        log.info("Saved student #2: {}", student2);
        Guide guide = session.get(Guide.class, 1L);
        log.info("Saved guide #1: {}", guide);
        Guide guide2 = session.get(Guide.class, 2L);
        log.info("Saved guide #2: {}", guide2);
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
        student2 = session.get(Student.class, 2L);
        log.info("Student #2: {}", student2);
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
        student2 = session.get(Student.class, 2L);
        log.info("Student #2: {}", student2);
        session.close();
    }
}