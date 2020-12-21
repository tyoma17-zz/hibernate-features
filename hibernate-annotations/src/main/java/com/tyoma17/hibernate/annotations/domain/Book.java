package com.tyoma17.hibernate.annotations.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PUBLISHER_ID")
    private Publisher publisher;

    @OneToMany
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
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