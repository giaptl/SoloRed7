package cs3500.solored;

import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw04.RedGameCreator;
import cs3500.solored.model.hw04.RedGameCreator.GameType;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Represents a runner for the Solo Red game.
 */
public final class SoloRed {

  /**
   * Main method to run the Solo Red game. If user gives invalid command arguments for palette size
   * or hand size, a default game of 4 palettes and 7 cards in hand will be created for the
   * specified type of game.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    // Default values for palettes and max hand size
    int defaultPalettes = 4;
    int defaultHandSize = 7;

    // Check if the first argument is present
    if (args.length == 0) {
      throw new IllegalArgumentException("You must specify a game type (basic or advanced).");
    }

    String gameType = args[0].toLowerCase();

    // Check if the game type is valid
    if (!gameType.equals("basic") && !gameType.equals("advanced")) {
      throw new IllegalArgumentException("Invalid game type. Must be 'basic' or 'advanced'.");
    }

    // Set the number of palettes and hand size with defaults
    int palettes = defaultPalettes;
    int handSize = defaultHandSize;

    // Check if there are additional arguments for palettes and hand size
    if (args.length >= 2) {
      palettes = Integer.parseInt(args[1]);
      if (palettes < 1) {
        palettes = defaultPalettes;
      }
    }

    if (args.length >= 3) {
      handSize = Integer.parseInt(args[2]);
      if (handSize < 1) {
        handSize = defaultHandSize;
      }
    }

    // Create the game with the specified or default parameters
    setUpGame(gameType, palettes, handSize);

  }

  // Helper method to set up the game based on gameType
  private static void setUpGame(String gameType, int palettes, int handSize) {
    RedGameModel<Card> game;

    // If game is basic
    if (gameType.equals("basic")) {
      game = RedGameCreator.createGame(GameType.BASIC);

      // If game is advanced
    } else if (gameType.equals("advanced")) {
      game = RedGameCreator.createGame(GameType.ADVANCED);
    } else {
      throw new IllegalArgumentException("Unknown game type: " + gameType);
    }

    // Start the game
    List<Card> deck = game.getAllCards();
    Appendable writer = new BufferedWriter(new OutputStreamWriter(System.out));
    SoloRedTextController controller = new SoloRedTextController(new InputStreamReader(System.in),
            writer);
    controller.playGame(game, deck, true, palettes, handSize);
  }

}
