package com.tyoma17.hibernate.transaction.client;

import com.tyoma17.hibernate.transaction.domain.Message;
import com.tyoma17.hibernate.transaction.util.DbUtils;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityUpdateClientIT {

    @AfterEach
    void tearDown() {
        DbUtils.dropAllDbObjects();
    }

    @Test
    void testUpdateAndMerge() {
        DbUtils.createMessageTable();

        Session session = EntityUpdateClient.openSessionAndBeginTransaction();
        Message message = new Message("Hello, world!");
        session.save(message);
        EntityUpdateClient.commitTransactionAndCloseSession(session);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> EntityUpdateClient.updateMessage(session, message, "Amended text"));
        assertEquals("Session/EntityManager is closed", exception.getMessage());

        Session session2 = EntityUpdateClient.openSessionAndBeginTransaction();
        Message message2 = session2.get(Message.class, 1L);
        assertEquals("Hello, world!", message2.getText());
        EntityUpdateClient.updateMessage(session2, message2, "Amended text");
        EntityUpdateClient.commitTransactionAndCloseSession(session2);

        Message message3 = DbUtils.getMessage();
        assertEquals("Amended text", message3.getText());

        Session session3 = EntityUpdateClient.openSessionAndBeginTransaction();
        Message message4 = session3.get(Message.class, 1L);

        NonUniqueObjectException nonUniqueObjectException = assertThrows(NonUniqueObjectException.class,
                () -> EntityUpdateClient.updateMessage(session3, message3, "Amended text 2"));
        assertEquals("A different object with the same identifier value was already associated with the session : " +
                "[com.tyoma17.hibernate.transaction.domain.Message#1]", nonUniqueObjectException.getMessage());

        EntityUpdateClient.mergeMessage(session3, message3, "Amended text 3");
        EntityUpdateClient.commitTransactionAndCloseSession(session3);

        message4 = DbUtils.getMessage();
        assertEquals("Amended text 3", message4.getText());
    }
}