package com.tyoma17.inheritance.entity.table_per_class;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.TABLE;
import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
@Getter
@Setter
public abstract class Animal_TPC {


    @Id
    @GeneratedValue(strategy = TABLE)
    private Long id;

    @Column(nullable = false)
    private String name;

    public abstract String makeNoise();

    @Override
    public String toString() {
        return name + " making " + makeNoise() + " noises";
    }

}