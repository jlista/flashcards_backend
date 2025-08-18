package com.flashcards.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.flashcards.model.DailyStats;
import com.flashcards.model.DTO.DailyStatsDTO;
import com.flashcards.model.DTO.StatisticsDTO;
import com.flashcards.repository.DailyStatsRepository;

@Service
public class StatisticService {

    private final DailyStatsRepository dailyStatsRepository;
    private final AuthenticationService authenticationService;

    public StatisticService(DailyStatsRepository dailyStatsRepository, AuthenticationService authenticationService){
        this.dailyStatsRepository = dailyStatsRepository;
        this.authenticationService = authenticationService;
    }
    
    /**
     * Return statistics for the user including the full list of daily stats and consolidated stats
     * @param userId
     * @return
     */
    public StatisticsDTO getUserStatistics(Long userId){

        if (!authenticationService.isOwnerOrAdmin(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }
        List<DailyStatsDTO> dailyStats = dailyStatsRepository.findByUser(userId);
        
        StatisticsDTO stats = new StatisticsDTO();

        stats.setDailyStats(dailyStats);

        int totalCorrect = 0;
        int totalIncorrect = 0;

        for (DailyStatsDTO s: dailyStats){
            totalCorrect += s.getNumCorrect();
            totalIncorrect += s.getNumIncorrect();
        }

        int totalAnswered = totalCorrect + totalIncorrect;
        float percentCorrect = 100 * totalCorrect / totalAnswered;

        stats.setUserId(userId);
        stats.setPercentCorrect(percentCorrect);
        stats.setTotalAnswered(totalAnswered);
        stats.setTotalCorrect(totalCorrect);
        stats.setTotalIncorrect(totalIncorrect);

        return stats;
    }

    /**
     * Update a user's daily stats by incrementing either the correct or incorrect answer counter
     * @param userId
     * @param isCorrect
     */
    protected void updateDailyStats(Long userId, Boolean isCorrect) {
        LocalDate today = LocalDate.now();
        Optional<DailyStats> s = dailyStatsRepository.findByUserAndDate(userId, today);

        DailyStats stats;
        if (s.isPresent()){
            stats = s.get();
        } 
        else {
            stats = new DailyStats(userId, today);
        }

        if (isCorrect){
            stats.setNumCorrect(stats.getNumCorrect() + 1);
        }
        else {
            stats.setNumIncorrect(stats.getNumIncorrect() + 1);
        }
        dailyStatsRepository.saveAndFlush(stats);
    }
}
