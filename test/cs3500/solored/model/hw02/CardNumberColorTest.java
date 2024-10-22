package cs3500.solored.model.hw02;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cs3500.solored.model.hw04.CardNumbers;

/**
 * Test class for the CardNumberColor enum.
 */
public class CardNumberColorTest {

  // Tests getNumber with a valid CardPiece
  @Test
  public void testGetNumberValidCardPiece() {
    CardPiece card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    int number = CardNumberColor.getNumber(card);
    assertEquals(5, number);
  }

  // Tests getNumber with an invalid card
  @Test(expected = IllegalArgumentException.class)
  public void testGetNumberInvalidCard() {
    Card card = new Card() {
    }; // Anonymous class implementing Card
    CardNumberColor.getNumber(card);
  }

  // Tests getColor with a valid CardPiece
  @Test
  public void testGetColorValidCardPiece() {
    CardPiece card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    CardColors color = CardNumberColor.getColor(card);
    assertEquals(CardColors.R, color);
  }

  // Tests getColor with an invalid card
  @Test(expected = IllegalArgumentException.class)
  public void testGetColorInvalidCard() {
    Card card = new Card() {
    }; // Anonymous class implementing Card
    CardNumberColor.getColor(card);
  }

}
