package com.tyoma17.hibernate.many_to_one.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Guide {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "STAFF_ID", nullable = false)
    private String staffId;
    private String name;
    private Integer salary;

    public Guide(String staffId, String name, Integer salary) {
        this.staffId = staffId;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                "ID = " + id +
                ", staffId = '" + staffId + "'" +
                ", name = '" + name + "'" +
                ", salary = " + salary +
                "]";
    }
}