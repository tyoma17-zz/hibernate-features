package com.tyoma17.hibernate.maps_id.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    // @GeneratedValue(strategy = IDENTITY) <-- no need because of @MapsId
    private Long id;

    private String name;

    @OneToOne(cascade = PERSIST)
    @JoinColumn(name = "PASSPORT_ID", unique = true)
    @MapsId
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