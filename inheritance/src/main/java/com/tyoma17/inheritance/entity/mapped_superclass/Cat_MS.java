package com.tyoma17.inheritance.entity.mapped_superclass;

import javax.persistence.Entity;

@Entity
public class Cat_MS extends Animal_MS {

    @Override
    public String makeNoise() {
        return "meow meow";
    }
}