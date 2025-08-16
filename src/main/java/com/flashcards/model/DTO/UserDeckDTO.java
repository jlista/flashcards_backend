package com.flashcards.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDeckDTO {

    private Long userDeckId;
    private Long deckId;
    private Long userId;
    private String deck_name;
    private String deck_desc;
    private int num_cards;
    private int num_cards_ready;
    private boolean isPublic;

    public UserDeckDTO(Long userDeckId, Long deckId, Long userId, String deck_name, String deck_desc, boolean isPublic) {
        this.userDeckId = userDeckId;
        this.deckId = deckId;
        this.userId = userId;
        this.deck_name = deck_name;
        this.deck_desc = deck_desc;
        this.isPublic = isPublic;
    }
}
