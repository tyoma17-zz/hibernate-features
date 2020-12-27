package com.tyoma17.inheritance.entity.single_table;

import javax.persistence.Entity;

@Entity
public class Cat_ST extends Animal_ST {

    @Override
    public String makeNoise() {
        return "meow meow";
    }
}