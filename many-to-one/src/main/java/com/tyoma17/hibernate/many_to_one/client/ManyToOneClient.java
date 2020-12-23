package com.tyoma17.hibernate.many_to_one.client;

import com.tyoma17.hibernate.many_to_one.entity.Guide;
import com.tyoma17.hibernate.many_to_one.entity.Student;
import com.tyoma17.hibernate.many_to_one.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class ManyToOneClient {

    public static void main(String[] args) {

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
        log.info("Save student: {}", student);
    }
}