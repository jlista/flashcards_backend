package com.flashcards.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@IdClass(DeckCardPK.class)
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "deck_card")
public class DeckCard {

	@Id
    @Column(name = "card_id")
    private Long cardId;
    @Id
    @Column(name = "user_deck_id")
    private Long userDeckId;
    @Column(name = "last_correct")
    private Timestamp lastCorrect;
    @Column(name = "mastery_level")
    private int masteryLevel;
    @Column(name = "streak")
    private int streak;
    @Column(name = "is_flagged")
    private boolean isFlagged;
    @Column(name = "created")
    @CreationTimestamp
    private Timestamp created;
    @Transient
    private boolean isReadyToReview;
    @Transient 
    private int priority;

    public DeckCard(long cardId, long userDeckId) {
        this.cardId = cardId;
        this.userDeckId = userDeckId;
    }

    public boolean getIsReadyToReview() {

        if (isFlagged || masteryLevel == 0) {
            return true;
        }

        int targetDays;
        switch (masteryLevel) {
            case 1:
                targetDays = 1;
                break;
            case 2:
                targetDays = 2;
                break;
            case 3:
                targetDays = 5;
                break;
            default:
                targetDays = 10;
                break;
        }

        Date targetDate = Date.from(Instant.now().minus(targetDays, ChronoUnit.DAYS));
        return lastCorrect.compareTo(targetDate) <= 0;
    }

    public int getPriority() {
        if (isFlagged) {
            return 10;
        }
        else if (masteryLevel == 0){
            return 5 - streak;
        }
        return 1;
    }
}
