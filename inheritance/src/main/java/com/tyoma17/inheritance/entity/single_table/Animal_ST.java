package com.tyoma17.inheritance.entity.single_table;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Inheritance
@Getter
@Setter
public abstract class Animal_ST {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    public abstract String makeNoise();

    @Override
    public String toString() {
        return name + " making " + makeNoise() + " noises";
    }
}