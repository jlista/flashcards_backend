package com.flashcards.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DeckCardPK implements Serializable {
      private Long userDeckId;
      private Long cardId;

      @Override
      public boolean equals(Object o) {
            if (this == o)
                  return true;
            if (!(o instanceof DeckCardPK deckCardPK))
                  return false;
            return userDeckId == deckCardPK.userDeckId && cardId == deckCardPK.cardId;
      }

      @Override
      public int hashCode() {
            return Objects.hash(cardId, userDeckId);
      }
}
