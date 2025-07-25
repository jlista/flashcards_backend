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
    @Column(name = "user_deck_id")
    private Long userDeckId;
    @Column(name = "deck_id")
    private Long deckId;
    @Column(name = "owned_by")
    private Long ownedBy;
    @Column(name = "created")
    @CreationTimestamp
    private Timestamp created;
}
