package com.flashcards.model.DTO;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DeckDTO implements Serializable {
    private String name;
    private String description;
    private boolean isPublic;
}
