package com.flashcards.service;

import com.flashcards.model.Card;
import com.flashcards.repository.CardRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CardService {

    final static Logger logger = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        List<Card> allCards = cardRepository.findAll();
        List<Card> readyCards =
                allCards.stream().filter(c -> c.getIsReadyToReview()).collect(Collectors.toList());
        List<Card> notReadyCards =
                allCards.stream().filter(c -> !c.getIsReadyToReview()).collect(Collectors.toList());
        readyCards.addAll(notReadyCards);

        return readyCards;
    }

    public Card getCardById(String id) {
        /**
         * Given an id string, find and return the card with that id.
         */
        return cardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public List<Card> getAllPossibleCards() {
        return getAllCards().stream().filter(card -> card.getIsReadyToReview()).toList();
    }

    public List<Card> getBalancedPossibleCards() {
        List<Card> cardChoices = getAllPossibleCards();

        List<Card> balancedCardChoices = new ArrayList<Card>();

        for (int i = 0; i < cardChoices.size(); i++) {
            Card card = cardChoices.get(i);
            if (card.getMasteryLevel() > 0) {
                balancedCardChoices.add(card);
            } else {
                int priority = 5 - card.getStreak();
                for (int j = 0; j < priority; j++) {
                    balancedCardChoices.add(card);
                }
            }
        }
        return balancedCardChoices;
    }

    public Optional<Card> getRandomCardSR() {
        /**
         * Searches the database for all eligible cards based on streak and mastery level, and
         * chooses one randomly
         * 
         * @return a randomly-selected eligible card
         */

        List<Card> balancedCardChoices = getBalancedPossibleCards();

        if (balancedCardChoices.size() > 0) {
            Random rand = new Random();
            return Optional.of(balancedCardChoices.get(rand.nextInt(balancedCardChoices.size())));
        }
        return Optional.empty();
    }

    public Optional<Card> getRandomCardSR(String lastCorrect) {
        /**
         * Searches the database for all eligible cards based on streak and mastery level, and
         * chooses one randomly. Does not choose the most recently seen card unless that is the only
         * choice.
         * 
         * @param lastCorrect the ID of the most recently seen card
         * @return a randomly-selected eligible card
         */
        List<Card> balancedCardChoices = getBalancedPossibleCards();

        if (balancedCardChoices.isEmpty()) {
            return Optional.empty();
        }

        // if the only choice is to show the previous card again, then show it
        if (!balancedCardChoices.stream().anyMatch(item -> !item.getId().equals(lastCorrect))) {
            return Optional.of(balancedCardChoices.get(0));
        }

        // otherwise, look for a choice that does not match the previous
        Random rand = new Random();
        Card card;
        do {
            card = balancedCardChoices.get(rand.nextInt(balancedCardChoices.size()));

        } while (card.getId().equals(lastCorrect));
        return Optional.of(card);
    }

    public void updateCardStreak(String id, Boolean isCorrect) {
        /**
         * Updates the streak and mastery level of a card based on whether or not the user answered
         * correctly
         * 
         * @param card the card to update
         * @param isCorrect whether or not the user answered correctly
         */
        Card card = getCardById(id);
        int mastery_level = card.getMasteryLevel();

        if (!isCorrect) {
            card.setStreak(0);
            card.setMasteryLevel(Math.max(mastery_level - 1, 0));
        } else {
            int streak = card.getStreak() + 1;
            if (streak % 5 == 0 && mastery_level < 4) {
                card.setMasteryLevel(mastery_level + 1);
            }
            card.setStreak(streak);
            Date now = Date.from(Instant.now());
            card.setLastCorrect(now);
        }
        cardRepository.save(card);
    }

    public Card createCard(String hint, String answer) {
        Card card = new Card(hint, answer);
        Card res = cardRepository.insert(card);
        logger.info("Created card: " + res.toString());
        return res;
    }

    public Card updateCard(String id, String hint, String answer) {
        Card card = getCardById(id);
        card.setHint(hint);
        card.setAnswer(answer);
        Card res = cardRepository.save(card);
        logger.info("Updated card: " + res.toString());
        return res;
    }

    public void deleteCard(String id) {
        Card card = getCardById(id);
        logger.info("Deleted card: " + card.toString());
        cardRepository.delete(card);
    }

    public void resetCard(String id) {
        Card card = getCardById(id);
        card.setLastCorrect(null);
        card.setMasteryLevel(0);
        card.setStreak(0);
        cardRepository.save(card);
    }
}
