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
    private String name;
    private String description;
    private int num_cards;
    private int num_cards_ready;
    private boolean isPublic;

    public UserDeckDTO(Long userDeckId, Long deckId, Long userId, String name, String description, boolean isPublic) {
        this.userDeckId = userDeckId;
        this.deckId = deckId;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
    }
}
