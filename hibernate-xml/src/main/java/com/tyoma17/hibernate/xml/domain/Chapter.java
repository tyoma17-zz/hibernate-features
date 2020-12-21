package com.tyoma17.hibernate.xml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Chapter {

    private Long id;
    private String title;
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
