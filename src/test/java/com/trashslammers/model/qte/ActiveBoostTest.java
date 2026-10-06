package com.trashslammers.model.qte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActiveBoostTest {

    @Test
    void noBoostMeansMultiplierOfOne() {
        ActiveBoost boost = new ActiveBoost();
        assertEquals(1, boost.currentMultiplier());
    }

    @Test
    void activatingSetsTheMultiplier() {
        ActiveBoost boost = new ActiveBoost();
        boost.activate(2, 10.0);
        assertEquals(2, boost.currentMultiplier());
    }

    @Test
    void boostExpiresAfterItsDuration() {
        ActiveBoost boost = new ActiveBoost();
        boost.activate(2, 10.0);
        boost.tick(11.0);
        assertEquals(1, boost.currentMultiplier());
    }

    @Test
    void reactivatingRefreshesTheDuration() {
        ActiveBoost boost = new ActiveBoost();
        boost.activate(2, 10.0);
        boost.tick(8.0);
        boost.activate(2, 10.0);
        boost.tick(5.0);
        assertEquals(2, boost.currentMultiplier());
    }
}
