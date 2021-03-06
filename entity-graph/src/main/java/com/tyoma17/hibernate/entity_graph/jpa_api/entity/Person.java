package com.tyoma17.hibernate.entity_graph.jpa_api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "person", cascade = PERSIST)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = PERSIST)
    private Set<Dog> dogs = new HashSet<>();

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addresses=" + addresses +
                ", dogs=" + dogs +
                '}';
    }
}