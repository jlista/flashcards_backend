package com.flashcards.repository;

import com.flashcards.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String> {
    // Optional: Add custom query methods here
}