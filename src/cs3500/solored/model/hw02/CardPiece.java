package cs3500.solored.model.hw02;

import java.util.Objects;

import cs3500.solored.model.hw04.CardNumbers;

/**
 * Represents a card piece in the game.
 */
public class CardPiece implements Card {

  private final CardColors color;
  private final CardNumbers number;
  protected static boolean isStartCard = false;

  /**
   * Constructs a card piece with a color and number.
   *
   * @param color  the color of the card
   * @param number the number of the card
   */
  public CardPiece(CardColors color, CardNumbers number) {
    this.color = color;
    this.number = number;
  }

  /**
   * Gets the color of the card.
   *
   * @return the color of the card
   */
  protected CardColors getColor() {
    return color;
  }

  /**
   * Gets the number of the card.
   *
   * @return the number of the card
   */
  protected int getNumber() {
    return number.getValue();
  }

  @Override
  public String toString() {
    if (isStartCard) {
      return color.toString();
    } else {
      return color.toString() + number.getValue();
    }
  }

  /**
   * Sets the start card of canvas.
   *
   * @param startCard boolean value to set the start card
   */
  public static void setStartCard(boolean startCard) {
    isStartCard = startCard;
  }


  @Override
  public boolean equals(Object o) {
    // Check if the object is the same
    if (this == o) {
      return true;
    }
    // Check if the object is null or not an instance of CardPiece
    if (!(o instanceof CardPiece)) {
      return false;
    }
    // Cast the object to a CardPiece
    CardPiece cardPiece = (CardPiece) o;
    return number == cardPiece.number && Objects.equals(color, cardPiece.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, number);
  }
}
