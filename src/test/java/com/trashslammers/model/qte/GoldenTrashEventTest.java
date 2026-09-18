package com.trashslammers.model.qte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GoldenTrashEventTest {

    @Test
    void newEventIsActive() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        assertTrue(event.isActive());
    }

    @Test
    void eventStillActiveInItsWindow() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        event.tick(2.0);
        assertTrue(event.isActive());
    }

    @Test
    void eventExpiresOnceWindowPasses() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        event.tick(3.5);
        assertFalse(event.isActive());
    }
}
