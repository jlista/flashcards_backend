package com.flashcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.flashcards.repository")
public class FlashcardsBackendApplication {
  public static void main(String[] args) {
    SpringApplication.run(FlashcardsBackendApplication.class, args);
  }
}