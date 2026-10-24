package com.trashslammers.model.qte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void claimInsideWindowSucceeds() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        event.tick(1.0);
        assertTrue(event.claim());
    }

    @Test
    void claimAfterExpiryFails() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        event.tick(3.5);
        assertFalse(event.claim());
    }

    @Test
    void claimTwiceOnlySucceedsOnce() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        assertTrue(event.claim());
        assertFalse(event.claim());
    }

    @Test
    void reportsTheWindowItWasGiven() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        assertEquals(3.0, event.windowSeconds(), 0.001);
    }

    @Test
    void claimedEventIsNoLongerActive() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        event.claim();
        assertFalse(event.isActive());
    }
}
