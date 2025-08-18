package com.flashcards.model;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "deck")
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id", nullable = false, unique = true)
    private Long deckId;
    @Column(name = "deck_name", length = 255, nullable = false, unique = false)
    private String deckName;
    @Column(name = "deck_desc", length = 511, nullable = true, unique = false)
    private String description;
    @Column(name = "owned_by", nullable = false, unique = false)
    private Long ownedBy;
    @Column(name = "is_public", nullable = false, unique = false)
    private boolean isPublic;
    @Column(name = "cloned_from", nullable = true, unique = false)
    private Long clonedFrom;
    @Column(name = "created", nullable = false, unique = false)
    @CreationTimestamp
    private Timestamp created;
    @Column(name = "is_enabled", nullable = false, unique = false)
    private boolean enabled = true;
}
