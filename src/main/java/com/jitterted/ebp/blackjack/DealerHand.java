package com.jitterted.ebp.blackjack;

import static org.fusesource.jansi.Ansi.ansi;

public class DealerHand extends Hand{
    public void displayFirstCard() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        System.out.println(cards.get(0).display()); // first card is Face Up
    }

    public void dealerTurn(boolean playerBusted,Deck deck) {
        if (!playerBusted) {
            while (handValueOf() <= 16) {
                drawFromDeck(deck);
            }
        }
    }

}
