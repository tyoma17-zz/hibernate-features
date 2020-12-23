package com.tyoma17.hibernate.orphan_removal.client;

import com.tyoma17.hibernate.one_to_many.util.HibernateUtil;
import com.tyoma17.hibernate.orphan_removal.entity.Guide;
import com.tyoma17.hibernate.orphan_removal.entity.Student;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class OrphanRemovalClient {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Guide guide1 = new Guide("2000MO10789", "Mike Lawson", 1000);
            Guide guide2 = new Guide("2000IM10901", "Ian Lamb", 2000);

            Student student1 = new Student("2014JT50123", "John Smith", guide1);
            Student student2 = new Student("2014AL50456", "Amy Gill", guide2);
            Student student3 = new Student("2014XY50981", "Jane Roe", guide2);
            Student student4 = new Student("2014OL50443", "John Doe", guide2);

            session.persist(student1);
            session.persist(student2);
            session.persist(student3);
            session.persist(student4);

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

        log.info("Two guides, four students are persisted to DB");
        log.info("Three of them have the same guide");
        log.info("Deleting one of these three students...");

        session = HibernateUtil.getSessionFactory().openSession();
        txn = session.getTransaction();

        try {

            txn.begin();
            Student student = session.get(Student.class, 2L);
            session.delete(student); // won't work without orphanRemoval in Student class
            // students #3 and #4 have become orphans, so will be deleted as well
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

        for (long i = 1; i <= 4; i++) {
            Student student = session.get(Student.class, i);
            log.info("Saved student #" + i + ": {}", student);
        }

        for (long i = 1; i <= 2; i++) {
            Guide guide = session.get(Guide.class, i);
            log.info("Saved student #" + i + ": {}", guide);
        }

        session.close();
    }
}