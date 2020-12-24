package com.tyoma17.hibernate.many_to_many.client;

import com.tyoma17.hibernate.many_to_many.entity.Actor;
import com.tyoma17.hibernate.many_to_many.entity.Movie;
import com.tyoma17.hibernate.many_to_many.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class ManyToManyClientIT {

    @Test
    void testManyToMany() {

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

        Movie movie1 = session.get(Movie.class, 1L);
        assertEquals(1L, movie1.getId());
        assertEquals("American Hustle", movie1.getName());
        assertEquals(1, movie1.getActors().size());

        Actor actor1 = movie1.getActors().get(0);
        assertEquals(1L, actor1.getId());
        assertEquals("Christian Bale", actor1.getName());
        assertEquals(2, actor1.getMovies().size());

        Movie movie2 = session.get(Movie.class, 2L);
        assertEquals(2L, movie2.getId());
        assertEquals("The Prestige", movie2.getName());
        assertEquals(2, movie2.getActors().size());

        Actor actor2 = movie2.getActors().get(1);
        assertEquals(2L, actor2.getId());
        assertEquals("Hugh Jackman", actor2.getName());
        assertEquals(1, actor2.getMovies().size());
        session.close();
    }
}