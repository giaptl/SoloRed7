package cs3500.solored.model.hw02;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cs3500.solored.model.hw04.CardNumbers;

/**
 * Test class for the Card class.
 */
public class CardTest {

  // Tests the creation of a valid Card
  @Test
  public void testValidCardCreation() {
    Card card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    assertEquals(CardColors.R, CardNumberColor.getColor(card));
    assertEquals(5, CardNumberColor.getNumber(card));
  }

  // Tests the toString() method on a Card
  @Test
  public void testToString() {
    Card card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    assertEquals("R5", card.toString());
  }

}
