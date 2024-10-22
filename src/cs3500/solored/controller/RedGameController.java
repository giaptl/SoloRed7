package cs3500.solored.controller;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import java.util.List;

/**
 * Represents a controller for a game of Solo Red.
 */
public interface RedGameController {

  /**
   * Plays a new game of Solo Red with the given model, using the startGame method on the model.
   *
   * @param model       the model to play the game with
   * @param deck        the deck of cards to use for the game
   * @param shuffle     whether to shuffle the deck
   * @param numPalettes the number of palettes to use
   * @param handSize    the size of the hand
   * @throws IllegalArgumentException if the provided model is null
   * @throws IllegalStateException    only if the controller is unable to successfully receive input
   *                                  or transmit output
   * @throws IllegalArgumentException if the game cannot be started
   * @throws IllegalStateException    if the controller is unable to successfully receive input or
   *                                  transmit output
   */
  <C extends Card> void playGame(RedGameModel<C> model, List<C> deck, boolean shuffle,
      int numPalettes, int handSize);


}
