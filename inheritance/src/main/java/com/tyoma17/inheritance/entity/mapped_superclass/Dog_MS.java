package com.tyoma17.inheritance.entity.mapped_superclass;

import javax.persistence.Entity;

@Entity
public class Dog_MS extends Animal_MS {

    @Override
    public String makeNoise() {
        return "woof woof";
    }
}