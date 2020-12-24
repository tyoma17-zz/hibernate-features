package com.tyoma17.hibernate.value_types_collection.client;

import com.tyoma17.hibernate.value_types_collection.entity.Address;
import com.tyoma17.hibernate.value_types_collection.entity.Friend;
import com.tyoma17.hibernate.value_types_collection.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;

@Log4j2
public class ValueTypesCollectionClient {

    public static void main(String[] args) {

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
        log.info("Persisted friend: {}", friend);
        session.close();
    }
}