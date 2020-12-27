package com.tyoma17.hibernate.jpql.util;

import com.tyoma17.hibernate.jpql.entity.Guide;
import com.tyoma17.hibernate.jpql.entity.Student;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@Log4j2
public final class DbUtils {

    public static final EntityManagerFactory ENTITY_MANAGER_FACTORY = getEntityManagerFactory();

    private DbUtils() {
    }

    public static void bootstrapDb() {

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Guide guide1 = new Guide("2000MO10789", "Mike Lawson", 1000);
            Guide guide2 = new Guide("2000IM10901", "Ian Lamb", 2000);

            Student student1 = new Student("2014JT50123", "John Smith", guide1);
            Student student2 = new Student("2014AL50456", "Amy Gill", guide1);

            guide1.getStudents().add(student1);
            guide1.getStudents().add(student2);

            entityManager.persist(guide1);
            entityManager.persist(guide2);

            transaction.commit();
            log.info("Guides and students have been persisted");
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            log.error(e);
        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public static void moreDataForNPlus1SelectsProblem() {

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Guide guide1 = new Guide("2000DO10777", "David Crow", 3000);
            Student student1 = new Student("2014RG50347", "Rahul Singh", guide1);
            guide1.getStudents().add(student1);

            entityManager.persist(guide1);

            transaction.commit();
            log.info("A guide and a student have been persisted");
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            log.error(e);
        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("jpql");
    }

    // https://stackoverflow.com/a/37163503/12943499
    public static void clearDatabase() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:h2:mem:example");
        Statement s = c.createStatement();

        // Disable FK
        s.execute("SET REFERENTIAL_INTEGRITY FALSE");

        // Find all tables and truncate them
        Set<String> tables = new HashSet<String>();
        ResultSet rs = s.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'");
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        rs.close();
        for (String table : tables) {
            s.executeUpdate("TRUNCATE TABLE " + table);
        }

        // Idem for sequences
        Set<String> sequences = new HashSet<String>();
        rs = s.executeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='PUBLIC'");
        while (rs.next()) {
            sequences.add(rs.getString(1));
        }
        rs.close();
        for (String seq : sequences) {
            s.executeUpdate("ALTER SEQUENCE " + seq + " RESTART WITH 1");
        }

        // Enable FK
        s.execute("SET REFERENTIAL_INTEGRITY TRUE");
        s.close();
        c.close();
    }
}