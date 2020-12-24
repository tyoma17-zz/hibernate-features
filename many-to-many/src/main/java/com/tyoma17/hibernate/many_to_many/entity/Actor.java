package com.tyoma17.hibernate.many_to_many.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Actor {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "actors")
    private List<Movie> movies = new ArrayList<>();

    public Actor(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    //utility methods - synchronizing both sides (owner and inverse-end) of the relationship when you add and remove a Movie from an Actor
    public void addMovie(Movie movie) {
        movies.add(movie);
        movie.getActors().add(this);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movie.getActors().remove(this);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id = " + id +
                ", name = '" + name + "'" +
                ", moviesIds = " + movies.stream()
                .map(Movie::getId)
                .collect(Collectors.toList()) +
                '}';
    }
}