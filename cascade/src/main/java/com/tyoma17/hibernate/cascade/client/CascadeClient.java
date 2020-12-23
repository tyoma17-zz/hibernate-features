package com.tyoma17.hibernate.cascade.client;

import com.tyoma17.hibernate.cascade.entity.Guide;
import com.tyoma17.hibernate.cascade.entity.Student;
import com.tyoma17.hibernate.cascade.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class CascadeClient {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();

        try {

            transaction.begin();
            Guide guide = new Guide("2000MO10789", "Mike Lawson", 1000);
            Student student = new Student("2014JT50123", "John Smith", guide);

//            session.save(guide); <-- no need!
            session.persist(student);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e);

        } finally {

            if (session != null) {
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        Student student = session.get(Student.class, 1L);
        log.info("Saved student: {}", student);

        transaction = session.getTransaction();
        transaction.begin();

        try {
            student = session.get(Student.class, 1L);
            session.delete(student);
            transaction.commit();
        } catch (Exception e) {

            log.error(e);

            if (transaction != null) {
                transaction.rollback();
            }
        } finally {

            if (session != null) {
                session.close();
            }
        }

        log.info("Trying to retrieve from DB deleted Student and Guide objects...");
        session = HibernateUtil.getSessionFactory().openSession();
        student = session.get(Student.class, 1L);
        log.info("Student: {}", student);

        Guide guide = session.get(Guide.class, 1L);
        session.close();
        log.info("Guide: {}", guide);
    }
}