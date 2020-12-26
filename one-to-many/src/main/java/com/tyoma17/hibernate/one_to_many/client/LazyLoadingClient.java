package com.tyoma17.hibernate.one_to_many.client;

import com.tyoma17.hibernate.one_to_many.entity.Guide;
import com.tyoma17.hibernate.one_to_many.entity.Student;
import com.tyoma17.hibernate.one_to_many.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Log4j2
public class LazyLoadingClient {

    public static void main(String[] args) {
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
//        select
//        guide0_.id as id1_0_0_,
//                guide0_.name as name2_0_0_,
//        guide0_.salary as salary3_0_0_,
//                guide0_.STAFF_ID as staff_id4_0_0_
//        from
//        Guide guide0_
//        where
//        guide0_.id=?
        log.info("Saved guide: {}", guide);

        log.info("Loading students within an open session...");
        List<Student> students = guide.getStudents();
//        select
//        students0_.GUIDE_ID as guide_id4_1_0_,
//                students0_.id as id1_1_0_,
//        students0_.id as id1_1_1_,
//                students0_.ENROLLMENT_ID as enrollme2_1_1_,
//        students0_.GUIDE_ID as guide_id4_1_1_,
//                students0_.name as name3_1_1_
//        from
//        Student students0_
//        where
//        students0_.GUIDE_ID=?

        for (Student student : students) {
            log.info("Saved student: {}", student);
        }

        session.close();

        session = HibernateUtil.getSessionFactory().openSession();
        guide = session.get(Guide.class, 1L);
//        select
//        guide0_.id as id1_0_0_,
//                guide0_.name as name2_0_0_,
//        guide0_.salary as salary3_0_0_,
//                guide0_.STAFF_ID as staff_id4_0_0_
//        from
//        Guide guide0_
//        where
//        guide0_.id=?
        session.close();
        log.info("Loading students within an closed session...");
        try {
            students = guide.getStudents();

            for (Student student : students) { // LazyInitializationException !
                log.info("Saved student: {}", student);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}