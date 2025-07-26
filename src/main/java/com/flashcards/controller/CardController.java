package com.flashcards.controller;

import com.flashcards.model.Card;
import com.flashcards.model.DTO.CardDTO;
import com.flashcards.model.DTO.CardCreationDTO;
import com.flashcards.service.CardService;
import com.flashcards.service.DeckService;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService, DeckService deckService) {
        this.cardService = cardService;
    }

    @GetMapping()
    public List<CardDTO> getAllCards(@RequestParam Long userDeckId) {
        return cardService.getAllCardsInDeck(userDeckId);
    }

    @GetMapping("/{id}")
    public Card getCardById(@PathVariable Long id) {
        return cardService.getCardById(id);
    }

    @PostMapping()
    public Card createCard(@RequestBody CardCreationDTO requestBody, @RequestParam Long deckId) {
        String hint = requestBody.getHint();
        String answer = requestBody.getAnswer();
        return cardService.createCard(hint, answer, deckId, 1l);
    }

    @PutMapping("/{id}")
    public Card updateCard(@PathVariable Long id, @RequestBody CardCreationDTO requestBody) {
        String hint = requestBody.getHint();
        String answer = requestBody.getAnswer();
        return cardService.updateCard(id, hint, answer);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

    @GetMapping("/allPossible")
    public List<CardDTO> getAllPossibleCards(@RequestParam Long userDeckId) {
        return cardService.getAllPossibleCards(userDeckId);
    }

    @GetMapping("/randomsr")
    public ResponseEntity<CardDTO> getRandomSR(@RequestParam Optional<Long> lastAnswered, @RequestParam Long userDeckId) {

        Optional<CardDTO> card;
        if (lastAnswered.isPresent()) {
            card = cardService.getRandomCardSR(lastAnswered.get(), userDeckId);
        } else {
            card = cardService.getRandomCardSR(userDeckId);
        }

        if (card.isPresent()) {
            return new ResponseEntity<>(card.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/answer")
    public void answerCard(@RequestParam Long cardId, @RequestParam Long userDeckId, @RequestBody Boolean isCorrect) {
        cardService.updateCardStreak(cardId, userDeckId, isCorrect);
    }

    @PutMapping("/reset")
    public void resetCard(@RequestParam Long cardId, @RequestParam Long userDeckId) {
        cardService.resetCard(cardId, userDeckId);
    }
} 
