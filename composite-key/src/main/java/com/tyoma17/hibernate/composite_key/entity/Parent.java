package com.tyoma17.hibernate.composite_key.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Parent {

    @EmbeddedId
    private ParentPrimaryKey parentPrimaryKey;

    @OneToMany(mappedBy = "parent", cascade = PERSIST)
    private Set<Child> children = new HashSet<>();

    public Parent(ParentPrimaryKey parentPrimaryKey) {
        this.parentPrimaryKey = parentPrimaryKey;
    }

    public void addChild(Child child) {
        child.setParent(this);
        this.children.add(child);
    }

    @Override
    public String toString() {
        return "Parent{" +
                "firstname = " + parentPrimaryKey.getFirstname() +
                ", lastname = " + parentPrimaryKey.getLastname() +
                ", children = " + children +
                '}';
    }
}