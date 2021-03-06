package com.tyoma17.hibernate.batch_fetching.util;

import com.github.javafaker.Faker;
import com.tyoma17.hibernate.batch_fetching.entity.Guide;
import com.tyoma17.hibernate.batch_fetching.entity.Student;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Log4j2
public final class DbUtils {

    public static final EntityManagerFactory ENTITY_MANAGER_FACTORY = getEntityManagerFactory();
    private static final Faker FAKER = new Faker();

    private DbUtils() {
    }

    public static void bootstrapDb() {

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            log.info("Inserting 10 000 student-guide pairs into DB...");
            for (int i = 1; i <= 10_000; i++) {

                String fullname = FAKER.name().fullName();
                String staffId = UUID.randomUUID().toString();
                Guide guide = new Guide(staffId, fullname, i);

                fullname = FAKER.name().fullName();
                String enrollmentId = UUID.randomUUID().toString();
                Student student = new Student(enrollmentId, fullname, guide);

                entityManager.persist(student);

                if (i % 1000 == 0) {
                    log.info("1000 records have been inserted");
                }
            }

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

    static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("batch");
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
        Set<String> sequences = new HashSet<>();
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