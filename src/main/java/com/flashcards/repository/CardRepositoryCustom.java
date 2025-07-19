package com.flashcards.repository;

import java.util.List;
import java.util.Optional;

import com.flashcards.model.Card;

public interface CardRepositoryCustom {

    public List<Card> getAllPossibleCards();

    public Optional<Card> getOneCardSR();
}