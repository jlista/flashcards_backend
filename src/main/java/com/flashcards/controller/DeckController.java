package com.flashcards.controller;

import com.flashcards.model.DTO.DeckDTO;
import com.flashcards.model.DTO.UserDeckDTO;
import com.flashcards.service.DeckCardService;
import com.flashcards.service.DeckService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;
    private final DeckCardService deckCardService;

    public DeckController(DeckService deckService, DeckCardService deckCardService) {
        this.deckService = deckService;
        this.deckCardService = deckCardService;
    }

    @GetMapping("/userdecks")
    public List<UserDeckDTO> getDecks(@RequestParam Long userId) {
        return deckService.getUserDecks(userId);
    }

    @PostMapping()
    public UserDeckDTO createDeck(@RequestBody DeckDTO requestBody,
            @RequestParam Long userId) {
                
        String deckName = requestBody.getName();
        String description = requestBody.getDescription();
        return deckService.createDeck(userId, deckName, description);
    }

    @PutMapping()
    public DeckDTO updateDeck(@RequestBody DeckDTO requestBody,
            @RequestParam Long deckId) {
        String deckName = requestBody.getName();
        String description = requestBody.getDescription();
        return deckService.updateDeck(deckId, deckName, description);
    }

    @PostMapping("/copy")
    public void copyDeck(@RequestParam Long deckId, @RequestParam Long userId){
        deckCardService.copyDeckAndCards(deckId, userId);
    }

    @PutMapping("/share")
    public void shareDeck(@RequestParam Long deckId){
        deckService.setDeckPublic(deckId);
    }
}

