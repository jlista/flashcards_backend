package com.flashcards.model.DTO;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatsDTO {
    private LocalTime dateStamp;
    private int numCorrect;
    private int numIncorrect;
}
