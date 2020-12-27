package com.tyoma17.hibernate.detached.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
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

    @OneToMany(mappedBy = "guide", cascade = MERGE)
    private List<Student> students = new ArrayList<>();

    public Guide(String staffId, String name, Integer salary) {
        this.staffId = staffId;
        this.name = name;
        this.salary = salary;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setGuide(this); // without it saving via inverse end will not work!
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