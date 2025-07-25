package com.flashcards.model;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Deck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private Long deckId;
    @Column(name = "deck_name")
    private String deckName;
    @Column(name = "description")
    private String description;
    @Column(name = "owned_by")
    private Long ownedBy;
    @Column(name = "created")
    @CreationTimestamp
    private Timestamp created;
}
