package com.tyoma17.inheritance.util;

import com.tyoma17.inheritance.entity.joined.Cat_J;
import com.tyoma17.inheritance.entity.joined.Dog_J;
import com.tyoma17.inheritance.entity.mapped_superclass.Cat_MS;
import com.tyoma17.inheritance.entity.mapped_superclass.Dog_MS;
import com.tyoma17.inheritance.entity.single_table.Cat_ST;
import com.tyoma17.inheritance.entity.single_table.Dog_ST;
import com.tyoma17.inheritance.entity.table_per_class.Cat_TPC;
import com.tyoma17.inheritance.entity.table_per_class.Dog_TPC;
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

    private static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("inheritance");
    }

    public static void bootstrapDb_ST() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Cat_ST cat = new Cat_ST();
            cat.setName("Lucy");

            Dog_ST dog = new Dog_ST();
            dog.setName("Oliver");

            entityManager.persist(cat);
            entityManager.persist(dog);

            transaction.commit();
            log.info("A dog and a cat have been persisted");

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public static void bootstrapDb_J() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Cat_J cat = new Cat_J();
            cat.setName("Lucy");

            Dog_J dog = new Dog_J();
            dog.setName("Oliver");

            entityManager.persist(cat);
            entityManager.persist(dog);

            transaction.commit();
            log.info("A dog and a cat have been persisted");

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public static void bootstrapDb_TPC() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Cat_TPC cat = new Cat_TPC();
            cat.setName("Lucy");

            Dog_TPC dog = new Dog_TPC();
            dog.setName("Oliver");

            entityManager.persist(cat);
            entityManager.persist(dog);

            transaction.commit();
            log.info("A dog and a cat have been persisted");

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public static void bootstrapDb_MS() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Cat_MS cat = new Cat_MS();
            cat.setName("Lucy");

            Dog_MS dog = new Dog_MS();
            dog.setName("Oliver");

            entityManager.persist(cat);
            entityManager.persist(dog);

            transaction.commit();
            log.info("A dog and a cat have been persisted");

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
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