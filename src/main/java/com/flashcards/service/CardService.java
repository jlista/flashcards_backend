package com.flashcards.service;

import com.flashcards.model.Card;
import com.flashcards.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(String id) {
        return cardRepository.findById(id).orElse(null);
    }
}