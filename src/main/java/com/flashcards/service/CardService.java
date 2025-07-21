package com.flashcards.service;

import com.flashcards.model.Card;
import com.flashcards.repository.CardRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
public class CardService {
    
    final static Logger logger = LoggerFactory.getLogger(CardService.class);

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

    public List<Card> getAllPossibleCards() {
        return cardRepository.getAllPossibleCards();
    }

    public Optional<Card> getRandomCardSR() {
        /**
         * Searches the database for all eligible cards based on streak and mastery level, and chooses one randomly
         * @return a randomly-selected eligible card
         */
        List<Card> cardChoices = getAllPossibleCards();

        if (cardChoices.size() > 0){
            Random rand = new Random();
            return Optional.of(cardChoices.get(rand.nextInt(cardChoices.size())));
        }
        return Optional.empty();
    }

    public void updateCardStreak(String id, Boolean isCorrect) {
        /**
         * Updates the streak and mastery level of a card based on whether or not the user answered correctly
         * @param card the card to update
         * @param isCorrect whether or not the user answered correctly
         */
        Card card = getCardById(id);

        if (!isCorrect){
            card.setStreak(0);
            card.setMasteryLevel(0);
        }
        else {
            int streak = card.getStreak() + 1;
            int mastery_level = card.getMasteryLevel();
            if (streak % 5 == 0 && mastery_level < 4){
                card.setMasteryLevel(mastery_level + 1);
            }
            card.setStreak(streak);
            
            Date now = Date.from(Instant.now());
            card.setLastCorrect(now);
        }
        cardRepository.save(card);
    }

    public Card createCard(String hint, String answer){
        Card card = new Card(hint, answer);
        Card res = cardRepository.insert(card);
        logger.info("Created card: " + res.toString());
        return res;
    }

    public Card updateCard(String id, String hint, String answer){
        Card card = getCardById(id);
        card.setHint(hint);
        card.setAnswer(answer);
        Card res = cardRepository.save(card);
        logger.info("Updated card: " + res.toString());
        return res;
    }

    public void deleteCard(String id){
        Card card = getCardById(id);
        logger.info("Deleted card: " + card.toString());
        cardRepository.delete(card);
    }

    public void resetCard(String id){
        Card card = getCardById(id);
        card.setLastCorrect(null);
        card.setMasteryLevel(0);
        card.setStreak(0);
        cardRepository.save(card);
    }
}