package com.flashcards;

import com.flashcards.model.Card;
import com.flashcards.model.Message;
import com.flashcards.service.CardService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final CardService cardService;

    public HelloController(CardService cardService) {
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

    @GetMapping("/randomsr")
    public Card getRandomCardSR() {
        return cardService.getRandomCardSR();
    }

    @GetMapping("/hello")
    public Message sayHello(@RequestParam(defaultValue = "World") String name) {
        return new Message("Hello, " + name + "!");
    }

    @PostMapping("/echo")
    public Message echoMessage(@RequestBody Message message) {
        return new Message("You said: " + message.getContent());
    }
}