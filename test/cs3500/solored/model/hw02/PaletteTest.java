package cs3500.solored.model.hw02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

import cs3500.solored.model.hw04.CardNumbers;

/**
 * Test class for the Palette class.
 */
public class PaletteTest {

  private Palette palette;

  @Before
  public void setUp() {
    palette = new Palette();
  }

  // Tests add card to palette works
  @Test
  public void testAddCard() {
    Card card = new CardPiece(CardColors.R, CardNumbers.FIVE);
    palette.addCard(card);
    List<Card> cards = palette.getCards();
    assertEquals(1, cards.size());
    assertEquals(card, cards.get(0));
  }

  // Tests add card to palette with null card
  @Test(expected = IllegalArgumentException.class)
  public void testAddCardNull() {
    palette.addCard(null);
  }

  // Tests getCards for palette
  @Test
  public void testGetCards() {
    Card card1 = new CardPiece(CardColors.R, CardNumbers.FIVE);
    Card card2 = new CardPiece(CardColors.B, CardNumbers.THREE);
    palette.addCard(card1);
    palette.addCard(card2);
    List<Card> cards = palette.getCards();
    assertEquals(2, cards.size());
    assertTrue(cards.contains(card1));
    assertTrue(cards.contains(card2));
  }

  // Tests method that finds occurrences for orange rule
  @Test
  public void testGetMostOfOneNumber() {
    palette.addCard(new CardPiece(CardColors.R, CardNumbers.FIVE));
    palette.addCard(new CardPiece(CardColors.B, CardNumbers.FIVE));
    palette.addCard(new CardPiece(CardColors.O, CardNumbers.THREE));
    assertEquals(2, palette.getMostOfOneNumber());
  }

  // Tests method that finds occurrences for blue rule
  @Test
  public void testGetMostOfDifferentColors() {
    palette.addCard(new CardPiece(CardColors.R, CardNumbers.FIVE));
    palette.addCard(new CardPiece(CardColors.I, CardNumbers.THREE));
    palette.addCard(new CardPiece(CardColors.V, CardNumbers.SEVEN));
    assertEquals(3, palette.getMostOfDifferentColors());
  }

  // Tests method that finds longest run for indigo rule
  @Test
  public void testGetLongestRun() {
    palette.addCard(new CardPiece(CardColors.R, CardNumbers.ONE));
    palette.addCard(new CardPiece(CardColors.B, CardNumbers.TWO));
    palette.addCard(new CardPiece(CardColors.V, CardNumbers.THREE));
    palette.addCard(new CardPiece(CardColors.B, CardNumbers.FIVE));
    assertEquals(3, palette.getLongestRun());
  }

  // Tests method that finds most below four for violet rule
  @Test
  public void testGetMostBelowFour() {
    palette.addCard(new CardPiece(CardColors.R, CardNumbers.ONE));
    palette.addCard(new CardPiece(CardColors.B, CardNumbers.TWO));
    palette.addCard(new CardPiece(CardColors.O, CardNumbers.FOUR));
    palette.addCard(new CardPiece(CardColors.V, CardNumbers.FIVE));
    assertEquals(2, palette.getMostBelowFour());
  }

}
