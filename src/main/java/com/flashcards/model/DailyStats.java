package com.flashcards.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DailyStatsPK.class)
@Table(name = "daily_stats")
@ToString
public class DailyStats {

    @Id
    @Column(name = "user_id", nullable = false, unique = false)
    private Long userId;
    @Id
    @Column(name = "date_stamp", nullable = false, unique = false)
    private LocalDate dateStamp;
    @Column(name = "num_correct", nullable = false, unique = false)
    private int numCorrect = 0;
    @Column(name = "num_incorrect", nullable = false, unique = false)
    private int numIncorrect = 0;

    public DailyStats(Long userId, LocalDate dateStamp){
        this.userId = userId;
        this.dateStamp = dateStamp;
    }
}
