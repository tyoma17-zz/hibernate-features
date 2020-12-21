package com.tyoma17.hibernate.xml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Publisher {

    private Long id;
    private String code;
    private String name;

    public Publisher(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Publisher [" +
                "id = " + id +
                ", code = " + code +
                ", name = " + name + "]";
    }
}