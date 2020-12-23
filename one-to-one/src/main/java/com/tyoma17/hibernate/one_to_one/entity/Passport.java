package com.tyoma17.hibernate.one_to_one.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Passport {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "PASSPORT_NUMBER")
    private String passportNumber;

    @OneToOne(mappedBy = "passport")
    private Customer customer;

    public Passport(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "id = " + id +
                ", passportNumber = '" + passportNumber + '\'' +
                ", customerId = " + customer.getId() +
                '}';
    }
}
