package com.flashcards.controller;

import com.flashcards.model.Card;
import com.flashcards.service.CardService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping()
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public Card getCardById(@PathVariable String id) {
        return cardService.getCardById(id);
    }

    @PostMapping()
    public Card createCard(@RequestBody CardRequestBody cardRequestBody){
        String hint = cardRequestBody.getHint();
        String answer = cardRequestBody.getAnswer();
        return cardService.createCard(hint, answer);
    }

    @PutMapping("/{id}")
    public Card updateCard(@PathVariable String id, @RequestBody CardRequestBody cardRequestBody){
        String hint = cardRequestBody.getHint();
        String answer = cardRequestBody.getAnswer();
        return cardService.updateCard(id, hint, answer);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable String id){
        cardService.deleteCard(id);
    }

    @GetMapping("/allPossible")
    public List<Card> getAllPossibleCards() {
        return cardService.getAllPossibleCards();
    }

    @GetMapping("/randomsr")
    public ResponseEntity<Card> getRandomSR() {
        Optional<Card> card = cardService.getRandomCardSR();
        if (card.isPresent()) {
            return new ResponseEntity<>(card.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/answer")
    public void answerCard(@RequestParam String id, @RequestBody Boolean isCorrect){
        cardService.updateCardStreak(id, isCorrect);
    }

    @PutMapping("/reset")
    public void resetCard(@RequestParam String id){
        cardService.resetCard(id);
    }
}