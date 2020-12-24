package com.tyoma17.hibernate.maps_id.client;

import com.tyoma17.hibernate.maps_id.entity.Customer;
import com.tyoma17.hibernate.maps_id.entity.Passport;
import com.tyoma17.hibernate.maps_id.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class MapsIdClient {

    public static void main(String[] args) {

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
        log.info("Customer: {}", customer);
        session.close();
    }
}