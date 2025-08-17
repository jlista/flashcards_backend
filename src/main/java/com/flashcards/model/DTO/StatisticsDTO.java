package com.flashcards.model.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {
    private Long userId;
    private int totalAnswered;
    private int totalCorrect;
    private int totalIncorrect;
    private float percentCorrect;
    private List<DailyStatsDTO> dailyStats;
}

