package com.tyoma17.hibernate.embeddable.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "HOME_STREET")),
            @AttributeOverride(name = "city", column = @Column(name = "HOME_CITY")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "HOME_ZIPCODE"))
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET")),
            @AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "BILLING_ZIPCODE"))
    })
    private Address billingAddress;

    public Person(String name, Address homeAddress, Address billingAddress) {
        this.name = name;
        this.homeAddress = homeAddress;
        this.billingAddress = billingAddress;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                "id = " + id +
                ", name = '" + name + "'" +
                ", homeAddress = " + homeAddress +
                ", billingAddress = " + billingAddress
                + "]";
    }
}