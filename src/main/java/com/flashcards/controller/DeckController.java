package com.flashcards.controller;

import com.flashcards.model.DTO.DeckCreationDTO;
import com.flashcards.model.DTO.UserDeckDTO;
import com.flashcards.service.DeckService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/userdecks")
    public List<UserDeckDTO> getDecks(@RequestParam Long userId) {
        return deckService.getUserDecks(userId);
    }

    @PostMapping()
    public UserDeckDTO createDeck(@RequestBody DeckCreationDTO requestBody, @RequestParam Long userId) {
        String deckName = requestBody.getName();
        String description = requestBody.getDescription();
        return deckService.createDeck(userId, deckName, description);
    }
}

