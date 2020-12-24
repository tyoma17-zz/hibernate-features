package com.tyoma17.hibernate.maps_id.client;

import com.tyoma17.hibernate.maps_id.entity.Customer;
import com.tyoma17.hibernate.maps_id.entity.Passport;
import com.tyoma17.hibernate.maps_id.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class MapsIdClientIT {

    @Test
    void testMapsId() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {

            txn.begin();
            Passport passport = new Passport("925076473");
            Customer customer = new Customer("Nicole Scott", passport);
            log.info("Persisting a customer and one's passport");
            session.persist(customer);
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

        Session session2 = HibernateUtil.getSessionFactory().openSession();
        Passport passport = session2.get(Passport.class, 1L);
        Customer customer = session2.get(Customer.class, 1L);

        assertTrue(passport.getId().equals(customer.getId()) && passport.getId().equals(1L));
        assertEquals("Nicole Scott", customer.getName());
        assertEquals("925076473", customer.getPassport().getPassportNumber());
        assertEquals("925076473", passport.getPassportNumber());
        assertEquals("Nicole Scott", passport.getCustomer().getName());

        assertDoesNotThrow(() -> session2.createSQLQuery("SELECT PASSPORT_ID, NAME FROM CUSTOMER").getResultList());
        session2.close();

        Session session3 = HibernateUtil.getSessionFactory().openSession();
        assertThrows(PersistenceException.class, () -> session3.createSQLQuery("SELECT ID, NAME, PASSPORT_ID FROM CUSTOMER").getResultList());
        session3.close();
    }
}