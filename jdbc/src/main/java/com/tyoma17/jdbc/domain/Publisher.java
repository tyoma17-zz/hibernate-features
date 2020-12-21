package com.tyoma17.jdbc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publisher {

    private String code;
    private String name;

    @Override
    public String toString() {
        return "Publisher [" +
                "code = " + code +
                ", name = " + name + "]";
    }
}