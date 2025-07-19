package com.flashcards;

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

    @GetMapping("/allPossible")
    public List<Card> getAllPossibleCards() {
        return cardService.getAllPossibleCards();
    }

    @GetMapping("/{id}")
    public Card getCardById(@PathVariable String id) {
        return cardService.getCardById(id);
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
    

    @PutMapping("/{id}")
    public void answerCard(@PathVariable String id, @RequestBody Boolean isCorrect){
        Card card = cardService.getCardById(id);
        cardService.updateCardStreak(card, isCorrect);
    }
}