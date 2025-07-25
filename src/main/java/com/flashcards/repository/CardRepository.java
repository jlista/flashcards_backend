package com.flashcards.repository;

import com.flashcards.model.Card;
import com.flashcards.model.CardDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardId(Long cardId);


    @Query("SELECT new com.flashcards.model.Card(c.cardId, c.clue, c.answer, c.created, c.deckId, c.ownedBy) " +
           "FROM Card c JOIN Deck d ON c.deckId = d.deckId " +
           "WHERE d.deckId = :id")
    List<Card> getAllCardsInDeck(@Param("id") Long deckId);

    @Query("SELECT new com.flashcards.model.CardDTO(c.cardId, c.clue, c.answer, d.masteryLevel, d.streak, d.lastCorrect, d.isFlagged) " +
           "FROM Card c JOIN DeckCard d ON c.cardId = d.cardId " +
           "WHERE d.userDeckId = :id")
    List<CardDTO> getAllCardsInUserDeck(@Param("id") Long userDeckId);

}
