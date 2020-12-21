package com.tyoma17.hibernate.annotations.client;


import com.tyoma17.hibernate.annotations.domain.Book;
import com.tyoma17.hibernate.annotations.domain.Chapter;
import com.tyoma17.hibernate.annotations.domain.Publisher;
import com.tyoma17.hibernate.annotations.util.DbUtils;
import com.tyoma17.hibernate.annotations.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class BookStoreClient {

    public static void main(String[] args) {
        createBookStoreTables();
        saveBook();
        retrieveSavedBook();
    }

    public static Book retrieveSavedBook() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Book book = session.get(Book.class, 1L);
        log.info("Saved book: {}", book);
        session.close();
        return book;
    }

    public static void saveBook() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Publisher publisher = new Publisher("4071", "Scholastic");
        session.save(publisher);

        List<Chapter> chapters = new ArrayList<>();
        Chapter chapter1 = new Chapter("The Boy Who Lived", 1);
        chapters.add(chapter1);
        session.save(chapter1);

        Chapter chapter2 = new Chapter("The Vanishing Glass", 2);
        chapters.add(chapter2);
        session.save(chapter2);

        Book book = new Book("978-0439708180", "Harry Potter and the Sorcerer's Stone", publisher, chapters);
        session.save(book);

        session.getTransaction().commit();
        session.close();
    }

    public static void createBookStoreTables() {
        log.info("Creating bookstore tables...");
        DbUtils.createBookStoreTables();
    }
}
