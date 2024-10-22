package cs3500.solored;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.CardColors;
import cs3500.solored.model.hw02.CardPiece;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.model.hw04.AdvancedSoloRedGameModel;
import cs3500.solored.model.hw04.CardNumbers;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;

/**
 * Test class for the controller of the RedSeven game.
 */
public class ControllerTest {

  private RedGameModel<Card> model;
  private List<Card> deck;
  private StringWriter output;

  @Before
  public void setUp() {
    model = new SoloRedGameModel();
    deck = new ArrayList<>(); // Populate with mock cards
    output = new StringWriter();
  }

  // Test constructor with null readable
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullReadable() {
    new SoloRedTextController(null, new StringWriter());
  }

  // Test constructor with null appendable
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullAppendable() {
    new SoloRedTextController(new StringReader(""), null);
  }

  // Test playGame with null model
  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameWithNullModel() {
    SoloRedTextController controller = new SoloRedTextController(new StringReader(""), output);
    controller.playGame(null, deck, false, 4, 7);
  }

  // Test playGame with negative parameters
  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameWithInvalidStartGameParameters() {
    SoloRedTextController controller = new SoloRedTextController(new StringReader(""), output);
    controller.playGame(model, deck, false, -1, 7); // Invalid numPalettes
    controller.playGame(model, deck, false, 4, -1); // Invalid handSize
  }

  // Test playGame with quit command right away
  @Test
  public void testPlayGameWithQuitCommand() {
    StringReader input = new StringReader("q");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, true, 4, 5);
    assertTrue(writer.toString().contains("Game quit!"));
  }

  // Test playGame with quit command mid input
  @Test
  public void testPlayGameWithQuitCommandMidInput() {
    StringReader input = new StringReader("canvas q 1");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, true, 4, 5);
    assertTrue(writer.toString().contains("Game quit!"));
  }

  // Test playGame with quit different way
  @Test
  public void testPlayGameWithQuitUppercaseQ() {
    StringReader input = new StringReader("Q");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, true, 4, 5);
    assertTrue(writer.toString().contains("Game quit!"));
  }

  // Test playGame with quit command uppercase mid input
  @Test
  public void testPlayGameWithQuitUppercaseCommandMidInput() {
    StringReader input = new StringReader("canvas\nQ\n1");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, true, 4, 5);
    assertTrue(writer.toString().contains("Game quit!"));
  }

  // Test playGame with invalid command
  @Test
  public void testPlayGameWithInvalidCommand() {
    StringReader input = new StringReader("gkewragbragn");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, false, 4, 5);
    assertTrue(writer.toString().contains("Invalid command. Try again."));
  }

  // Test playGame with a valid palette move input
  @Test
  public void testPlayGameWithValidPaletteMove() {
    StringReader input = new StringReader("palette 1 1");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, false, 4, 5);
    assertEquals(2, model.getPalette(0).size());
  }

  // Test playGame with a valid palette move with invalid symbols in the middle
  @Test
  public void testPlayGameWithInvalidCommandsMidValidPaletteMove() {
    StringReader input = new StringReader("palette $?\n1\n-1\n3");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, false, 3, 8);
    assertEquals(2, model.getPalette(0).size());
  }

  // Test playGame with an invalid palette index
  @Test
  public void testPlayGameWithInvalidPaletteMove() {
    StringReader input = new StringReader("palette 10 1");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, false, 4, 5);
    assertTrue(writer.toString().contains("Invalid move. Try again. Invalid palette index."));
  }

  // Test playGame with a valid canvas input
  @Test
  public void testPlayGameWithValidCanvasMove() {
    StringReader input = new StringReader("canvas 1");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);

    // Initialize the model and deck properly
    SoloRedGameModel game = new SoloRedGameModel(new Random());
    List<Card> deck = game.getAllCards();

    // Ensure the deck is not empty and has valid cards
    if (deck.isEmpty()) {
      deck.add(new CardPiece(CardColors.R, CardNumbers.ONE));
      deck.add(new CardPiece(CardColors.O, CardNumbers.TWO));
      deck.add(new CardPiece(CardColors.B, CardNumbers.THREE));
    }

    controller.playGame(game, deck, false, 4, 5);
    Card canvas = game.getCanvas();
    assertNotNull(canvas);
    assertEquals("R5", canvas.toString());
  }

  // Test playGame with an invalid canvas input
  @Test
  public void testPlayGameWithInvalidCanvasMove() {
    StringReader input = new StringReader("canvas 1 canvas 2 q");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    SoloRedGameModel game = new SoloRedGameModel(new Random());
    List<Card> deck = game.getAllCards();
    controller.playGame(game, deck, false, 4, 5);
    assertTrue(writer.toString().contains("Cannot play to canvas twice in a row"));
  }

  // Test controller works with basic model
  @Test
  public void testControllerWithBasicModel() {
    StringReader input = new StringReader("canvas 1\npalette 1 1\nq");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    List<Card> deck = model.getAllCards();
    controller.playGame(model, deck, false, 4, 5);
    assertTrue(writer.toString().contains("Game quit!"));
  }

  // Test controller works advanced model
  @Test
  public void testControllerWithAdvancedModel() {
    StringReader input = new StringReader("canvas 1\npalette 1 1\nq");
    Appendable writer = new StringWriter();
    SoloRedTextController controller = new SoloRedTextController(input, writer);
    RedGameModel<Card> advancedModel = new AdvancedSoloRedGameModel();
    List<Card> deck = advancedModel.getAllCards();
    controller.playGame(advancedModel, deck, false, 4, 5);
    assertTrue(writer.toString().contains("Game quit!"));
  }
}
