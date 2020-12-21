package com.tyoma17.jdbc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {

    private String title;
    private Integer chapterNumber;

    @Override
    public String toString() {
        return "Chapter [" +
                "title = " + title +
                ", chapterNumber = " + chapterNumber + "]";
    }
}
