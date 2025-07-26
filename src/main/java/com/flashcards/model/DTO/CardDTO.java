package com.flashcards.model.DTO;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CardDTO {

    private Long cardId;
    private String clue;
    private String answer;
    private int masteryLevel;
    private int streak;
    private Timestamp lastCorrect;
    private boolean isFlagged;

    public CardDTO(String clue, String answer, int mastery_level, int streak, Timestamp lastCorrect) {
        this.clue = clue;
        this.answer = answer;
        this.masteryLevel = mastery_level;
        this.streak = streak;
        this.lastCorrect = lastCorrect;
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
        } else if (masteryLevel == 0) {
            return 5 - streak;
        }
        return 1;
    }
}
