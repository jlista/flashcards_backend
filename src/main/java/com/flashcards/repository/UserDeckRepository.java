package com.flashcards.repository;

import com.flashcards.model.UserDeck;
import com.flashcards.model.DTO.UserDeckDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDeckRepository extends JpaRepository<UserDeck, Long> {


    @Query("SELECT new com.flashcards.model.DTO.UserDeckDTO(ud.userDeckId, ud.deckId, ud.ownedBy, d.deckName, d.description, d.isPublic) "
            + "FROM Deck d JOIN UserDeck ud ON ud.deckId = d.deckId "
            + "WHERE ud.ownedBy = :userId")
    public List<UserDeckDTO> findByOwnedBy(@Param("userId") Long userId);

    @Query("Select ud.ownedBy "
            + "FROM UserDeck ud "
            + "WHERE ud.userDeckId = :userDeckId")
    public Optional<Long> findOwner(@Param("userDeckId") long userDeckId);

    @Query("SELECT new com.flashcards.model.DTO.UserDeckDTO(ud.userDeckId, ud.deckId, ud.ownedBy, d.deckName, d.description, d.isPublic) "
            + "FROM Deck d JOIN UserDeck ud ON ud.deckId = d.deckId "
            + "WHERE ud.ownedBy = :userId AND ud.deckId = :deckId")
    public Optional<UserDeckDTO> findByUserAndDeck(@Param("userId") Long userId, @Param("deckId") Long deckId);

}
