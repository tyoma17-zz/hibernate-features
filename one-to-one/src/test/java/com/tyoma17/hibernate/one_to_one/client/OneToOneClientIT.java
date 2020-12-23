package com.tyoma17.hibernate.one_to_one.client;

import com.tyoma17.hibernate.one_to_one.entity.Customer;
import com.tyoma17.hibernate.one_to_one.entity.Passport;
import com.tyoma17.hibernate.one_to_one.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class OneToOneClientIT {

    @Test
    void saveCustomerAndPassport() {

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

        session = HibernateUtil.getSessionFactory().openSession();

        Customer customer = session.get(Customer.class, 1L);
        assertEquals(1L, customer.getId());
        assertEquals("Nicole Scott", customer.getName());
        assertEquals("925076473", customer.getPassport().getPassportNumber());

        Passport passport = session.get(Passport.class, 1L);
        assertEquals(1L, passport.getId());
        assertEquals("925076473", passport.getPassportNumber());
        assertEquals("Nicole Scott", passport.getCustomer().getName());

        session.close();
    }

}