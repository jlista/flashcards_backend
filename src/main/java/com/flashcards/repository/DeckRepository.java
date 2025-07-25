package com.flashcards.repository;

import com.flashcards.model.Deck;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    @Query("Select u.userDeckId " +
           "FROM Deck d JOIN UserDeck u on d.deckId = u.deckId " +
           "WHERE d.deckId = :id" )
    List<Long> getAssociatedUserDeckIds(@Param("id") Long deckId);
}
