package com.tyoma17.jdbc.service;

import com.tyoma17.jdbc.domain.Book;
import com.tyoma17.jdbc.domain.Chapter;
import com.tyoma17.jdbc.domain.Publisher;
import com.tyoma17.jdbc.util.DbUtils;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookStoreServiceIT {

    private static final String CHAPTER_1 = "chapter_1";
    private static final String CHAPTER_2 = "chapter_2";
    private static final String CODE = "code";
    private static final String PUBLISHER_NAME = "publisher_name";
    private static final String BOOK_NAME = "book_name";
    private static final String ISBN = "isbn";

    @Test
    void persistAndRetrieveBook() throws SQLException {

        DbUtils.createBookStoreTables();

        List<Chapter> chapters = List.of(
                new Chapter(CHAPTER_1, 1),
                new Chapter(CHAPTER_2, 2));
        Publisher publisher = new Publisher(CODE, PUBLISHER_NAME);
        Book book = new Book(ISBN, BOOK_NAME, publisher, chapters);

        BookStoreService.persistObjectGraph(book);

        Book savedBook = BookStoreService.retrieveObjectGraph(ISBN);
        assertEquals(BOOK_NAME, savedBook.getName());
        assertEquals(ISBN, savedBook.getIsbn());
        assertEquals(2, savedBook.getChapters().size());

        Publisher savedPublisher = savedBook.getPublisher();
        assertEquals(CODE, savedPublisher.getCode());
        assertEquals(PUBLISHER_NAME, savedPublisher.getName());

        Chapter chapter1 = savedBook.getChapters().get(0);
        assertEquals(CHAPTER_1, chapter1.getTitle());
        assertEquals(1, chapter1.getChapterNumber());

        Chapter chapter2 = savedBook.getChapters().get(1);
        assertEquals(CHAPTER_2, chapter2.getTitle());
        assertEquals(2, chapter2.getChapterNumber());

        DbUtils.setDbCloseDelay(0);
    }

}