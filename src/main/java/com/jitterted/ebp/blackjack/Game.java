package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

  public static final int INITIAL_NUMBER_OF_DEALS = 2;
  private final Deck deck;

  private final PlayerHand playerHand = new PlayerHand();
  private final DealerHand dealerHand = new DealerHand();

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
    deal();
  }

  private void deal() {
    for(int i = 0; i< Game.INITIAL_NUMBER_OF_DEALS; i++) {
      dealerHand.drawFromDeck(deck);
      playerHand.drawFromDeck(deck);
    }
  }


  public void play() {
    // get Player's decision: hit until they stand, then they're done (or they go bust)
    boolean playerBusted = playerTurn();
    if (playerBusted) {
      System.out.println("You Busted, so you lose.  💸");
    }
      // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
    dealerHand.dealerTurn(playerBusted,deck);

    displayFinalGameState();

    playerHand.displayPlayerBustedState(dealerHand);
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
      playerHand.drawFromDeck(deck);
      playerBusted = playerHand.isPlayerBusted();
    } else {
      System.out.println("You need to [H]it or [S]tand");
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

  private String inputFromPlayer() {
    System.out.println("[H]it or [S]tand?");
    Scanner scanner = new Scanner(System.in);
    return scanner.nextLine();
  }

  private void displayGameState() {
    dealerHand.displayFirstCard();

    // second card is the hole card, which is hidden
    displayBackOfCard();

    System.out.println();
    System.out.println("Player has: ");
    playerHand.displayHand();
    System.out.println(" (" + playerHand.value() + ")");
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

  private void displayFinalGameState() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    playerHand.displayHand();
    System.out.println(" (" + playerHand.value() + ")");

    System.out.println();
    System.out.println("Player has: ");
    playerHand.displayHand();
    System.out.println(" (" + playerHand.value() + ")");
  }
}
