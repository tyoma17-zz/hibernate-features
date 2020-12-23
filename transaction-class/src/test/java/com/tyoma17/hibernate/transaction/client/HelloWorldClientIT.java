package com.tyoma17.hibernate.transaction.client;

import com.tyoma17.hibernate.transaction.domain.Message;
import com.tyoma17.hibernate.transaction.util.DbUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldClientIT {

    @AfterEach
    void tearDown() {
        DbUtils.dropAllDbObjects();
    }

    @Test
    void testTransaction() {

        DbUtils.createMessageTable();
        DbUtils.saveMessage();

        Message message = DbUtils.getMessage();
        assertEquals(1L, message.getId());
        assertEquals("Hello, world!", message.getText());

        HelloWorldClient.changeMessageTextWithinTransaction("Amended text 1", true);
        message = DbUtils.getMessage();
        assertEquals(1L, message.getId());
        assertEquals("Hello, world!", message.getText());

        HelloWorldClient.changeMessageTextWithinTransaction("Amended text 2", false);
        message = DbUtils.getMessage();
        assertEquals(1L, message.getId());
        assertEquals("Amended text 2", message.getText());
    }
}