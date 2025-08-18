package com.flashcards.model.DTO;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatsDTO {
    private LocalDate dateStamp;
    private int numCorrect;
    private int numIncorrect;
}
