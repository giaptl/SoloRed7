package cs3500.solored.model.hw04;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * Represents a creator for RedGameModel instances.
 */
public class RedGameCreator {

  /**
   * Enum to define the possible game types.
   */
  public enum GameType {
    BASIC, ADVANCED
  }

  /**
   * Static method that returns an instance (of an appropriate subclass of) RedGameModel, depending
   * on GameType given.
   *
   * @param gameType type of Game (basic or advanced)
   * @return specified instance of RedGameModel
   * @throws IllegalArgumentException if gameType is null
   */
  public static RedGameModel<Card> createGame(GameType gameType) {
    if (gameType == null) {
      throw new IllegalArgumentException("GameType cannot be null");
    }
    switch (gameType) {
      case BASIC:
        return new SoloRedGameModel();
      case ADVANCED:
        return new AdvancedSoloRedGameModel();
      default:
        throw new IllegalArgumentException("Unknown GameType: " + gameType);
    }

  }

}
