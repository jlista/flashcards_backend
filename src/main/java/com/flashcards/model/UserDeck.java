package com.flashcards.model;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "user_deck")
public class UserDeck {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_deck_id", nullable = false, unique = true)
    private Long userDeckId;
    @Column(name = "deck_id", nullable = false, unique = false)
    private Long deckId;
    @Column(name = "owned_by", nullable = false, unique = false)
    private Long ownedBy;
    @Column(name = "created", nullable = false, unique = false)
    @CreationTimestamp
    private Timestamp created;
}
