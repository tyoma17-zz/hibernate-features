package com.tyoma17.hibernate.composite_key.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Child {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "FIRSTNAME_FK", referencedColumnName = "FIRSTNAME"),
            @JoinColumn(name = "LASTNAME_FK", referencedColumnName = "LASTNAME")
    })
    private Parent parent;

    public Child(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id = " + id +
                ", name = " + name +
                '}';
    }
}