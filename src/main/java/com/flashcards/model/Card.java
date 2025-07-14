package com.flashcards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vocab")
public class Card {

    @Id
    private String id;
    private String hint;
    private String answer;
    private String last_correct;
    private String mastery_level;
    private String streak;

    public Card() {}

    public Card(String hint, String answer, String last_correct, String mastery_level, String streak ) {
        this.hint = hint;
        this.answer = answer;
        this.last_correct = last_correct;
        this.mastery_level = mastery_level;
        this.streak = streak;
    }

    public Card(String hint, String answer) {
        this.hint = hint;
        this.answer = answer;
        this.last_correct = null;
        this.mastery_level = "1";
        this.streak = "0";
    }

    public String getId() { return id; }
    public String getHint() { return hint; }
    public String getAnswer() { return answer; }
    public String getLastCorrect() { return last_correct; }
    public String getMasteryLevel() { return mastery_level; }
    public String getStreak() { return streak; }

    public void setId(String id) { this.id = id; }
    public void setHint(String hint) { this.hint = hint; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setLastCorrect(String last_correct) { this.last_correct = last_correct; }
    public void setMasteryLevel(String mastery_level) {this.mastery_level = mastery_level; }
    public void setStreak(String streak) { this.streak = streak; }
}