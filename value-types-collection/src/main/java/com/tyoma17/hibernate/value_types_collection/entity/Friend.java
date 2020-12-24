package com.tyoma17.hibernate.value_types_collection.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
public class Friend {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private String email;

    @ElementCollection
    @CollectionTable(name = "FRIEND_NICKNAMES", joinColumns = @JoinColumn(name = "FRIEND_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"FRIEND_ID", "NICKNAME"}))
    @Column(name = "NICKNAME")
    private List<String> nicknames = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name = "FRIEND_ADDRESSES", joinColumns = @JoinColumn(name = "FRIEND_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"ADDRESS_STREET",
                    "ADDRESS_CITY", "ADDRESS_ZIPCODE"}))
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "ADDRESS_STREET")),
            @AttributeOverride(name = "city", column = @Column(name = "ADDRESS_CITY")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "ADDRESS_ZIPCODE"))
    })
    private List<Address> addresses = new ArrayList<>();

    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Friend [ID = " + id + ", name = " + name + ", email = " + email + ", nicknames = " + nicknames +
                ", addresses = " + addresses + "]";
    }
}