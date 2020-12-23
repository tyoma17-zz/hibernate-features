package com.tyoma17.hibernate.embeddable.client;

import com.tyoma17.hibernate.embeddable.entity.Address;
import com.tyoma17.hibernate.embeddable.entity.Person;
import com.tyoma17.hibernate.embeddable.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmbeddableClientIT {

    @Test
    void savePerson() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Address homeAddress = new Address("200 E Main St", "Seattle", "85123");
            Address billingAddress = new Address("290 N Gamble St", "Shelby", "44875");
            Person person = new Person("Ruby", homeAddress, billingAddress);

            session.save(person);

            txn.commit();
        } catch (Exception e) {
            if (txn != null) {
                txn.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        Person person = session.get(Person.class, 1L);
        session.close();

        assertEquals(1L, person.getId());
        assertEquals("Ruby", person.getName());

        Address homeAddress = person.getHomeAddress();
        assertEquals("Seattle", homeAddress.getCity());
        assertEquals("200 E Main St", homeAddress.getStreet());
        assertEquals("85123", homeAddress.getZipcode());

        Address billingAddress = person.getBillingAddress();
        assertEquals("Shelby", billingAddress.getCity());
        assertEquals("290 N Gamble St", billingAddress.getStreet());
        assertEquals("44875", billingAddress.getZipcode());
    }
}