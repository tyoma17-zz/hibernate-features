package com.tyoma17.hibernate.xml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Book {

    private Long id;
    private String isbn;
    private String name;
    private Publisher publisher;
    private List<Chapter> chapters;

    public Book(String isbn, String name, Publisher publisher, List<Chapter> chapters) {
        this.isbn = isbn;
        this.name = name;
        this.publisher = publisher;
        this.chapters = chapters;
    }

    @Override
    public String toString() {
        return "Book [" +
                "id = " + id +
                ", isbn = " + isbn +
                ", name = " + name +
                ", publisher = " + publisher +
                ", chapters = " + chapters + "]";
    }
}