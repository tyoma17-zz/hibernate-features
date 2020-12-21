package com.tyoma17.jdbc.service;

import com.tyoma17.jdbc.domain.Book;
import com.tyoma17.jdbc.domain.Chapter;
import com.tyoma17.jdbc.domain.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.tyoma17.jdbc.util.DbUtils.CONNECTION_URL;

public class BookStoreService {

    public static void persistObjectGraph(Book book) {

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement publisherStmt = connection.prepareStatement("INSERT INTO PUBLISHER (CODE, PUBLISHER_NAME) VALUES (?, ?)");
             PreparedStatement bookStmt = connection.prepareStatement("INSERT INTO BOOK (ISBN, BOOK_NAME, PUBLISHER_CODE) VALUES (?, ?, ?)");
             PreparedStatement chapterStmt = connection.prepareStatement("INSERT INTO CHAPTER (BOOK_ISBN, CHAPTER_NUM, TITLE) VALUES (?, ?, ?)")) {

            publisherStmt.setString(1, book.getPublisher().getCode());
            publisherStmt.setString(2, book.getPublisher().getName());
            publisherStmt.executeUpdate();

            bookStmt.setString(1, book.getIsbn());
            bookStmt.setString(2, book.getName());
            bookStmt.setString(3, book.getPublisher().getCode());
            bookStmt.executeUpdate();

            for (Chapter chapter : book.getChapters()) {
                chapterStmt.setString(1, book.getIsbn());
                chapterStmt.setInt(2, chapter.getChapterNumber());
                chapterStmt.setString(3, chapter.getTitle());
                chapterStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Book retrieveObjectGraph(String isbn) {

        Book book = new Book();

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement bookPublisherStmt = connection.prepareStatement("SELECT * FROM BOOK, PUBLISHER WHERE BOOK.PUBLISHER_CODE = PUBLISHER.CODE AND BOOK.ISBN = ?");
             PreparedStatement chapterStmt = connection.prepareStatement("SELECT * FROM CHAPTER WHERE BOOK_ISBN = ?")) {

            bookPublisherStmt.setString(1, isbn);
            ResultSet rs = bookPublisherStmt.executeQuery();

            book = new Book();

            if (rs.next()) {
                book.setIsbn(rs.getString("ISBN"));
                book.setName(rs.getString("BOOK_NAME"));

                Publisher publisher = new Publisher();
                publisher.setCode(rs.getString("CODE"));
                publisher.setName(rs.getString("PUBLISHER_NAME"));
                book.setPublisher(publisher);
            }

            rs.close();

            List<Chapter> chapters = new ArrayList<>();
            chapterStmt.setString(1, isbn);
            rs = chapterStmt.executeQuery();

            while (rs.next()) {
                Chapter chapter = new Chapter();
                chapter.setTitle(rs.getString("TITLE"));
                chapter.setChapterNumber(rs.getInt("CHAPTER_NUM"));
                chapters.add(chapter);
            }
            book.setChapters(chapters);

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }
}
