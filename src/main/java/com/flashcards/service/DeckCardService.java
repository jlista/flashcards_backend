package com.flashcards.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.flashcards.model.Deck;
import com.flashcards.model.DeckCard;
import com.flashcards.model.UserDeck;
import com.flashcards.model.DTO.DeckDTO;
import com.flashcards.model.DTO.DeckStatsDTO;
import com.flashcards.model.DTO.UserDeckDTO;

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

    public void deleteDeck(Long userDeckId){
        
        DeckDTO d = deckService.getAssociatedDeck(userDeckId);
        
        cardService.deleteDeckCards(userDeckId);
        deckService.deleteUserDeck(userDeckId);

        if (!d.isPublic()){
            cardService.deleteCards(d.getDeckId());
            deckService.deleteDeck(d.getDeckId());
        }
    }

    public DeckStatsDTO getConsolidatedDeckCardStats(Long userId){
        List<UserDeckDTO> uds = deckService.getUserDecks(userId);
        List<DeckCard> dcs = cardService.getDeckCardsByUser(userId);
        DeckStatsDTO stats = new DeckStatsDTO();
        stats.setNumDecks(uds.size());
        stats.setNumCards(dcs.size());
        int[] cardsByMastery = new int[5];
        for (DeckCard card: dcs){
            cardsByMastery[card.getMasteryLevel()]++;
        }
        stats.setCardsByMastery(cardsByMastery);
        return stats;
    }
}
