package com.flashcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.flashcards.repository.AppUserRepository;
import com.flashcards.repository.CardRepository;
import com.flashcards.repository.DeckCardRepository;
import com.flashcards.repository.DeckRepository;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.flashcards.repository")
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
    AppUserRepository dppUserRepository;
}


