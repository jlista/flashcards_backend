package com.flashcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.flashcards.repository.AppUserRepository;
import com.flashcards.repository.CardRepository;
import com.flashcards.repository.DailyStatsRepository;
import com.flashcards.repository.DeckCardRepository;
import com.flashcards.repository.DeckRepository;
import com.flashcards.repository.UserDeckRepository;

@SpringBootApplication
// @EnableJpaRepositories(basePackages = "com.flashcards.repository")
public class FlashcardsBackendApplication {
  public static void main(String[] args) {
    SpringApplication.run(FlashcardsBackendApplication.class, args);
  }

  @Autowired
  CardRepository cardRepository;
  @Autowired
  DeckCardRepository deckCardRepository;
  @Autowired
  DeckRepository deckRepository;
  @Autowired
  AppUserRepository appUserRepository;
  @Autowired
  UserDeckRepository userDeckRepository;
  @Autowired
  DailyStatsRepository dailyStatsRepository;
}


