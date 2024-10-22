package cs3500.solored.model.hw04;

/**
 * Represents the numbers of the cards in the game.
 */
public enum CardNumbers {

  ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

  private final int value;

  CardNumbers(int value) {
    this.value = value;
  }

  /**
   * Gets the value of the card number.
   *
   * @return integer value of the card number
   */
  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
