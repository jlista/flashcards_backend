package com.flashcards.service;

import com.flashcards.model.AppUser;
import com.flashcards.model.Card;
import com.flashcards.model.DeckCard;
import com.flashcards.model.UserDeck;
import com.flashcards.model.DTO.CardDTO;
import com.flashcards.repository.AppUserRepository;
import com.flashcards.repository.CardRepository;
import com.flashcards.repository.DeckCardRepository;
import com.flashcards.repository.DeckRepository;
import com.flashcards.repository.UserDeckRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserDeckRepository userDeckRepository;
    private AppUserRepository appUserRepository;
    private AuthenticationService authService;
    private CardService cardService;


    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        deckRepository = mock(DeckRepository.class);
        deckCardRepository = mock(DeckCardRepository.class);
        userDeckRepository = mock(UserDeckRepository.class);
        appUserRepository = mock(AppUserRepository.class);
        authService = new AuthenticationService(appUserRepository, null, null);
        cardService = new CardService(cardRepository, deckCardRepository, deckRepository,
                userDeckRepository, authService);
    }

    private void setupAuthenticationTest() {
        AppUser userDetails = new AppUser();
        userDetails.setUserId(1l);
        userDetails.setUsername("test");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUser testUser = new AppUser();
        testUser.set_admin(true);
        when(appUserRepository.findByUsername("test")).thenReturn(Optional.of(testUser));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void testGetAllCards() {
        setupAuthenticationTest();

        List<CardDTO> mockCards = Arrays.asList(new CardDTO("Clue", "Answer", 0, 0, null));

        when(cardRepository.getAllCardsInUserDeck(1l)).thenReturn(mockCards);
        when(userDeckRepository.findOwner(1l)).thenReturn(Optional.of(1l));

        List<CardDTO> result = cardService.getAllCardsInUserDeck(1l);

        assertEquals(1, result.size());
        assertEquals("Clue", result.get(0).getClue());
    }

    @Test
    void testGetCardByIdFound() {
        setupAuthenticationTest();
        Card card = new Card("Front", "Back", 1l, 1l);
        card.setCardId(123L);

        when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(123L);

        assertNotNull(result);
        assertEquals("Front", result.getClue());
    }

    @Test
    void testGetCardByIdNotFound() {
        setupAuthenticationTest();
        assertThrows(NoSuchElementException.class, () -> {
            cardService.getCardById(123L);
        });
    }

    @Test
    void testGetRandomCards() {
        setupAuthenticationTest();
        Timestamp daysAgo0 = Timestamp.from(Instant.now().minus(0, ChronoUnit.DAYS));
        Timestamp daysAgo1 = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));

        CardDTO card1 = new CardDTO("Hint1", "Answer1", 0, 1, daysAgo0);
        card1.setCardId(0L);
        CardDTO card2 = new CardDTO("Hint2", "Answer2", 1, 5, daysAgo1);
        card2.setCardId(1L);

        List<CardDTO> mockCards = Arrays.asList(card1, card2);

        when(cardRepository.getAllCardsInUserDeck(1l)).thenReturn(mockCards);
        when(userDeckRepository.findOwner(1l)).thenReturn(Optional.of(1l));

        // Assuming we just answered card 0, the only valid choice is 1 since 0 would be twice in a
        // row
        Optional<CardDTO> firstCardSelected = cardService.getRandomCardSR(0l, 1l);
        assertEquals(firstCardSelected.get().getCardId(), 1L);
    }

    @Test
    void testUpdateCardStreakIncorrect() {
        setupAuthenticationTest();
        Timestamp startDate = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));

        DeckCard dc = new DeckCard(0l, 1l, startDate, 2, 1, false, null);

        when(deckCardRepository.findByCardAndDeckId(0l, 1l)).thenReturn(Optional.of(dc));
        when(userDeckRepository.findOwner(1l)).thenReturn(Optional.of(1l));

        cardService.updateCardStreak(0l, 1l, false);

        assertEquals(0, dc.getStreak());
        assertEquals(1, dc.getMasteryLevel());
        assertEquals(dc.getLastCorrect(), startDate);
    }

    @Test
    void testUpdateCardStreakCorrect() {
        setupAuthenticationTest();
        Timestamp startDate = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));

        DeckCard dc = new DeckCard(0l, 1l, startDate, 1, 1, false, null);

        when(deckCardRepository.findByCardAndDeckId(0l, 1l)).thenReturn(Optional.of(dc));
        when(userDeckRepository.findOwner(1l)).thenReturn(Optional.of(1l));
        cardService.updateCardStreak(0l, 1l, true);

        assertEquals(2, dc.getStreak());
        assertEquals(1, dc.getMasteryLevel());
        assertTrue(dc.getLastCorrect().compareTo(startDate) > 0);
    }

    @Test
    void testUpdateCardStreakLevelUp() {
        setupAuthenticationTest();
        Timestamp startDate = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));

        DeckCard dc = new DeckCard(0l, 1l, startDate, 1, 4, false, null);

        when(deckCardRepository.findByCardAndDeckId(0l, 1l)).thenReturn(Optional.of(dc));
        when(userDeckRepository.findOwner(1l)).thenReturn(Optional.of(1l));
        cardService.updateCardStreak(0l, 1l, true);

        assertEquals(5, dc.getStreak());
        assertEquals(2, dc.getMasteryLevel());
        assertTrue(dc.getLastCorrect().compareTo(startDate) > 0);
    }

    // @Test
    // void testIsCardReady() {
    //     Timestamp today = Timestamp.from(Instant.now().minus(0, ChronoUnit.DAYS));
    //     Timestamp yesterday = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));

    //     DeckCard card1 = new DeckCard(today, 0, 0);
    //     DeckCard card2 = new DeckCard(today, 1, 5);
    //     DeckCard card3 = new DeckCard(yesterday, 0, 0);
    //     DeckCard card4 = new DeckCard(yesterday, 1, 5);

    //     assertEquals(true, card1.getIsReadyToReview());
    //     assertEquals(false, card2.getIsReadyToReview());
    //     assertEquals(true, card3.getIsReadyToReview());
    //     assertEquals(true, card4.getIsReadyToReview());
    // }
}
