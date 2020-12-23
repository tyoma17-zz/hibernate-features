package com.tyoma17.hibernate.embeddable.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    private String street;
    private String city;
    private String zipcode;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                "street = '" + street + "'" +
                ", city = '" + city + "'" +
                ", zipcode = '" + zipcode + "'" +
                "]";
    }
}