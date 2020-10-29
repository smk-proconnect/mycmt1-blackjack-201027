package com.jitterted.ebp.blackjack;

public enum Suit {
    Spades("♠"),
    Diamonds("♦"),
    Hearts("♥"),
    Clubs("♣");
    private String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }
}