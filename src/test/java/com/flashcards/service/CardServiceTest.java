package com.flashcards.service;

import com.flashcards.model.Card;
import com.flashcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    private CardRepository cardRepository;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        cardService = new CardService(cardRepository);
    }

    @Test
    void testGetAllCards() {
        List<Card> mockCards = Arrays.asList(
            new Card("Front", "Back")
        );

        when(cardRepository.findAll()).thenReturn(mockCards);

        List<Card> result = cardService.getAllCards();

        assertEquals(1, result.size());
        assertEquals("Front", result.get(0).getHint());
    }

    @Test
    void testGetCardByIdFound() {
        Card card = new Card("Front", "Back");
        card.setId("123");

        when(cardRepository.findById("123")).thenReturn(Optional.of(card));

        Card result = cardService.getCardById("123");

        assertNotNull(result);
        assertEquals("Front", result.getHint());
    }

    @Test
    void testGetCardByIdNotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            cardService.getCardById("foo");
        });
    }

    @Test
    void testUpdateCardStreakIncorrect() {

        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card = new Card("Hint", "Answer", startDate, 1, 1);
        card.setId("123");

        when(cardRepository.findById("123")).thenReturn(Optional.of(card));

        Card result = cardService.getCardById("123");
        cardService.updateCardStreak("123", false);

        assertEquals(0, result.getStreak());
        assertEquals(0, result.getMasteryLevel());
        assertEquals(startDate, result.getLastCorrect());
    }

    @Test
    void testUpdateCardStreakCorrect() {

        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card = new Card("Hint", "Answer", startDate, 1, 1);
        card.setId("123");

        when(cardRepository.findById("123")).thenReturn(Optional.of(card));

        Card result = cardService.getCardById("123");
        cardService.updateCardStreak("123", true);

        assertEquals(2, result.getStreak());
        assertEquals(1, result.getMasteryLevel());
        assertTrue(result.getLastCorrect().compareTo(startDate) > 0);
    }

    @Test
    void testUpdateCardStreakLevelUp() {

        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card = new Card("Hint", "Answer", startDate, 1, 4);
        card.setId("123");

        when(cardRepository.findById("123")).thenReturn(Optional.of(card));

        Card result = cardService.getCardById("123");
        cardService.updateCardStreak("123", true);

        assertEquals(5, result.getStreak());
        assertEquals(2, result.getMasteryLevel());
        assertTrue(result.getLastCorrect().compareTo(startDate) > 0);

    }
}