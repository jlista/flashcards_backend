package com.flashcards.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.flashcards.model.Deck;
import com.flashcards.model.UserDeck;
import com.flashcards.model.DTO.DeckDTO;
import com.flashcards.model.DTO.UserDeckDTO;
import com.flashcards.repository.DeckRepository;
import com.flashcards.repository.UserDeckRepository;

@Service
public class DeckService {

    final static Logger logger = LoggerFactory.getLogger(DeckService.class);

    private final DeckRepository deckRepository;
    private final UserDeckRepository userDeckRepository;
    
    private final AuthenticationService authenticationService;

    public DeckService(DeckRepository deckRepository, UserDeckRepository userDeckRepository, AuthenticationService authenticationService) {

        this.deckRepository = deckRepository;
        this.userDeckRepository = userDeckRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Get all UserDecks associated with a given user ID
     * @param userId
     * @return
     */
    public List<UserDeckDTO> getUserDecks(Long userId) {
        return userDeckRepository.findByOwnedBy(userId);
    }

    /**
     * Creates a Deck object, and then creates a UserDeck object associating the new deck with the user who created it
     * @param userId the id of the user who will own the deck
     * @param name name of the deck
     * @param description description of the deck
     * @return 
     */
    public UserDeckDTO createDeck(Long userId, String name, String description) {
        
        if (!authenticationService.isOwnerOrAdmin(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }
        Deck deck = new Deck();
        deck.setDeckName(name);
        deck.setDescription(description);
        deck.setOwnedBy(userId);
        deck.setPublic(false);
        deckRepository.save(deck);
        long deckId = deck.getDeckId();
        UserDeck ud = new UserDeck();
        ud.setDeckId(deckId);
        ud.setOwnedBy(userId);
        userDeckRepository.save(ud);
        return new UserDeckDTO(ud.getUserDeckId(), deckId, userId, name, description, false);
    }

    /**
     * Updates the name and description of a given deck
     * 
     * @param deckId the id of the deck to update
     * @param name
     * @param description
     * @return a DTO containing the updated name and description
     */
    public DeckDTO updateDeck(Long deckId, String name, String description) {
        Deck deck = deckRepository.getReferenceById(deckId);

        if (deck.isPublic()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update a public deck");
        }

        if (!authenticationService.isOwnerOrAdmin(deck.getOwnedBy())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        deck.setDeckName(name);
        deck.setDescription(description);
        deckRepository.save(deck);
       
        return new DeckDTO(deck.getDeckId(), name, description, deck.isPublic());
    }

    /**
     * Sets the isPublic flag to true, allowing a deck to be shared with other users
     * 
     * @param deckId the id of the deck to update
     */
    public void setDeckPublic(Long deckId) {

        Deck deck = deckRepository.getReferenceById(deckId);

        if (!authenticationService.isOwnerOrAdmin(deck.getOwnedBy())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        deck.setPublic(true);
        deckRepository.save(deck);
    }

    /**
     * Given a user ID and deck ID, creates a UserDeck object allowing that user to
     * use their own version of the deck.
     * 
     * @param deckId (must be a public deck)
     * @param userId
     * 
     * @return the UserDeck that was created
     */
    protected UserDeck copyDeckForUser(Long deckId, Long userId){
        Deck deck = deckRepository.getReferenceById(deckId);
        
        // only allow copying a deck if it is public or owned by the current user
        if (!deck.isPublic() && !authenticationService.isOwnerOrAdmin(deck.getOwnedBy())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        UserDeck ud = new UserDeck();
        ud.setDeckId(deckId);
        ud.setOwnedBy(userId);
        
        return userDeckRepository.save(ud);
    }

    public List<DeckDTO> getPublicDecks(){
        return deckRepository.getPublicDecks(); 
    }

    public List<DeckDTO> getPublicDecksNotOwned(Long userId){
        if (!authenticationService.isOwnerOrAdmin(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        return deckRepository.getPublicDecksNotOwned(userId);
    }
}
