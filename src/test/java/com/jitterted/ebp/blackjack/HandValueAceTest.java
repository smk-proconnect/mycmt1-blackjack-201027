package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class HandValueAceTest {

  @Test
  public void handWithOneAceTwoCardsIsValuedAt11() throws Exception {

    var cards = List.of(new Card(Suit.Spades, "A"),
                       new Card(Suit.Hearts, "5"));
    Hand hand = new Hand(cards);
    assertThat(hand.handValueOf())
          .isEqualTo(11 + 5);
  }

  @Test
  public void handWithOneAceAndOtherCardsEqualTo11IsValuedAt1() throws Exception {
    var cards = List.of(new Card(Suit.Clubs, "A"),
                       new Card(Suit.Diamonds, "8"),
                       new Card(Suit.Hearts, "3"));
    Hand hand = new Hand(cards);
    assertThat(hand.handValueOf())
        .isEqualTo(1 + 8 + 3);
  }
  @Test
  public void handWithOneAceAndOtherCardsEqualTo10IsValuedAt1() throws Exception {
    var cards = List.of(new Card(Suit.Clubs, "A"),
            new Card(Suit.Diamonds, "Q"));
    Hand hand = new Hand(cards);
    assertThat(hand.handValueOf())
            .isEqualTo(21);
  }
}
