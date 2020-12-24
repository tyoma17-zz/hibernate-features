package com.tyoma17.hibernate.value_types_collection.client;

import com.tyoma17.hibernate.value_types_collection.entity.Address;
import com.tyoma17.hibernate.value_types_collection.entity.Friend;
import com.tyoma17.hibernate.value_types_collection.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Log4j2
class ValueTypesCollectionClientIT {

    @Test
    void testCollectionsOfValueTypes() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Friend friend = new Friend("Mark Anderson", "markanderson@pluswhere.com");

        friend.getNicknames().add("Marky");
        friend.getNicknames().add("Marco");
        friend.getNicknames().add("Markster");

        friend.getAddresses().add(new Address("street1", "city1", "zipcode1"));
        friend.getAddresses().add(new Address("street2", "city2", "zipcode2"));
        friend.getAddresses().add(new Address("street3", "city3", "zipcode3"));

        session.save(friend);

        session.getTransaction().commit();
        log.info("A friend has been saved as well as his nicknames and adresses");
        session.close();

        session = HibernateUtil.getSessionFactory().openSession();
        friend = session.get(Friend.class, 1L);

        assertEquals(1L, friend.getId());
        assertEquals("Mark Anderson", friend.getName());
        assertEquals("markanderson@pluswhere.com", friend.getEmail());
        assertEquals(3, friend.getNicknames().size());
        assertEquals(3, friend.getAddresses().size());

        assertThat(friend.getNicknames(), containsInAnyOrder("Marky", "Marco", "Markster"));

        List<String> cities = friend.getAddresses().stream().map(Address::getCity).collect(toList());
        List<String> streets = friend.getAddresses().stream().map(Address::getStreet).collect(toList());
        List<String> zipcodes = friend.getAddresses().stream().map(Address::getZipcode).collect(toList());

        assertThat(cities, containsInAnyOrder("city1", "city2", "city3"));
        assertThat(streets, containsInAnyOrder("street1", "street2", "street3"));
        assertThat(zipcodes, containsInAnyOrder("zipcode1", "zipcode2", "zipcode3"));

        Transaction transaction = session.getTransaction();
        friend.getNicknames().add("Marco");

        assertThrows(IllegalStateException.class, () -> transaction.commit());

        transaction.rollback();
        session.close();
    }
}