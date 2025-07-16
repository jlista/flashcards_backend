package com.flashcards.service;

import com.flashcards.model.Card;
import com.flashcards.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
        return cardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public Card getRandomCardSR() {
        return cardRepository.getOneCardSR();
    }

    public void updateCardStreak(Card card, Boolean isCorrect) {
        if (!isCorrect){
            card.setStreak(0);
            card.setMasteryLevel(0);
        }
        else {
            int streak = card.getStreak();
            int mastery_level = card.getMasteryLevel();
            if (streak == 4 && mastery_level < 4){
                card.setMasteryLevel(mastery_level + 1);
                card.setStreak(0);
            }
            else {
                card.setStreak(streak + 1);
            }
        }
        cardRepository.save(card);
    }
}