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
@Table(name = "PUBLISHER")
public class Publisher {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
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