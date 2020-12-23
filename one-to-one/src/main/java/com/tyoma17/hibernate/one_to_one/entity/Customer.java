package com.tyoma17.hibernate.one_to_one.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = {PERSIST})
    @JoinColumn(name = "PASSPORT_ID", unique = true)
    private Passport passport;

    public Customer(String name, Passport passport) {
        this.name = name;
        this.passport = passport;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", passport = " + passport +
                '}';
    }
}
