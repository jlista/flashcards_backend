package com.flashcards.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.flashcards.model.DTO.StatisticsDTO;
import com.flashcards.model.DTO.UserDeckDTO;
import com.flashcards.service.AuthenticationService;
import com.flashcards.service.JwtService;
import com.flashcards.service.StatisticService;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

    private final StatisticService statisticsService;

    public StatisticsController(StatisticService statisticService){
        this.statisticsService = statisticService;
    }

    @GetMapping("/")
    public StatisticsDTO getDecks(@RequestParam Long userId) {
        return statisticsService.getUserStatistics(userId);
    }
    
}
