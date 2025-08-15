package com.flashcards.model.DTO;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CardMigrationDTO implements Serializable {
    private String hint;
    private String answer;
    private String lastCorrect;
    private int masteryLevel;
    private int streak;
}