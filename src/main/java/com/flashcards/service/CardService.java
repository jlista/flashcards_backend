package com.flashcards.service;

import com.flashcards.model.Card;
import com.flashcards.model.Deck;
import com.flashcards.model.DeckCard;
import com.flashcards.model.UserDeck;
import com.flashcards.model.DTO.CardDTO;
import com.flashcards.repository.CardRepository;
import com.flashcards.repository.DeckCardRepository;
import com.flashcards.repository.DeckRepository;
import com.flashcards.repository.UserDeckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class CardService {

    final static Logger logger = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;
    private final DeckCardRepository deckCardRepository;
    private final DeckRepository deckRepository;
    private final UserDeckRepository userDeckRepository;
    private final AuthenticationService authenticationService;

    public CardService(CardRepository cardRepository, DeckCardRepository deckCardRepository,
            DeckRepository deckRepository, UserDeckRepository userDeckRepository, AuthenticationService authenticationService) {
        this.cardRepository = cardRepository;
        this.deckCardRepository = deckCardRepository;
        this.deckRepository = deckRepository;
        this.userDeckRepository = userDeckRepository;
        this.authenticationService = authenticationService;
    }

    public List<CardDTO> getAllCardsInUserDeck(Long userDeckId) {

        Optional<Long> owner = userDeckRepository.findOwner(userDeckId);

        System.out.println(owner.isPresent());
        System.out.println(authenticationService.isOwnerOrAdmin(owner.get()));
        if (!owner.isPresent() || !authenticationService.isOwnerOrAdmin(owner.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        List<CardDTO> allCards = cardRepository.getAllCardsInUserDeck(userDeckId);

        List<CardDTO> readyCards =
                allCards.stream().filter(c -> c.getIsReadyToReview()).collect(Collectors.toList());
        List<CardDTO> notReadyCards =
                allCards.stream().filter(c -> !c.getIsReadyToReview()).collect(Collectors.toList());
        readyCards.addAll(notReadyCards);

        return readyCards;
    }

    public Card getCardById(Long id) {
        /**
         * Given an id string, find and return the card with that id.
         */
        return cardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }


    public DeckCard getDeckCardById(Long cardId, Long userDeckId) {
        /**
         * Given an id string, find and return the card with that id.
         */
        Optional<Long> owner = userDeckRepository.findOwner(userDeckId);

        if (!owner.isPresent() || !authenticationService.isOwnerOrAdmin(owner.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }
        return deckCardRepository.findByCardAndDeckId(cardId, userDeckId)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public List<CardDTO> getAllPossibleCards(Long userDeckId) {
        return getAllCardsInUserDeck(userDeckId).stream().filter(card -> card.getIsReadyToReview())
                .toList();
    }

    public List<CardDTO> getBalancedPossibleCards(Long deckId) {
        List<CardDTO> cardChoices = getAllPossibleCards(deckId);

        List<CardDTO> balancedCardChoices = new ArrayList<CardDTO>();

        for (int i = 0; i < cardChoices.size(); i++) {
            CardDTO card = cardChoices.get(i);
            if (card.getMasteryLevel() > 0) {
                balancedCardChoices.add(card);
            } else {
                int priority = 5 - card.getStreak();
                for (int j = 0; j < priority; j++) {
                    balancedCardChoices.add(card);
                }
            }
        }
        return balancedCardChoices;
    }

    public Optional<CardDTO> getRandomCardSR(Long deckId) {
        /**
         * Searches the database for all eligible cards based on streak and mastery level, and
         * chooses one randomly
         * 
         * @return a randomly-selected eligible card
         */

        List<CardDTO> balancedCardChoices = getBalancedPossibleCards(deckId);

        if (balancedCardChoices.size() > 0) {
            Random rand = new Random();
            return Optional.of(balancedCardChoices.get(rand.nextInt(balancedCardChoices.size())));
        }
        return Optional.empty();
    }

    public Optional<CardDTO> getRandomCardSR(Long lastCorrect, Long deckId) {
        /**
         * Searches the database for all eligible cards based on streak and mastery level, and
         * chooses one randomly. Does not choose the most recently seen card unless that is the only
         * choice.
         * 
         * @param lastCorrect the ID of the most recently seen card
         * @return a randomly-selected eligible card
         */
        List<CardDTO> balancedCardChoices = getBalancedPossibleCards(deckId);

        if (balancedCardChoices.isEmpty()) {
            return Optional.empty();
        }

        // if the only choice is to show the previous card again, then show it
        if (!balancedCardChoices.stream().anyMatch(item -> !item.getCardId().equals(lastCorrect))) {
            return Optional.of(balancedCardChoices.get(0));
        }

        // otherwise, look for a choice that does not match the previous
        Random rand = new Random();
        CardDTO card;
        do {
            card = balancedCardChoices.get(rand.nextInt(balancedCardChoices.size()));

        } while (card.getCardId().equals(lastCorrect));
        return Optional.of(card);
    }

    public void updateCardStreak(Long cardId, long userDeckId, Boolean isCorrect) {
        /**
         * Updates the streak and mastery level of a card based on whether or not the user answered
         * correctly
         * 
         * @param card the card to update
         * @param isCorrect whether or not the user answered correctly
         */
        Optional<Long> owner = userDeckRepository.findOwner(userDeckId);

        if (!owner.isPresent() || !authenticationService.isOwnerOrAdmin(owner.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        DeckCard card = getDeckCardById(cardId, userDeckId);
        int mastery_level = card.getMasteryLevel();

        if (!isCorrect) {
            card.setStreak(0);
            card.setMasteryLevel(Math.max(mastery_level - 1, 0));
        } else {
            int streak = card.getStreak() + 1;
            if (streak % 5 == 0 && mastery_level < 4) {
                card.setMasteryLevel(mastery_level + 1);
            }
            card.setStreak(streak);
            Timestamp now = Timestamp.from(Instant.now());
            card.setLastCorrect(now);
        }
        deckCardRepository.save(card);
    }

    public Card createCard(String clue, String answer, Long deckId, Long userId) {

        Deck deck = deckRepository.getReferenceById(deckId);
        if (deck.isPublic()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot add a card to a public deck");
        }

        List<Long> userDeckIds = deckRepository.getAssociatedUserDeckIds(deckId);
        if (userDeckIds.size() != 1) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Deck must be associated with exactly one user to add a card");
        }

        Long userDeckId = userDeckIds.get(0);
        Optional<Long> owner = userDeckRepository.findOwner(userDeckId);

        if (!owner.isPresent() || !authenticationService.isOwnerOrAdmin(owner.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }
        
        Card card = cardRepository.save(new Card(clue, answer, deckId, userId));
        deckCardRepository.save(new DeckCard(card.getCardId(), userDeckId));

        logger.info("Created card: " + card.toString());
        return card;
    }

   @Deprecated
   public Card migrateCard(String hint, String answer, String lastCorrect, int masteryLevel, int streak, Long deckId, long userId) {

        Timestamp timestamp = new Timestamp(0l);
        try {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date parsedDate = df.parse(lastCorrect);
        timestamp = new java.sql.Timestamp(parsedDate.getTime());
        }catch(Exception e){
            logger.info(e.getMessage());
        }

        Card card = cardRepository.save(new Card(hint, answer, deckId, userId));

        List<Long> userDeckIds = deckRepository.getAssociatedUserDeckIds(deckId);

        for (int i = 0; i < userDeckIds.size(); i++) {
            DeckCard dc = new DeckCard(card.getCardId(), userDeckIds.get(i));
            dc.setMasteryLevel(masteryLevel);
            dc.setStreak(streak);
            dc.setLastCorrect(timestamp);
            deckCardRepository.save(dc);
        }

        logger.info("Created card: " + card.toString());
        return card;
    }

    public Card updateCard(Long id, String clue, String answer) {

        Optional<Long> owner = cardRepository.findOwner(id);
        if (!owner.isPresent() || !authenticationService.isOwnerOrAdmin(owner.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        Card card = getCardById(id);
        long deckId = card.getDeckId();
        Deck deck = deckRepository.getReferenceById(deckId);
        if (deck.isPublic()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify a card in a public deck");
        }

        card.setClue(clue);
        card.setAnswer(answer);
        Card res = cardRepository.save(card);
        logger.info("Updated card: " + res.toString());
        return res;
    }

    public void deleteCard(Long cardId, Long deckId) {

        Card card = getCardById(cardId);
        Long owner = card.getOwnedBy();
        if (!authenticationService.isOwnerOrAdmin(owner)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        Deck deck = deckRepository.getReferenceById(deckId);
        if (deck.isPublic()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete a card in a public deck");
        }

        List<Long> uds = deckRepository.getAssociatedUserDeckIds(deckId);
        logger.info(uds.toString());
        List<DeckCard> dcs = uds.stream().map(ud -> getDeckCardById(cardId,ud)).toList();
        logger.info(dcs.toString());
        dcs.stream().forEach(dc -> deckCardRepository.delete(dc));
        cardRepository.delete(card);
        logger.info("Deleted card: " + card.toString());
    }

    public void resetCard(Long cardId, Long userDeckId) {

        // TODO - this should be based on whether the user owns the userdeck associated with the card
        Optional<Long> owner = cardRepository.findOwner(cardId);
        if (!owner.isPresent() || !authenticationService.isOwnerOrAdmin(owner.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }
        DeckCard card = getDeckCardById(cardId, userDeckId);
        card.setLastCorrect(null);
        card.setMasteryLevel(0);
        card.setStreak(0);
        deckCardRepository.save(card);
    }

    /**
     * given ids of a public Deck and UserDeck, create a DeckCard instance of DeckCard for each Card in the Deck
     * and associate it with the given UserDeck.
     * 
     * @param deckId
     * @param userDeckId
     */
    protected void copyCardsToUserDeck(Long deckId, Long userDeckId) {

        UserDeck ud = userDeckRepository.getReferenceById(userDeckId);
        List<Card> cards = cardRepository.getAllCardsInDeck(deckId);

        if (!authenticationService.isOwnerOrAdmin(ud.getOwnedBy())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to access this resource");
        }

        ArrayList<DeckCard> dcs = new ArrayList<DeckCard>();
        for (int i = 0; i < cards.size(); i++){
            Card c = cards.get(i);
            dcs.add(new DeckCard(c.getCardId(), userDeckId));
        }
        deckCardRepository.saveAllAndFlush(dcs);
    }

}
