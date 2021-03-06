package com.tyoma17.hibernate.query_hints.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Guide {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.PERSIST)
    private List<Student> students = new ArrayList<>();

    public Guide(String name) {
        this.name = name;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setGuide(this);
    }
}