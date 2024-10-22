package cs3500.solored.model.hw02;

/**
 * Checks if the given Card implementation is an instance of CardPiece.
 */
public class CardNumberColor {

  /**
   * Gets the number of the cars.
   *
   * @param card the card to get the number from
   * @throws IllegalArgumentException if the card is not a CardPiece
   */
  public static int getNumber(Card card) {
    if (card instanceof CardPiece) {
      return ((CardPiece) card).getNumber();
    }
    throw new IllegalArgumentException("Card is not a CardPiece");

  }

  /**
   * Gets the color of the cars.
   *
   * @param card the card to get the color from
   * @throws IllegalArgumentException if the card is not a CardPiece
   */
  public static CardColors getColor(Card card) {
    if (card instanceof CardPiece) {
      return ((CardPiece) card).getColor();
    }
    throw new IllegalArgumentException("Card is not a CardPiece");

  }

}
