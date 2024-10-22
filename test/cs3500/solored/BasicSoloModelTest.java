package cs3500.solored;


import static org.junit.Assert.assertEquals;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.model.hw04.AbstractModelTest;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for implementation-specific details.
 */
public class BasicSoloModelTest extends AbstractModelTest {

  @Before
  public void setUp() {
    model = createGameModel();
    deck = model.getAllCards();
  }

  @Override
  public RedGameModel<Card> createGameModel() {
    return new SoloRedGameModel(new Random(42));
  }

  // Test draw method works
  @Test
  public void testDrawForHand() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();
    int initialDeckSize = model.numOfCardsInDeck();
    model.playToPalette(1, 1);
    assertEquals(initialHandSize - 1, model.getHand().size());
    model.drawForHand();
    assertEquals(initialHandSize, model.getHand().size());
    assertEquals(initialDeckSize - 1, model.numOfCardsInDeck());
  }


}