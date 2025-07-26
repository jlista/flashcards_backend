package com.flashcards.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.flashcards.model.Deck;
import com.flashcards.model.UserDeck;
import com.flashcards.model.DTO.UserDeckDTO;
import com.flashcards.repository.DeckRepository;
import com.flashcards.repository.UserDeckRepository;

@Service
public class DeckService {

    final static Logger logger = LoggerFactory.getLogger(CardService.class);

    private final DeckRepository deckRepository;
    private final UserDeckRepository userDeckRepository;

    public DeckService(DeckRepository deckRepository, UserDeckRepository userDeckRepository) {

        this.deckRepository = deckRepository;
        this.userDeckRepository = userDeckRepository;
    }

    public List<UserDeckDTO> getUserDecks(Long userId) {
        return userDeckRepository.findByOwnedBy(userId);
    }

    public UserDeckDTO createDeck(Long userId, String name, String description) {
        Deck deck = new Deck();
        deck.setDeckName(name);
        deck.setDescription(description);
        deck.setOwnedBy(userId);
        deckRepository.save(deck);
        long deckId = deck.getDeckId();
        UserDeck ud = new UserDeck();
        ud.setDeckId(deckId);
        ud.setOwnedBy(userId);
        userDeckRepository.save(ud);
        return new UserDeckDTO(ud.getUserDeckId(), deckId, userId, name, description);
    }
}
