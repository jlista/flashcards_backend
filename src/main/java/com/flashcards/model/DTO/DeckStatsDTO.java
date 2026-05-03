package com.flashcards.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeckStatsDTO {
    private int numDecks;
    private int numCards;
    private int[] cardsByMastery;
}
