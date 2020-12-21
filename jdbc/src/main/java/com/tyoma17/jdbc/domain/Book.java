package com.tyoma17.jdbc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String isbn;
    private String name;
    private Publisher publisher;
    private List<Chapter> chapters;

    @Override
    public String toString() {
        return "Book [" +
                "isbn = " + isbn +
                ", name = " + name +
                ", publisher = " + publisher +
                ", chapters = " + chapters + "]";
    }
}