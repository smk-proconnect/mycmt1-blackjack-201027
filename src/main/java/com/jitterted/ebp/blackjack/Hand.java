package com.jitterted.ebp.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    final List<Card> cards = new ArrayList<>();

    public Hand() {
    }
    public Hand(List<Card> cards) {
        this.cards.addAll(cards);
    }
    void drawFromDeck(Deck deck) {
      cards.add(deck.draw());
    }

    public int handValueOf() {
      int handValue = sumOfAllCardsOnHand();

      // does the hand contain at least 1 Ace?
      boolean hasAce = IsPlayerHasAce();

      // if the total hand value <= 11, then count the Ace as 11 by adding 10
      handValue = addAceValue(handValue, hasAce);

      return handValue;
    }

    private int sumOfAllCardsOnHand() {
      return cards
              .stream()
              .mapToInt(Card::rankValue)
              .sum();
    }

    private int addAceValue(int handValue, boolean hasAce) {
      if (hasAce && handValue <= 11) {
        handValue += 10;
      }
      return handValue;
    }

    private boolean IsPlayerHasAce() {
        return cards
                .stream()
                .anyMatch(card -> card.rankValue() == 1);
    }
}