package com.flashcards.service;

import org.springframework.stereotype.Service;
import com.flashcards.model.Deck;
import com.flashcards.model.UserDeck;

@Service
public class DeckCardService {

    private CardService cardService;
    private DeckService deckService;

    public DeckCardService(DeckService deckService, CardService cardService){
        this.deckService = deckService;
        this.cardService = cardService;
    }
    
    public void copyDeckAndCards(Long deckId, Long userId){
        UserDeck new_user_deck = deckService.copyDeckForUser(deckId, userId);
        cardService.copyCardsToUserDeck(deckId, new_user_deck.getUserDeckId());
    }

    public void cloneDeckAndCards(Long deckId, Long userId, String name, String desc){
        Deck new_deck = deckService.cloneDeck(deckId, userId, name, desc);
        cardService.cloneCards(deckId, new_deck.getDeckId(), userId);
        UserDeck new_user_deck = deckService.copyDeckForUser(new_deck.getDeckId(), userId);
        cardService.copyCardsToUserDeck(new_deck.getDeckId(), new_user_deck.getUserDeckId());
    }
}
