package com.tyoma17.inheritance.entity.joined;

import javax.persistence.Entity;

@Entity
public class Cat_J extends Animal_J {

    @Override
    public String makeNoise() {
        return "meow meow";
    }
}