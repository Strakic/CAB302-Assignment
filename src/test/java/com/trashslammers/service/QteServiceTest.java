package com.trashslammers.service;
import com.trashslammers.model.qte.GoldenTrashEvent;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QteServiceTest {

    @Test
    void noSpawnBeforeDelayElapses() {
        QteService service = new QteService(new Random(42));
        Optional<GoldenTrashEvent> spawned = service.tick(1.0);
        assertTrue(spawned.isEmpty());
    }

    @Test
    void spawnsOnceDelayElapses() {
        QteService service = new QteService(new Random(42));
        Optional<GoldenTrashEvent> spawned = service.tick(13.0);
        assertTrue(spawned.isPresent());
    }
}
