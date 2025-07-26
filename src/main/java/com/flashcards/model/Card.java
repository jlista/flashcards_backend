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
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Card {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id", nullable = false, unique = true)
    private Long cardId;
    @Column(name = "clue", length = 255, nullable = false, unique = false)
    private String clue;
    @Column(name = "answer", length = 511, nullable = false, unique = false)
    private String answer;
    @Column(name = "created", nullable = false, unique = false)
    @CreationTimestamp
    private Timestamp created;
    @Column(name = "deck_id", nullable = false, unique = false)
    private Long deckId;
    @Column(name = "owned_by", nullable = false, unique = false)
    private Long ownedBy;

    public Card(String clue, String answer, Long deckId, Long ownedBy){
        this.clue = clue;
        this.answer = answer;
        this.deckId = deckId;
        this.ownedBy = ownedBy;
    }
}
