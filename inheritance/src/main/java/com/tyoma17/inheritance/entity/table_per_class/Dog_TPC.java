package com.tyoma17.inheritance.entity.table_per_class;

import javax.persistence.Entity;

@Entity
public class Dog_TPC extends Animal_TPC {

    @Override
    public String makeNoise() {
        return "woof woof";
    }
}