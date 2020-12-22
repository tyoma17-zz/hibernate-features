package com.tyoma17.hibernate.transaction.client;

import com.tyoma17.hibernate.transaction.domain.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldClientIT {

    @Test
    void testTransaction() {

        HelloWorldClient.saveMessage();
        Message message = HelloWorldClient.getMessage();
        assertEquals(1L, message.getId());
        assertEquals("Hello, world!", message.getText());

        HelloWorldClient.changeMessageTextWithinTransaction("Amended text 1", true);
        message = HelloWorldClient.getMessage();
        assertEquals(1L, message.getId());
        assertEquals("Hello, world!", message.getText());

        HelloWorldClient.changeMessageTextWithinTransaction("Amended text 2", false);
        message = HelloWorldClient.getMessage();
        assertEquals(1L, message.getId());
        assertEquals("Amended text 2", message.getText());
    }
}