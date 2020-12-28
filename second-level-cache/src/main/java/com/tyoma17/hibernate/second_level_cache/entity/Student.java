package com.tyoma17.hibernate.second_level_cache.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Cacheable
@NoArgsConstructor
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "ENROLLMENT_ID", nullable = false)
    private String enrollmentId;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "GUIDE_ID")
    private Guide guide;



    public Student(String enrollmentId, String name, Guide guide) {
        this.enrollmentId = enrollmentId;
        this.name = name;
        this.guide = guide;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                "ID = " + id +
                ", enrollmentId = '" + enrollmentId + "'" +
                ", name = '" + name + "'" +
                "]";
    }
}