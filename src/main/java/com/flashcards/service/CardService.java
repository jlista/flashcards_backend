package com.flashcards.service;

import com.flashcards.CardHelper;
import com.flashcards.model.Card;
import com.flashcards.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
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
        /**
         * Given an id string, find and return the card with that id. 
         */
        return cardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public Card getRandomCardSR() {
        /**
         * Searches the database for all eligible cards based on streak and mastery level, and chooses one randomly
         * @return a randomly-selected eligible card
         */
        return cardRepository.getOneCardSR();
    }

    public void updateCardStreak(Card card, Boolean isCorrect) {
        /**
         * Updates the streak and mastery level of a card based on whether or not the user answered correctly
         * @param card the card to update
         * @param isCorrect whether or not the user answered correctly
         */
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

            Date now = Date.from(Instant.now());
            card.setLastCorrect(CardHelper.getIsoDateFormat(now));
        }
        cardRepository.save(card);
    }
}