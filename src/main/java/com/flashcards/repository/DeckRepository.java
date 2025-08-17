package com.flashcards.repository;

import com.flashcards.model.Deck;
import com.flashcards.model.DTO.DeckDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    @Query("Select u.userDeckId " 
            + "FROM Deck d JOIN UserDeck u on d.deckId = u.deckId "
            + "WHERE d.deckId = :id")
    List<Long> getAssociatedUserDeckIds(@Param("id") Long deckId);

    @Query("Select new com.flashcards.model.DTO.DeckDTO(deckId, deckName, description, isPublic) " 
          +"from Deck d "
          +"where d.isPublic = true")
    List<DeckDTO> getPublicDecks();

    @Query("Select new com.flashcards.model.DTO.DeckDTO(deckId, deckName, description, isPublic) " 
          +"from Deck d "
          +"where d.isPublic = true "
          +"and d.deckId not in ( "
          +"Select u.deckId from Deck d join UserDeck u "
          +"on u.deckId = d.deckId "
          +"where u.ownedBy = :userId)" )
    List<DeckDTO> getPublicDecksNotOwned(@Param("userId") Long userId);
}
