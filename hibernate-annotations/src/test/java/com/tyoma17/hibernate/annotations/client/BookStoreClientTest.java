package com.tyoma17.hibernate.annotations.client;

import com.tyoma17.hibernate.annotations.domain.Book;
import com.tyoma17.hibernate.annotations.domain.Chapter;
import com.tyoma17.hibernate.annotations.domain.Publisher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookStoreClientIT {

    @Test
    void saveAndRetrieveBook() {
        BookStoreClient.createBookStoreTables();
        BookStoreClient.saveBook();

        Book book = BookStoreClient.retrieveSavedBook();
        assertEquals(1, book.getId());
        assertEquals("978-0439708180", book.getIsbn());
        assertEquals("Harry Potter and the Sorcerer's Stone", book.getName());

        Publisher publisher = book.getPublisher();
        assertEquals(1, publisher.getId());
        assertEquals("4071", publisher.getCode());
        assertEquals("Scholastic", publisher.getName());

        Chapter chapter1 = book.getChapters().get(0);
        assertEquals(1, chapter1.getId());
        assertEquals(1, chapter1.getChapterNumber());
        assertEquals("The Boy Who Lived", chapter1.getTitle());

        Chapter chapter2 = book.getChapters().get(1);
        assertEquals(2, chapter2.getId());
        assertEquals(2, chapter2.getChapterNumber());
        assertEquals("The Vanishing Glass", chapter2.getTitle());
    }

}