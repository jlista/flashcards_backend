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
        List<Card> mockCards = Arrays.asList(new Card("Front", "Back"));

        when(cardRepository.findAll()).thenReturn(mockCards);

        List<Card> result = cardService.getAllCards();

        assertEquals(1, result.size());
        assertEquals("Front", result.get(0).getHint());
    }

    @Test
    void testGetCardByIdFound() {
        Card card = new Card("Front", "Back");
        card.setId(123L);

        when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(123L);

        assertNotNull(result);
        assertEquals("Front", result.getHint());
    }

    @Test
    void testGetCardByIdNotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            cardService.getCardById(123L);
        });
    }

    @Test
    void testGetRandomCards() {
        Date daysAgo0 = Date.from(Instant.now().minus(0, ChronoUnit.DAYS));
        Date daysAgo1 = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card1 = new Card("Hint1", "Answer1", daysAgo0, 0, 1);
        card1.setId(0L);
        Card card2 = new Card("Hint2", "Answer2", daysAgo1, 1, 5);
        card2.setId(1L);

        List<Card> mockCards = Arrays.asList(card1, card2);

        when(cardRepository.findAll()).thenReturn(mockCards);
        when(cardRepository.findById(0L)).thenReturn(Optional.of(card1));
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card2));

        // Assuming we just answered card 000, the only valid choice is 001 since 000 would be twice
        // in a row
        Optional<Card> firstCardSelected = cardService.getRandomCardSR(0L);
        assertEquals(firstCardSelected.get().getId(), 1L);
    }

    @Test
    void testUpdateCardStreakIncorrect() {

        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card = new Card("Hint", "Answer", startDate, 2, 1);
        card.setId(123L);

        when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(123L);
        cardService.updateCardStreak(123L, false);

        assertEquals(0, result.getStreak());
        assertEquals(1, result.getMasteryLevel());
        assertEquals(startDate, result.getLastCorrect());
    }

    @Test
    void testUpdateCardStreakCorrect() {

        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card = new Card("Hint", "Answer", startDate, 1, 1);
        card.setId(123L);

        when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(123L);
        cardService.updateCardStreak(123L, true);

        assertEquals(2, result.getStreak());
        assertEquals(1, result.getMasteryLevel());
        assertTrue(result.getLastCorrect().compareTo(startDate) > 0);
    }

    @Test
    void testUpdateCardStreakLevelUp() {

        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card = new Card("Hint", "Answer", startDate, 1, 4);
        card.setId(123L);

        when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(123L);
        cardService.updateCardStreak(123L, true);

        assertEquals(5, result.getStreak());
        assertEquals(2, result.getMasteryLevel());
        assertTrue(result.getLastCorrect().compareTo(startDate) > 0);
    }

    @Test
    void testIsCardReady() {
        Date today = Date.from(Instant.now().minus(0, ChronoUnit.DAYS));
        Date yesterday = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        Card card1 = new Card("hint", "answer", today, 0, 0);
        Card card2 = new Card("hint", "answer", today, 1, 5);
        Card card3 = new Card("hint", "answer", yesterday, 0, 0);
        Card card4 = new Card("hint", "answer", yesterday, 1, 5);

        assertEquals(true, card1.getIsReadyToReview());
        assertEquals(false, card2.getIsReadyToReview());
        assertEquals(true, card3.getIsReadyToReview());
        assertEquals(true, card4.getIsReadyToReview());

    }
}
