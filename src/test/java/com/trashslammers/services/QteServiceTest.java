package com.trashslammers.services;
import com.trashslammers.model.qte.GoldenTrashEvent;
import com.trashslammers.model.qte.QteReward;
import com.trashslammers.service.QteService;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void spawnsAgainAfterAnotherDelay() {
        QteService service = new QteService(new Random(42));
        service.tick(13.0);
        Optional<GoldenTrashEvent> second = service.tick(13.0);
        assertTrue(second.isPresent());
    }

    @Test
    void rewardValuesAreAllWorthWinning() {
        QteService service = new QteService(new Random(42));
        assertTrue(service.cashBonus() > 0);
        assertTrue(service.boostMultiplier() > 1);
        assertTrue(service.boostSeconds() > 0);
        assertTrue(service.burstSize() > 0);
    }

    @Test
    void rollingRewardsEventuallyGivesAllThreeTypes() {
        QteService service = new QteService(new Random(42));
        Set<QteReward> seen = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            seen.add(service.rollReward());
        }

        assertEquals(3, seen.size());
    }
}
