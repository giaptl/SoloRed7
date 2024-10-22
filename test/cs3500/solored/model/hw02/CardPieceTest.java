package cs3500.solored.model.hw02;

import org.junit.Test;

import cs3500.solored.model.hw04.CardNumbers;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the CardPiece class.
 */
public class CardPieceTest {

  // Tests the creation of a valid CardPiece
  @Test
  public void testValidCardPieceCreation() {
    CardPiece card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    assertEquals(CardColors.R, card.getColor());
    assertEquals(5, card.getNumber());
  }

  // Tests the creation of an invalid CardPiece with a null color
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardPieceCreationNullColor() {
    new CardPiece(null, CardNumbers.FIVE);
  }

  // Tests the toString method on a CardPiece
  @Test
  public void testToString() {
    CardPiece card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    assertEquals("R5", card.toString());
  }

  // Tests the toString method on a CardPiece that is a start card
  @Test
  public void testToStringStartCard() {
    CardPiece card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    CardPiece.isStartCard = true;
    assertEquals("R", card.toString());
  }

}
