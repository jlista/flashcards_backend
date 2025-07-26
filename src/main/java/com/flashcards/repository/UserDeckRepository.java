package com.flashcards.repository;

import com.flashcards.model.UserDeck;
import com.flashcards.model.DTO.UserDeckDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDeckRepository extends JpaRepository<UserDeck, Long> {


    @Query("SELECT new com.flashcards.model.DTO.UserDeckDTO(ud.userDeckId, ud.deckId, ud.ownedBy, d.deckName, d.description) "
            + "FROM Deck d JOIN UserDeck ud ON ud.deckId = d.deckId "
            + "WHERE ud.ownedBy = :userId")
    public List<UserDeckDTO> findByOwnedBy(@Param("userId") Long userId);
}
