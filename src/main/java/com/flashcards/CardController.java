package com.flashcards;

import com.flashcards.model.Card;
import com.flashcards.service.CardService;

import java.util.List;

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
    public Card getRandomCardSR() {
        return cardService.getRandomCardSR();
    }

    @PutMapping("/{id}")
    public void answerCard(@PathVariable String id, @RequestBody Boolean isCorrect){
        Card card = cardService.getCardById(id);
        cardService.updateCardStreak(card, isCorrect);
    }
}