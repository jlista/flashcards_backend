package com.flashcards.repository;

import com.flashcards.model.DeckCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckCardRepository extends JpaRepository<DeckCard, Long> {

    @Query("SELECT new com.flashcards.model.DeckCard(cardId, userDeckId, lastCorrect, masteryLevel, streak, isFlagged, created) "
            + "FROM DeckCard c " 
            + "WHERE c.userDeckId = :userDeckId and c.cardId = :cardId")
    Optional<DeckCard> findByCardAndDeckId(@Param("cardId") Long cardId,
            @Param("userDeckId") Long userDeckId);

    @Query("SELECT new com.flashcards.model.DeckCard(cardId, userDeckId, lastCorrect, masteryLevel, streak, isFlagged, created) "
            + "FROM DeckCard c " 
            + "WHERE c.userDeckId = :userDeckId")
    List<DeckCard> findByUserDeckId(@Param("userDeckId") Long userDeckId);

}
