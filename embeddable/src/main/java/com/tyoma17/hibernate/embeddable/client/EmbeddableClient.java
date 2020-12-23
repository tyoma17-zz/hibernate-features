package com.tyoma17.hibernate.embeddable.client;

import com.tyoma17.hibernate.embeddable.entity.Address;
import com.tyoma17.hibernate.embeddable.entity.Person;
import com.tyoma17.hibernate.embeddable.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class EmbeddableClient {

    public static void main(String[] args) {

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
            log.error(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        session = HibernateUtil.getSessionFactory().openSession();
        Person person = session.get(Person.class, 1L);
        session.close();
        log.info("Saved person: {}", person);
    }
}