package com.flashcards.model;

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

    public Card() {}

    public Card(String hint, String answer, Date last_correct, int mastery_level, int streak ) {
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

    public String getId() { return id; }
    public String getHint() { return hint; }
    public String getAnswer() { return answer; }
    public Date getLastCorrect() { return last_correct; }
    public int getMasteryLevel() { return mastery_level; }
    public int getStreak() { return streak; }

    public void setId(String id) { this.id = id; }
    public void setHint(String hint) { this.hint = hint; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setLastCorrect(Date last_correct) { this.last_correct = last_correct; }
    public void setMasteryLevel(int mastery_level) {this.mastery_level = mastery_level; }
    public void setStreak(int streak) { this.streak = streak; }

    @Override
    public String toString(){
        String dateStr = "";
        if (this.last_correct != null){
            dateStr = last_correct.toString();
        }
        return String.format("id: %1$s, hint: %2$s, answer: %3$s, lastCorrect: %4$s, mastery: %5$d, streak: %6$d", 
            this.id, this.hint, this.answer, dateStr, this.mastery_level, this.streak);
    }
}