package com.flashcards.service;

import org.springframework.stereotype.Service;
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
        UserDeck ud = deckService.copyDeckForUser(deckId, userId);
        cardService.copyCardsToUserDeck(deckId, ud.getUserDeckId());
    }
}
