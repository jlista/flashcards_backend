package com.flashcards.repository;

import com.flashcards.model.DailyStats;
import com.flashcards.model.DTO.DailyStatsDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {

    @Query("SELECT new com.flashcards.model.DailyStats(s.userId, s.dateStamp, s.numCorrect, s.numIncorrect) "
          +"FROM DailyStats s "
          +"WHERE s.userId = :userId AND s.dateStamp = :dateStamp")
    public Optional<DailyStats> findByUserAndDate(@Param("userId") Long userId, @Param("dateStamp") LocalDate dateStamp);


    @Query("SELECT new com.flashcards.model.DTO.DailyStatsDTO(s.dateStamp, s.numCorrect, s.numIncorrect) "
        +"FROM DailyStats s "
        +"WHERE s.userId = :userId")
    public List<DailyStatsDTO> findByUser(@Param("userId") Long userId);
}
