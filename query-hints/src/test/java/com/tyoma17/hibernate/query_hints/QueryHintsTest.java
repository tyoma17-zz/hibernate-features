package com.tyoma17.hibernate.query_hints;

import com.tyoma17.hibernate.query_hints.entity.Address;
import com.tyoma17.hibernate.query_hints.entity.Dog;
import com.tyoma17.hibernate.query_hints.entity.Person;
import org.hibernate.FlushMode;
import org.hibernate.LazyInitializationException;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryHintsTest {

    private static EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

    @Test
    void queryHints() {

        loadPersonData();

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<Person> persons;
        try {

            EntityGraph<Person> personEntityGraph = entityManager.createEntityGraph(Person.class);
            personEntityGraph.addAttributeNodes("dogs");

            persons = entityManager.createQuery("FROM Person", Person.class)
                    .setHint("javax.persistence.fetchgraph", personEntityGraph)
                    // only 'dogs' will be retrieved from DB. As Hibernate is used as JPA provider,
                    // EAGER attributes will be retrieved as well
                    .getResultList();

        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }

        assertDoesNotThrow(() -> persons.get(0).getDogs()
                .stream()
                .map(Dog::getName)
                .forEach(System.out::println));

        assertThrows(LazyInitializationException.class, () -> persons.get(0).getAddresses()
                .stream()
                .map(Address::toString)
                .forEach(System.out::println));

        entityManager = entityManagerFactory.createEntityManager();

        List<Person> personsWithAddresses;
        try {

            EntityGraph<Person> personEntityGraph = entityManager.createEntityGraph(Person.class);
            personEntityGraph.addAttributeNodes("addresses");

            personsWithAddresses = entityManager.createQuery("FROM Person", Person.class)
                    .setHint("javax.persistence.loadgraph", personEntityGraph)
                    // specified attributes ('addresses') as well as EAGER attributes will be retrieved from DB
                    .getResultList();

        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }

        assertDoesNotThrow(() -> personsWithAddresses.get(0).getAddresses()
                .stream()
                .map(Address::toString)
                .forEach(System.out::println));

        assertThrows(LazyInitializationException.class, () -> personsWithAddresses.get(0).getDogs()
                .stream()
                .map(Dog::getName)
                .forEach(System.out::println));

        entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();

            Dog dogCharlie = entityManager.find(Dog.class, 1L);
            dogCharlie.setName("Daisy");

//            Dog dogBuddy = (Dog) entityManager.createQuery("FROM Dog d WHERE d.id = 2")
//                    .getSingleResult(); Hibernate by default flushes the persistence context
//                    and all changes to the database before executing a query
//            DEBUG [main] o.h.SQL
//                    update
//            Dog
//                    set
//            name=?,
//            PERSON_ID=?
//            where
//            id=?
//            TRACE [main] o.h.t.d.s.BasicBinder binding parameter [1] as [VARCHAR] - [Daisy]
//            TRACE [main] o.h.t.d.s.BasicBinder binding parameter [2] as [BIGINT] - [1]
//            TRACE [main] o.h.t.d.s.BasicBinder binding parameter [3] as [BIGINT] - [1]
            Dog dogBuddy = (Dog) entityManager.createQuery("FROM Dog d WHERE d.id = 2")
                    .setHint(QueryHints.HINT_FLUSH_MODE, FlushMode.COMMIT) // update query will be executed after commit
                    .getSingleResult();


            System.out.println(dogBuddy.getName());

            entityManager.getTransaction().commit();

        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }

        entityManager = entityManagerFactory.createEntityManager();

        try {

            entityManager.getTransaction().begin();

//            List<Address> addresses = entityManager.createQuery("FROM Address", Address.class).getResultList();
//            Cities of all addresses will be updated after commit

            List<Address> addresses = entityManager.createQuery("FROM Address", Address.class)
                    .setHint(QueryHints.HINT_READONLY, true) // addresses will not be updated in DB
                    .getResultList();

            for (Address address : addresses) {
                address.setCity("Paris");
            }

            entityManager.getTransaction().commit();
            entityManager.clear();

            Address rigaAddress = entityManager.find(Address.class, 1L);
            assertEquals("Riga", rigaAddress.getCity());

            Address moscowAddress = entityManager.find(Address.class, 2L);
            assertEquals("Moscow", moscowAddress.getCity());

        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void loadPersonData() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {

            transaction.begin();

            Person person = new Person("Artyom");

            Dog dog1 = new Dog("Charlie");
            dog1.setPerson(person);

            Dog dog2 = new Dog("Buddy");
            dog2.setPerson(person);

            Address address1 = new Address("Riga", "Kalku");
            address1.setPerson(person);

            Address address2 = new Address("Moscow", "1st Basmanny Lane");
            address2.setPerson(person);

            person.setAddresses(List.of(address1, address2));
            person.setDogs(List.of(dog1, dog2));
            entityManager.persist(person);
            transaction.commit();

        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("query-hints");
    }
}