package com.flashcards.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "vocab")
public class Card {

    @Id
    private String id;

    @Field("hint")
    private String hint;

    @Field("answer")
    private String answer;

    @Field("last_correct")
    private Date last_correct;

    @Field("mastery_level")
    private int mastery_level;

    @Field("streak")
    private int streak;

    private boolean isReadyToReview;

    public Card() {}

    public Card(String hint, String answer, Date last_correct, int mastery_level, int streak) {
        this.hint = hint;
        this.answer = answer;
        this.mastery_level = mastery_level;
        this.streak = streak;
        this.last_correct = last_correct;
    }

    public Card(String hint, String answer) {
        this.hint = hint;
        this.answer = answer;
        this.last_correct = null;
        this.mastery_level = 0;
        this.streak = 0;
    }

    public String getId() {
        return id;
    }

    public String getHint() {
        return hint;
    }

    public String getAnswer() {
        return answer;
    }

    public Date getLastCorrect() {
        return last_correct;
    }

    public int getMasteryLevel() {
        return mastery_level;
    }

    public int getStreak() {
        return streak;
    }

    public boolean getIsReadyToReview() {
        return checkIsReadyToReview();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setLastCorrect(Date last_correct) {
        this.last_correct = last_correct;
    }

    public void setMasteryLevel(int mastery_level) {
        this.mastery_level = mastery_level;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void setIsReadyToReview(boolean isReadyToReview) {
        this.isReadyToReview = isReadyToReview;
    }

    @Override
    public String toString() {
        String dateStr = "";
        if (this.last_correct != null) {
            dateStr = last_correct.toString();
        }
        return String.format(
                "id: %1$s, hint: %2$s, answer: %3$s, lastCorrect: %4$s, mastery: %5$d, streak: %6$d",
                this.id, this.hint, this.answer, dateStr, this.mastery_level, this.streak);
    }

    private static Date dateToUTC(Date date){
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

    private boolean checkIsReadyToReview() {

        if (getMasteryLevel() == 0) {
            return true;
        }

        int targetDays;
        switch (getMasteryLevel()) {
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
        return getLastCorrect().compareTo(dateToUTC(targetDate)) <= 0;
    }
}
