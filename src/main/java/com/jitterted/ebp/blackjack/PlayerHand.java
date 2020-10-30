package com.jitterted.ebp.blackjack;

import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class PlayerHand extends Hand{
    void displayHand() {
        System.out.println(cards.stream()
                .map(Card::display)
                .collect(Collectors.joining(
                        ansi().cursorUp(6).cursorRight(1).toString())));
    }

    public void displayPlayerBustedState(Hand dealerHand) {
      if (dealerHand.value() < value()) {
        System.out.println("You beat the Dealer! 💵");
      } else if (dealerHand.value() == value()) {
        System.out.println("Push: The house wins, you Lose. 💸");
      } else {
        System.out.println("You lost to the Dealer. 💸");
      }
    }

    public boolean isPlayerBusted() {
        boolean playerBusted = false;
        if (value() > 21) {
            playerBusted = true;
        }
        return playerBusted;
    }
}
