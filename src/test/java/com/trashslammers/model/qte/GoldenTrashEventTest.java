package com.trashslammers.model.qte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoldenTrashEventTest {

    @Test
    void newEventIsActive() {
        GoldenTrashEvent event = new GoldenTrashEvent(3.0);
        assertTrue(event.isActive());
    }
}
