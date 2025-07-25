package com.flashcards.service;

import com.flashcards.model.Card;
import com.flashcards.model.CardDTO;
import com.flashcards.model.DeckCard;
import com.flashcards.repository.CardRepository;
import com.flashcards.repository.DeckCardRepository;
import com.flashcards.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
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
    private DeckRepository deckRepository;
    private DeckCardRepository deckCardRepository;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        deckRepository = mock(DeckRepository.class);
        deckCardRepository = mock(DeckCardRepository.class);
        cardService = new CardService(cardRepository, deckCardRepository, deckRepository);
    }

    // @Test
    // void testGetAllCards() {
    //     List<CardDTO> mockCards = Arrays.asList(new Card("Front", "Back"));

    //     when(cardRepository.getAllCardsInUserDeck(1l)).thenReturn(mockCards);

    //     List<Card> result = cardService.getAllCards();

    //     assertEquals(1, result.size());
    //     assertEquals("Front", result.get(0).getHint());
    // }

    @Test
    void testGetCardByIdFound() {
        Card card = new Card("Front", "Back", 1l, 1l);
        card.setCardId(123L);

        when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(123L);

        assertNotNull(result);
        assertEquals("Front", result.getClue());
    }

    @Test
    void testGetCardByIdNotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            cardService.getCardById(123L);
        });
    }

    // @Test
    // void testGetRandomCards() {
    //     Timestamp daysAgo0 = Timestamp.from(Instant.now().minus(0, ChronoUnit.DAYS));
    //     Timestamp daysAgo1 = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));

    //     CardDTO card1 = new CardDTO("Hint1", "Answer1", 0, 1, daysAgo0);
    //     card1.setCardId(0L);
    //     CardDTO card2 = new CardDTO("Hint2", "Answer2", 1, 5, daysAgo1);
    //     card2.setCardId(1L);

    //     List<CardDTO> mockCards = Arrays.asList(card1, card2);

    //     when(cardRepository.getAllCardsInUserDeck(1l)).thenReturn(mockCards);
    //     // when(deckCardRepository.findByCardAndDeckId(0L, 1l)).thenReturn(Optional.of(card1));
    //     // when(cardRepository.findById(1L)).thenReturn(Optional.of(card2));

    //     // Assuming we just answered card 000, the only valid choice is 001 since 000 would be twice
    //     // in a row
    //     Optional<CardDTO> firstCardSelected = cardService.getRandomCardSR(0l, 1l);
    //     assertEquals(firstCardSelected.get().getCardId(), 1L);
    //}

    @Test
    void testUpdateCardStreakIncorrect() {

        Timestamp startDate = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));

        DeckCard card = new DeckCard(123l,0l);
        card.setMasteryLevel(2);
        card.setStreak(3);
        card.setLastCorrect(startDate);

        when(deckCardRepository.findByCardAndDeckId(123L, 0l)).thenReturn(Optional.of(card));

        cardService.updateCardStreak(123L, 0l, false);
        DeckCard result = deckCardRepository.findByCardAndDeckId(123l,0l).get();

        assertEquals(0, result.getStreak());
        assertEquals(1, result.getMasteryLevel());
        assertEquals(startDate, result.getLastCorrect());
    }

    // @Test
    // void testUpdateCardStreakCorrect() {

    //     Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

    //     Card card = new Card("Hint", "Answer", startDate, 1, 1);
    //     card.setId(123L);

    //     when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

    //     Card result = cardService.getCardById(123L);
    //     cardService.updateCardStreak(123L, true);

    //     assertEquals(2, result.getStreak());
    //     assertEquals(1, result.getMasteryLevel());
    //     assertTrue(result.getLastCorrect().compareTo(startDate) > 0);
    // }

    // @Test
    // void testUpdateCardStreakLevelUp() {

    //     Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

    //     Card card = new Card("Hint", "Answer", startDate, 1, 4);
    //     card.setId(123L);

    //     when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

    //     Card result = cardService.getCardById(123L);
    //     cardService.updateCardStreak(123L, true);

    //     assertEquals(5, result.getStreak());
    //     assertEquals(2, result.getMasteryLevel());
    //     assertTrue(result.getLastCorrect().compareTo(startDate) > 0);
    // }

    // @Test
    // void testIsCardReady() {
    //     Date today = Date.from(Instant.now().minus(0, ChronoUnit.DAYS));
    //     Date yesterday = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

    //     Card card1 = new Card("hint", "answer", today, 0, 0);
    //     Card card2 = new Card("hint", "answer", today, 1, 5);
    //     Card card3 = new Card("hint", "answer", yesterday, 0, 0);
    //     Card card4 = new Card("hint", "answer", yesterday, 1, 5);

    //     assertEquals(true, card1.getIsReadyToReview());
    //     assertEquals(false, card2.getIsReadyToReview());
    //     assertEquals(true, card3.getIsReadyToReview());
    //     assertEquals(true, card4.getIsReadyToReview());

    // }
}
