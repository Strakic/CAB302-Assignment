package com.trashslammers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreTest {

    @Test
    void newScoreStartsAtZero() {
        Score score = new Score();
        assertEquals(0, score.getValue());
    }

    @Test
    void canStartFromSavedAccountValue() {
        Score score = new Score(40);
        assertEquals(40, score.getValue());
    }

    @Test
    void cannotStartNegative(){
        assertThrows(IllegalArgumentException.class, () -> new Score(-1));
    }

    @Test
    void correctSortIncreasesScore(){
        Score score = new Score();
        score.addForCorrectSort();
        assertEquals(Score.CORRECT_SORT_POINTS, score.getValue());
    }

    @Test
    void buyAnAnimalDecreasesScore(){
        Score score = new Score(50);
        assertTrue(score.spendOnAnimal(20));
        assertEquals(30, score.getValue());
    }

    @Test
    void cannotSpendMoreThanCurrentScore(){
        Score score = new Score(10);
        assertFalse(score.spendOnAnimal(15));
        assertEquals(10, score.getValue());
    }

    @Test
    void cannotSpendNegativeCost(){
        Score score = new Score(10);
        assertThrows(IllegalArgumentException.class, () -> score.spendOnAnimal(-5));
    }

    @Test
    void scoreStaysConsistentAfterMixedActions(){
        Score score = new Score();
        score.addForCorrectSort();
        score.addForCorrectSort();
        assertTrue(score.spendOnAnimal(10));
        assertEquals(10, score.getValue());
    }
}
