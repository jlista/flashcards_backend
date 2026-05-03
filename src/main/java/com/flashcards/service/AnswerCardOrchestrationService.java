package com.flashcards.service;

import org.springframework.stereotype.Service;

@Service
public class AnswerCardOrchestrationService {
    
    private final CardService cardService;
    private final StatisticService statisticService;

    public AnswerCardOrchestrationService(CardService cardService, StatisticService statisticService){
        this.cardService = cardService;
        this.statisticService = statisticService;
    }

    public void updateCardStreak(Long cardId, long userDeckId, Boolean isCorrect) {
        Long userId = cardService.updateCardStreak(cardId, userDeckId, isCorrect);
        statisticService.updateDailyStats(userId, isCorrect);
    }
}
