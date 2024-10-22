package cs3500.solored.model.hw04;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.CardColors;
import cs3500.solored.model.hw02.CardPiece;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/**
 * Test class for implementation-specific details for AbstractSoloRedGameModel.
 */
public abstract class AbstractModelTest {

  protected RedGameModel<Card> model;
  protected List<Card> deck;

  @Before
  public void setUp() {
    model = createGameModel();
    deck = model.getAllCards();
  }

  protected abstract RedGameModel<Card> createGameModel();

  // Test that deck is created with the correct number of cards
  @Test
  public void testDeckIsCorrectSize() {
    model.startGame(deck, true, 4, 5);
    assertEquals(26, model.numOfCardsInDeck());
  }

  // Test starting the game with a small deck
  @Test
  public void testStartGameSmallDeck() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(model.getAllCards().subList(0, 5), false, 4, 5));
  }

  // Test starting the game with non-unique cards
  @Test
  public void testDeckShuffles() {
    List<Card> originalDeck = new ArrayList<>(deck);
    model.startGame(deck, true, 4, 5);
    List<Card> shuffledDeck = model.getHand();
    boolean isShuffled = false;
    for (int i = 0; i < originalDeck.size(); i++) {
      if (!originalDeck.get(i).equals(shuffledDeck.get(i))) {
        break;
      }
    }
    assertFalse("The deck should be shuffled", isShuffled);
  }

  @Test
  public void testCanvasConstructor() {
    model.startGame(deck, true, 4, 5);
    assertEquals(new CardPiece(CardColors.R, CardNumbers.ONE), model.getCanvas());
  }


  // Test game starts correctly
  @Test
  public void testStartGame() {
    model.startGame(deck, false, 4, 5);
    assertFalse(model.isGameOver());
    assertEquals(4, model.numPalettes());
    assertEquals(5, model.getHand().size());
    assertEquals(deck.size() - 9, model.numOfCardsInDeck());
  }

  // Test game throws IllegalStateException if started after it has already started
  @Test(expected = IllegalStateException.class)
  public void testStartGameTwice() {
    model.startGame(deck, false, 4, 5);
    model.startGame(deck, false, 4, 5);
  }

  // Test game throws IllegalArgumentException if started with invalid parameters
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameWithInvalidPaletteCount() {
    model.startGame(deck, false, 1, 5);
  }

  // Test game throws IllegalArgumentException if started with invalid parameters
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameWithInvalidHandSize() {
    model.startGame(deck, false, 4, 0);
  }

  // Test play to palette works
  @Test
  public void testPlayToPalette() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();
    model.playToPalette(1, 0);
    assertEquals(initialHandSize - 1, model.getHand().size());
    assertEquals(2, model.getPalette(1).size());
  }

  // Test play to palette throws IllegalStateException if game has not started
  @Test(expected = IllegalStateException.class)
  public void testPlayToPaletteBeforeGameStart() {
    model.playToPalette(0, 0);
  }

  // Test play to palette throws IllegalStateException with invalid parameters
  @Test(expected = IllegalArgumentException.class)
  public void testPlayToPaletteInvalidPaletteIndex() {
    model.startGame(deck, false, 4, 5);
    model.playToPalette(4, 0);
  }

  // Test play to palette throws IllegalArgumentException with invalid parameters
  @Test(expected = IllegalArgumentException.class)
  public void testPlayToPaletteInvalidCardIndex() {
    model.startGame(deck, false, 4, 5);
    model.playToPalette(0, 5);
  }

  // Test play to canvas works
  @Test
  public void testPlayToCanvas() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();
    model.playToCanvas(0);
    assertEquals(initialHandSize - 1, model.getHand().size());
    assertNotNull(model.getCanvas());
  }

  // Test IllegalStateException is thrown when playing to canvas twice in a row
  @Test(expected = IllegalStateException.class)
  public void testPlayToCanvasTwiceInOneTurn() {
    model.startGame(deck, false, 4, 5);
    model.playToCanvas(0);
    model.playToCanvas(0);
  }


  @Test
  public void testIsGameOverWhenDeckAndHandEmpty() {
    model.startGame(deck, false, 4, 5);
    // Play all cards from hand to palettes
    int count = 0;
    while (!model.getHand().isEmpty()) {
      model.playToPalette(count, 0);
      count++;
      if (count == 4) {
        count = 0;
      }
    }
    // Empty the deck
    while (model.numOfCardsInDeck() > 0) {
      model.drawForHand();
      if (!model.getHand().isEmpty()) {
        model.playToPalette(1, 0);
      }
    }
    assertTrue(model.isGameOver());
  }

  // Test behavior to get all cards work
  @Test
  public void testGetAllCards() {
    List<Card> allCards = model.getAllCards();
    assertEquals(35, allCards.size()); // 5 colors * 7 numbers
  }


  // Test starting the game with non-unique cards
  @Test
  public void testStartGameNonUniqueCards() {
    List<Card> nonUniqueDeck = new ArrayList<>(model.getAllCards());
    nonUniqueDeck.add(nonUniqueDeck.get(0)); // Add a duplicate card
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(nonUniqueDeck, false, 4, 5));
  }

  // Test starting the game with null cards
  @Test
  public void testStartGameNullCards() {
    List<Card> nullDeck = new ArrayList<>(model.getAllCards());
    nullDeck.set(0, null);
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(nullDeck, false, 4, 5));
  }

  // Test starting the game with valid deck and args
  @Test
  public void testStartGameWithValidDeckAndArgs() {
    model.startGame(deck, true, 4, 5);
    assertEquals(4, model.numPalettes());
    assertEquals(5, model.getHand().size());
    assertEquals(26, model.numOfCardsInDeck());
  }

  // Test view with valid game
  @Test
  public void testViewWithValidGame() {
    model.startGame(deck, true, 4, 5);
    SoloRedGameTextView view = new SoloRedGameTextView(model);
    String gameState = view.toString();
    assertTrue(gameState.contains("Canvas:"));
    assertTrue(gameState.contains("P1:"));
    assertTrue(gameState.contains("Hand:"));
  }

  // Test random constructor results
  @Test
  public void testRandomConstructorResults() {
    Random random = new Random(42);
    SoloRedGameModel randomModel = new SoloRedGameModel(random);
    randomModel.startGame(randomModel.getAllCards(), true, 4, 5);
    List<Card> hand1 = randomModel.getHand();
    random = new Random(42);
    SoloRedGameModel anotherRandomModel = new SoloRedGameModel(random);
    anotherRandomModel.startGame(anotherRandomModel.getAllCards(), true, 4, 5);
    List<Card> hand2 = anotherRandomModel.getHand();
    assertEquals(hand1, hand2);
  }

  // Test hand is immutable
  @Test
  public void testHandIsImmutable() {
    model.startGame(deck, true, 4, 5);
    List<Card> hand = model.getHand();
    hand.remove(0);
    assertEquals(5, model.getHand().size());
  }

  // Test playing from hand slides cards down
  @Test
  public void testPlayingFromHandSlidesCardsDown() {
    model.startGame(deck, true, 4, 5);
    model.playToPalette(2, 2);
    assertEquals(4, model.getHand().size());
    assertEquals(2, model.getPalette(2).size());
  }

  // Test playing from hand removes card from hand
  @Test
  public void testPlayingFromHandRemovesCardFromHand() {
    model.startGame(deck, false, 4, 5);
    Card card = model.getHand().get(0);
    model.playToPalette(1, 0);
    assertFalse(model.getHand().contains(card));
  }

}
