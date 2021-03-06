package com.tyoma17.hibernate.entity_graph.jpa_api;

import com.tyoma17.hibernate.entity_graph.jpa_api.entity.*;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityGraphViaJpaApiTest {

    private static EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

    @Disabled // activate along with related classes in persistence.xml
    @Test
    void viaJpaApi() {

        loadPersonData();

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<Person> persons;
        try {

            EntityGraph<Person> personEntityGraph = entityManager.createEntityGraph(Person.class);
            personEntityGraph.addAttributeNodes(Person_.DOGS);

            personEntityGraph.addSubgraph(Person_.DOGS)
                    .addAttributeNodes(Dog_.TOYS);

            // retrieved data from DB will be Person.dogs and dogs.toys

            persons = entityManager.createQuery("FROM Person", Person.class)
                    .setHint("javax.persistence.fetchgraph", personEntityGraph)
                    .getResultList();

//            select
//            person0_.id as id1_2_0_,
//                    dogs1_.id as id1_1_1_,
//            toys2_.id as id1_3_2_,
//                    person0_.name as name2_2_0_,
//            dogs1_.name as name2_1_1_,
//                    dogs1_.PERSON_ID as person_i3_1_1_,
//            dogs1_.PERSON_ID as person_i3_1_0__,
//                    dogs1_.id as id1_1_0__,
//            toys2_.dog_id as dog_id3_3_2_,
//                    toys2_.name as name2_3_2_,
//            toys2_.dog_id as dog_id3_3_1__,
//                    toys2_.id as id1_3_1__
//            from
//            Person person0_
//            left outer join
//            Dog dogs1_
//            on person0_.id=dogs1_.PERSON_ID
//            left outer join
//            Toy toys2_
//            on dogs1_.id=toys2_.dog_id

        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }

        assertThrows(LazyInitializationException.class, () -> persons.stream()
                .map(Person::getAddresses)
                .forEach(System.out::println)
        );

        assertDoesNotThrow(() -> persons.stream()
                .map(Person::getDogs)
                .flatMap(Set::stream)
                .map(Dog::getToys)
                .flatMap(Set::stream)
                .map(Toy::getName)
                .forEach(System.out::println)
        );

    }

    private void loadPersonData() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {

            transaction.begin();

            Person person = new Person("Artyom");

            Toy toy1 = new Toy("monkey");
            Toy toy2 = new Toy("giraffe");
            Toy toy3 = new Toy("cat");
            Toy toy4 = new Toy("elephant");

            Dog dog1 = new Dog("Charlie");
            dog1.setPerson(person);
            dog1.addToy(toy1);
            dog1.addToy(toy2);

            Dog dog2 = new Dog("Buddy");
            dog2.setPerson(person);
            dog2.addToy(toy3);
            dog2.addToy(toy4);

            Address address1 = new Address("Riga", "Kalku");
            address1.setPerson(person);

            Address address2 = new Address("Moscow", "1st Basmanny Lane");
            address2.setPerson(person);

            person.setAddresses(Set.of(address1, address2));
            person.setDogs(Set.of(dog1, dog2));
            entityManager.persist(person);
            transaction.commit();

        } finally {

            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("entity-graph");
    }
}