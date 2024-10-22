package cs3500.solored;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for properties of the public model interface.
 */
public class ModelTest {

  private RedGameModel<Card> model;
  private List<Card> deck;

  @Before
  public void setUp() {
    model = new SoloRedGameModel();
    deck = model.getAllCards();
  }

  // Test that hand is empty when game starts
  @Test
  public void testEmptyHandAtStart() {
    model.startGame(deck, true, 4, 5);
    for (int i = 0; i < 4; i++) {
      assertFalse(model.getHand().isEmpty());
    }
  }

  // Test invalid palette index
  @Test
  public void testInvalidPaletteIndex() {
    model.startGame(deck, true, 4, 5);
    assertThrows(IllegalArgumentException.class,
        () -> model.playToPalette(-1, 0));
    assertThrows(IllegalArgumentException.class,
        () -> model.playToPalette(5, 0));
  }

  // Test invalid card index in hand
  @Test
  public void testInvalidCardIndexInHand() {
    model.startGame(deck, true, 4, 5);
    assertThrows(IllegalArgumentException.class,
        () -> model.playToPalette(0, -1));
    assertThrows(IllegalArgumentException.class,
        () -> model.playToPalette(0, 10));
  }

  // Test starting the game with invalid parameters
  @Test
  public void testStartGameInvalidParameters() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(model.getAllCards(), false, 1, 5));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(model.getAllCards(), false, 4, 0));
  }

  // Test game is not over during play
  @Test
  public void testGameNotOver() {
    model.startGame(deck, false, 4, 5);
    assertFalse(model.isGameOver());
    for (int i = 0; i < 4; i++) {
      model.playToPalette(i, 0);
    }
  }

  // Test palette works
  @Test
  public void testPaletteSizeIncreasesWhenPlayed() {
    model.startGame(deck, false, 4, 5);
    int size = model.getPalette(0).size();
    model.playToPalette(0, 0);
    assertEquals(size + 1, model.getPalette(0).size());
  }

  // Test different size palette and hand sizes
  @Test
  public void testDifferentSizePaletteAndHand() {
    model.startGame(deck, false, 3, 5);
    assertEquals(3, model.numPalettes());
    assertEquals(5, model.getHand().size());
  }

  // Test different size palette and hand sizes
  @Test
  public void testDifferentSizePaletteAndHand2() {
    model.startGame(deck, false, 2, 9);
    assertEquals(2, model.numPalettes());
    assertEquals(9, model.getHand().size());
  }

}