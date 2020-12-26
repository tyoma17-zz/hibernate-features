package com.tyoma17.hibernate.entity_manager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String text;

    public Message(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message [id = " + id + ", text = " + text + "]";
    }
}