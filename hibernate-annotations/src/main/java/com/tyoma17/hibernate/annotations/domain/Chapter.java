package com.tyoma17.hibernate.annotations.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CHAPTER")
public class Chapter {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CHAPTER_NUMBER")
    private Integer chapterNumber;

    public Chapter(String title, Integer chapterNumber) {
        this.title = title;
        this.chapterNumber = chapterNumber;
    }

    @Override
    public String toString() {
        return "Chapter [" +
                "id = " + id +
                ", title = " + title +
                ", chapterNumber = " + chapterNumber + "]";
    }
}
