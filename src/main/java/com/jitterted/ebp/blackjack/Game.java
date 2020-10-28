package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

  public static final int INITIAL_NUMBER_OF_DEALS = 2;
  private final Deck deck;

  private final List<Card> dealerHand = new ArrayList<>();
  private final List<Card> playerHand = new ArrayList<>();

  public static void main(String[] args) {
    Game game = new Game();
    displayGreetings();
    game.initialDeal();
    game.play();
    resetDisplay();
  }

  private static void resetDisplay() {
    System.out.println(ansi().reset());
  }

  private static void displayGreetings() {
    System.out.println(ansi()
                           .bgBright(Ansi.Color.WHITE)
                           .eraseScreen()
                           .cursor(1, 1)
                           .fgGreen().a("Welcome to")
                           .fgRed().a(" Jitterted's")
                           .fgBlack().a(" BlackJack"));
  }

  public Game() {
    deck = new Deck();
  }

  public void initialDeal() {
    deal(INITIAL_NUMBER_OF_DEALS);
  }

  private void deal(int numberOfDeals) {
    for(int i=0; i<numberOfDeals; i++) {
      dealTo(playerHand);
      dealTo(dealerHand);
    }
  }

  private void dealTo(List<Card> playerHand) {
    playerHand.add(deck.draw());
  }


  public void play() {
    // get Player's decision: hit until they stand, then they're done (or they go bust)
    boolean playerBusted = playerTurn();

    // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
    dealerTurn(playerBusted);

    displayFinalGameState();

    displayPlayerBustedState(playerBusted);
  }

  private boolean playerTurn() {
    boolean playerBusted = false;
    while (!playerBusted) {
      displayGameState();
      String playerChoice = getPlayerChoice();
      if (isPlayerWantToStand(playerChoice)) {
        break;
      }
      playerBusted = isPlayerBusted(playerChoice);
    }
    return playerBusted;
  }

  private boolean isPlayerBusted(String playerChoice) {
    boolean playerBusted = false;
    if (isPlayerWantToHit(playerChoice)) {
      dealTo(playerHand);
      playerBusted = isPlayerBusted();
    } else {
      System.out.println("You need to [H]it or [S]tand");
    }
    return playerBusted;
  }

  private void displayPlayerBustedState(boolean playerBusted) {
    if (playerBusted) {
      System.out.println("You Busted, so you lose.  💸");
    } else if (handValueOf(dealerHand) > 21) {
      System.out.println("Dealer went BUST, Player wins! Yay for you!! 💵");
    } else if (handValueOf(dealerHand) < handValueOf(playerHand)) {
      System.out.println("You beat the Dealer! 💵");
    } else if (handValueOf(dealerHand) == handValueOf(playerHand)) {
      System.out.println("Push: The house wins, you Lose. 💸");
    } else {
      System.out.println("You lost to the Dealer. 💸");
    }
  }

  private void dealerTurn(boolean playerBusted) {
    if (!playerBusted) {
      while (handValueOf(dealerHand) <= 16) {
        dealTo(dealerHand);
      }
    }
  }

  private boolean isPlayerBusted() {
    boolean playerBusted = false;
    if (handValueOf(playerHand) > 21) {
      playerBusted = true;
    }
    return playerBusted;
  }

  private String getPlayerChoice() {
    return inputFromPlayer().toLowerCase();
  }

  private boolean isPlayerWantToHit(String playerChoice) {
    return playerChoice.startsWith("h");
  }

  private boolean isPlayerWantToStand(String playerChoice) {
    return playerChoice.startsWith("s");
  }

  public int handValueOf(List<Card> hand) {
    int handValue = sumOfAllCardsOnHand(hand);

    // does the hand contain at least 1 Ace?
    boolean hasAce = IsPlayerHasAce(hand);

    // if the total hand value <= 11, then count the Ace as 11 by adding 10
    handValue = addAceValue(handValue, hasAce);

    return handValue;
  }

  private int sumOfAllCardsOnHand(List<Card> hand) {
    return hand
            .stream()
            .mapToInt(Card::rankValue)
            .sum();
  }

  private int addAceValue(int handValue, boolean hasAce) {
    if (hasAce && handValue < 11) {
      handValue += 10;
    }
    return handValue;
  }

  private boolean IsPlayerHasAce(List<Card> hand) {
    return hand
            .stream()
            .anyMatch(card -> card.rankValue() == 1);
  }

  private String inputFromPlayer() {
    System.out.println("[H]it or [S]tand?");
    Scanner scanner = new Scanner(System.in);
    return scanner.nextLine();
  }

  private void displayGameState() {
    displayFaceUpCard();

    // second card is the hole card, which is hidden
    displayBackOfCard();

    System.out.println();
    System.out.println("Player has: ");
    displayHand(playerHand);
    System.out.println(" (" + handValueOf(playerHand) + ")");
  }

  private void displayFaceUpCard() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    System.out.println(dealerHand.get(0).display()); // first card is Face Up
  }

  private void displayBackOfCard() {
    System.out.print(
        ansi()
            .cursorUp(7)
            .cursorRight(12)
            .a("┌─────────┐").cursorDown(1).cursorLeft(11)
            .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
            .a("│░ J I T ░│").cursorDown(1).cursorLeft(11)
            .a("│░ T E R ░│").cursorDown(1).cursorLeft(11)
            .a("│░ T E D ░│").cursorDown(1).cursorLeft(11)
            .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
            .a("└─────────┘"));
  }

  private void displayHand(List<Card> hand) {
    System.out.println(hand.stream()
                           .map(Card::display)
                           .collect(Collectors.joining(
                               ansi().cursorUp(6).cursorRight(1).toString())));
  }

  private void displayFinalGameState() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    displayHand(dealerHand);
    System.out.println(" (" + handValueOf(dealerHand) + ")");

    System.out.println();
    System.out.println("Player has: ");
    displayHand(playerHand);
    System.out.println(" (" + handValueOf(playerHand) + ")");
  }
}
