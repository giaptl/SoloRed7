package cs3500.solored.controller;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents a controller for a solo game of RedSeven that accepts input from the user and
 * transmits output to the user.
 */
public class SoloRedTextController implements RedGameController {

  private final Readable rd;
  private final Appendable ap;
  private boolean playedToCanvasLastRound;

  /**
   * Controller accepts and stores the given readable and appendable objects for doing input and
   * output. Any input coming from the user will be received via the Readable object, and any output
   * sent to the user should be written to the Appendable object by way of a RedGameView. Hint: Look
   * at the Readable and Appendable interfaces to see how to read from and write to them. Ultimately
   * you must figure out a way to transmit a String to an Appendable and read suitable data from a
   * Readable object. The Scanner class will likely be useful, as will the lecture notes on
   * controllers.
   *
   * @throws IllegalArgumentException if the given readable or appendable is null
   */
  public SoloRedTextController(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Readable or appendable cannot be null.");
    }

    this.rd = rd;
    this.ap = ap;
    this.playedToCanvasLastRound = false;

  }

  /**
   * For this method, we are assuming that index counts of hand and palettes START FROM 1 For
   * example, if the hand has 5 cards, the indices are 1, 2, 3, 4, 5 A user trying to play "canvas
   * 2" would be trying to play the second card in their hand A user trying to play "palette 3 4"
   * would be trying to play the fourth card in their hand to the third palette.
   */
  @Override
  public <C extends Card> void playGame(RedGameModel<C> model, List<C> deck, boolean shuffle,
      int numPalettes, int handSize) {
    checkConditions(model, deck, shuffle, numPalettes, handSize);
    Scanner scanner = readInitialInput();
    SoloRedGameTextView view = new SoloRedGameTextView(model, ap);
    renderInitialGameState(model, view);
    if (checkGameOverRightAway(model, view)) {
      return;
    }
    while (!model.isGameOver()) {
      String command;
      try {
        if (!scanner.hasNext()) {
          break;
        }
        command = scanner.next().toLowerCase();
      } catch (NoSuchElementException | IllegalStateException e) {
        throw new IllegalStateException("No more input available.");
      }
      try {
        switch (command) {
          case "q":
            handleGameQuit(model, view);
            return;
          case "palette":
            handlePaletteCommand(model, scanner, view);
            break;
          case "canvas":
            handleCanvasCommand(model, scanner, view);
            break;
          default:
            ap.append("Invalid command. Try again." + command + "\n");
            view.render();
            view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
            continue;
        }
        if (model.isGameOver()) {
          handleGameOverAfterMove(model, view);
          return;
        }
      } catch (IllegalArgumentException | IOException e) {
        try {
          ap.append("Error: ").append(e.getMessage()).append("\n");
        } catch (IOException ex) {
          throw new IllegalStateException("Failed to append error message");
        }
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("No more input available.");
      }
    }
  }

  // Uses scanner to read initial input
  private Scanner readInitialInput() {
    Scanner scanner;
    try {
      scanner = new Scanner(rd);
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Scanner failed to read input.");
    }
    return scanner;
  }

  // Renders the initial game state to the user
  private static <C extends Card> void renderInitialGameState(RedGameModel<C> model,
      SoloRedGameTextView view) {
    try {
      view.render();
      view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to render initial game state", e);
    }
  }

  // Handles the game over state after a move has been entered
  private <C extends Card> void handleGameOverAfterMove(RedGameModel<C> model,
      SoloRedGameTextView view)
      throws IOException {
    ap.append(model.isGameWon() ? "Game won.\n" : "Game lost.\n");
    view.render();
    view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
  }

  // Checks if the game is over right after starting the game
  private <C extends Card> boolean checkGameOverRightAway(RedGameModel<C> model,
      SoloRedGameTextView view) {
    if (model.isGameOver()) {
      try {
        ap.append(model.isGameWon() ? "Game won.\n" : "Game lost.\n");
        view.render();
        view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
      } catch (IOException e) {
        throw new IllegalStateException("Failed to render game state", e);
      }
      return true;
    }
    return false;
  }

  // Handles the palette command (when the user input contains palette)
  private <C extends Card> void handlePaletteCommand(RedGameModel<C> model, Scanner scanner,
      SoloRedGameTextView view) throws IOException {
    int paletteIdx = getValidIndex(scanner) - 1;
    if (paletteIdx == -11) {
      handleGameQuit(model, view);
      return;
    }
    int cardIdx = getValidIndex(scanner) - 1;
    if (cardIdx == -11) {
      handleGameQuit(model, view);
      return;
    }

    try {
      model.playToPalette(paletteIdx, cardIdx);
      playedToCanvasLastRound = false;
    } catch (IllegalArgumentException e) {
      ap.append("Invalid move. Try again. Invalid palette index.\n");
      return;

    } catch (IllegalStateException e) {
      ap.append("Invalid move. Try again. Cannot play to a winning palette.\n");
      return;
    }

    // If game is not over, draw a card and render view
    try {
      if (!model.isGameOver()) {
        model.drawForHand();
        view.render();
        view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to render game state", e);
    }

  }

  // Handles the canvas command (when the user input contains canvas)
  private <C extends Card> void handleCanvasCommand(RedGameModel<C> model, Scanner scanner,
      SoloRedGameTextView view)
      throws IOException {

    if (playedToCanvasLastRound) {
      ap.append("Invalid move. Try again. Cannot play to canvas twice in a row.\n");
      return;
    }

    int cardIdx = getValidIndex(scanner) - 1;

    if (cardIdx == -11) {
      handleGameQuit(model, view);
      return;
    } else if (cardIdx == -1) {
      ap.append("Invalid move. Try again. Invalid card index.\n");
      view.render();
      view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
      return;
    } else if (model.getHand().size() == 1) {
      ap.append("Invalid move. Try again. Cannot play to canvas with 1 card in hand.\n");
      view.render();
      view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
      return;
    }

    // Test play to canvas exceptions
    try {
      model.playToCanvas(cardIdx);
      playedToCanvasLastRound = true;
      view.render();
      view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
    } catch (IllegalStateException e) {
      ap.append("Invalid move. Try again. Cannot play to canvas.\n");
      view.render();
      view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
    } catch (IllegalArgumentException e) {
      ap.append("Invalid move. Try again. Invalid index.\n");
      view.render();
      view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
    }

  }

  // Gets the next valid index from the user input
  private int getValidIndex(Scanner scanner) {
    while (true) {
      String input;
      try {
        // TEST
        if (!scanner.hasNext()) {
          throw new IllegalStateException("No more input available.");
        }
        // TEST*
        input = scanner.next().toLowerCase();
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("No more input available.");
      }

      if (input.equalsIgnoreCase("q")) {
        return -10; // Special value indicating the desire to quit
      }
      try {
        int index = Integer.parseInt(input);
        if (index >= 0) {
          return index;
        }
      } catch (NumberFormatException ignored) {
      }
    }
  }


  // Check if the given parameters are valid for starting the game
  private static <C extends Card> void checkConditions(RedGameModel<C> model, List<C> deck,
      boolean shuffle, int numPalettes, int handSize) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    try {
      model.startGame(deck, shuffle, numPalettes, handSize);
    } catch (IllegalArgumentException | IllegalStateException e) {
      throw new IllegalArgumentException("Game cannot be started.");
    }
  }

  // Handles the game quit command (when the user input contains q or Q)
  private <C extends Card> void handleGameQuit(RedGameModel<C> model, SoloRedGameTextView view)
      throws IOException {
    ap.append("Game quit!\nState of game when quit:\n");
    view.render();
    view.render("\nNumber of cards in deck: " + model.numOfCardsInDeck() + "\n");
  }

}

