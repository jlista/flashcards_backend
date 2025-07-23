package com.flashcards.repository;

import com.flashcards.model.Card;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String>, CardRepositoryCustom {

    public List<Card> getAllPossibleCards();
}
