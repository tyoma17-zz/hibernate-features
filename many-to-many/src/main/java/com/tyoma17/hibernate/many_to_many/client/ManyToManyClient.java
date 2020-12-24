package com.tyoma17.hibernate.many_to_many.client;

import com.tyoma17.hibernate.many_to_many.entity.Actor;
import com.tyoma17.hibernate.many_to_many.entity.Movie;
import com.tyoma17.hibernate.many_to_many.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Log4j2
public class ManyToManyClient {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Movie movie1 = new Movie("American Hustle");
            Movie movie2 = new Movie("The Prestige");

            Actor actor1 = new Actor("Christian Bale");
            Actor actor2 = new Actor("Hugh Jackman");

            movie1.getActors().add(actor1);

            movie2.getActors().add(actor1);
            movie2.getActors().add(actor2);

            session.persist(movie1);
            session.persist(movie2);

            txn.commit();
            log.info("Persisted to DB one movie with one actor and another movie with two actors");

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

        for (long i = 1; i <= 2; i++) {
            Movie movie = session.get(Movie.class, i);
            log.info("Persisted movie: {}", movie);
        }

        session.close();
    }
}