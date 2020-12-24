package com.tyoma17.hibernate.many_to_many.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = PERSIST)
    @JoinTable(
            name = "MOVIE_ACTOR",
            joinColumns = {@JoinColumn(name = "MOVIE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ACTOR_ID")}
    )
    private List<Actor> actors = new ArrayList<>();

    public Movie(String name) {
        this.name = name;
    }

    public List<Actor> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id = " + id +
                ", name = '" + name + "'" +
                ", actors = " + actors +
                '}';
    }
}