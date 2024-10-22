package cs3500.solored;

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
import cs3500.solored.model.hw04.AbstractModelTest;
import cs3500.solored.model.hw04.AdvancedSoloRedGameModel;
import cs3500.solored.model.hw04.CardNumbers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for implementation-specific details for AdvancedSoloRedGameModel.
 */
public class AdvancedSoloModelTest extends AbstractModelTest {

  @Before
  public void setUp() {
    model = createGameModel();
    deck = model.getAllCards();
  }

  @Override
  protected RedGameModel<Card> createGameModel() {
    return new AdvancedSoloRedGameModel(new Random(42));
  }

  // Test only 1 card is drawn after playing to palette
  @Test
  public void testDrawOneCardAfterPlayToPalette() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();
    int initialDeckSize = model.numOfCardsInDeck();

    model.playToPalette(1, 0);
    model.drawForHand();

    assertEquals(initialHandSize - 1, model.getHand().size());
    assertEquals(initialDeckSize, model.numOfCardsInDeck());
  }

  // Test deck is empty after drawing all cards
  @Test
  public void testNoDrawWhenDeckEmpty() {
    List<Card> smallDeck = new ArrayList<>(List.of());
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.ONE));
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.TWO));
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.THREE));
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.FOUR));
    model.startGame(smallDeck, false, 2, 2);

    model.playToPalette(0, 0);
    model.drawForHand();

    assertEquals(1, model.getHand().size());
    assertEquals(0, model.numOfCardsInDeck());
  }

  // Test drawing two cards after playing to canvas and palette
  @Test
  public void testDrawTwoCardsAfterPlayToCanvasAndPalette() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();
    int initialDeckSize = model.numOfCardsInDeck();

    model.playToCanvas(0);
    model.playToPalette(1, 0);
    model.drawForHand();

    assertEquals(initialHandSize, model.getHand().size());
    assertEquals(initialDeckSize - 2, model.numOfCardsInDeck());
  }

  // Test drawing one card after playing to canvas only
  @Test
  public void testResetToOneCardDrawAfterTwoCardDraw() {
    model.startGame(deck, false, 4, 5);

    // First turn: play high card to canvas, then to palette
    model.playToCanvas(0);
    model.playToPalette(1, 0);
    model.drawForHand();

    int handSizeAfterTwoCardDraw = model.getHand().size();

    // Second turn: play to palette only
    model.playToPalette(2, 0);
    model.drawForHand();

    assertEquals(handSizeAfterTwoCardDraw, model.getHand().size());
  }

  // Test drawing one card after playing to canvas only and card not high enough
  @Test
  public void testDrawOneCardWhenCanvasCardNotHighEnough() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();

    // Play a low card to canvas
    model.playToCanvas(4);
    model.playToPalette(1, 0);
    model.drawForHand();

    assertEquals(initialHandSize, model.getHand().size());
  }

  // Test drawing twice
  @Test
  public void testConsecutiveTwoCardDraws() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();

    // First turn
    model.playToCanvas(0);
    model.playToPalette(1, 0);
    model.drawForHand();

    assertEquals(initialHandSize, model.getHand().size());

    // Second turn
    model.playToCanvas(0);
    model.playToPalette(2, 0);
    model.drawForHand();

    assertEquals(initialHandSize, model.getHand().size());
  }

  // Test deck is empty after drawing all cards
  @Test
  public void testDrawWithAlmostEmptyDeck() {
    List<Card> smallDeck = new ArrayList<>(List.of());
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.ONE));
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.TWO));
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.THREE));
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.FOUR));
    smallDeck.add(new CardPiece(CardColors.R, CardNumbers.FIVE));
    model.startGame(smallDeck, false, 2, 3);

    // Play high card to canvas
    model.playToCanvas(0);
    model.playToPalette(0, 0);
    model.drawForHand();

    assertEquals(1, model.getHand().size());
    assertEquals(0, model.numOfCardsInDeck());
  }

  // Test second draw does NOT happen when it is supposed to because deck is empty
  @Test
  public void testNoExtraDrawAfterPlayToCanvasOnly() {
    model.startGame(deck, false, 4, 5);
    int initialHandSize = model.getHand().size();
    int initialDeckSize = model.numOfCardsInDeck();

    model.playToCanvas(0);

    assertEquals(initialHandSize - 1, model.getHand().size());
    assertEquals(initialDeckSize, model.numOfCardsInDeck());
  }

  // Test if SoloRedGameModel is created with a deck of 4 cards.
  // Game should be lost with 4 red cards in deck  and lower card played on any palette under
  // red rules.
  @Test
  public void testGameLostWithFourRedCardsInDeck() {
    // Create a deck with 4 red cards
    List<Card> deck = new ArrayList<>();
    deck.add(new CardPiece(CardColors.R, CardNumbers.TWO));
    deck.add(new CardPiece(CardColors.R, CardNumbers.THREE));
    deck.add(new CardPiece(CardColors.R, CardNumbers.FOUR));
    deck.add(new CardPiece(CardColors.R, CardNumbers.FIVE));
    deck.add(new CardPiece(CardColors.R, CardNumbers.SIX));

    // Create the game model
    SoloRedGameModel model = new SoloRedGameModel();

    // Start the game with the deck, shuffle set to false, 4 players, and 5 cards per player
    model.startGame(deck, false, 4, 1);

    // Simulate playing a lower card on any palette under red rules
    model.playToPalette(1, 0); // Assuming the first card in hand is lower

    // Check if the game is lost
    assertTrue("The game should be lost with 4 red cards in deck and lower card "
            + "played on any palette under red rules", model.isGameOver());
  }

}
