package com.tyoma17.jdbc.client;

import com.tyoma17.jdbc.domain.Book;
import com.tyoma17.jdbc.domain.Chapter;
import com.tyoma17.jdbc.domain.Publisher;
import com.tyoma17.jdbc.service.BookStoreService;
import com.tyoma17.jdbc.util.DbUtils;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class BookStoreClient {

    private static final BookStoreService BOOK_STORE_SERVICE = new BookStoreService();

    public static void main(String[] args) {

        DbUtils.createBookStoreTables();

        Publisher publisher = new Publisher("4071", "Scholastic");

        List<Chapter> chapters = new ArrayList<>();
        Chapter chapter1 = new Chapter("The Boy Who Lived", 1);
        chapters.add(chapter1);
        Chapter chapter2 = new Chapter("The Vanishing Glass", 2);
        chapters.add(chapter2);

        Book book = new Book("978-0439708180", "Harry Potter and the Sorcerer's Stone", publisher, chapters);
        BOOK_STORE_SERVICE.persistObjectGraph(book);

        Book savedBook = BOOK_STORE_SERVICE.retrieveObjectGraph("978-0439708180");
        log.debug("Saved in DB book: {}", savedBook);
    }
}
