package com.flashcards.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DailyStatsPK implements Serializable {
      private Long userId;
      private LocalDate dateStamp;

      @Override
      public boolean equals(Object o) {
            if (this == o)
                  return true;
            if (!(o instanceof DailyStatsPK dailyStatsPK))
                  return false;
            return userId == dailyStatsPK.userId && dateStamp == dailyStatsPK.dateStamp;
      }

      @Override
      public int hashCode() {
            return Objects.hash(userId, dateStamp);
      }
}
