package cs3500.solored;

import static org.junit.Assert.assertEquals;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Represents a runner for the Solo Red game.
 */
public class ViewTest {

  private SoloRedGameModel model;
  private StringWriter stringWriter;

  @Before
  public void setUp() {
    model = new SoloRedGameModel();
    List<Card> deck = model.getAllCards();
    model.startGame(deck, true, 4, 5);
    stringWriter = new StringWriter();
  }

  // Test constructor with null model
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullModel() {
    new SoloRedGameTextView(null, stringWriter);
  }

  // Test constructor with null appendable
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullAppendable() {
    new SoloRedGameTextView(model, null);
  }

  //  @Test
  //  public void testRender() throws IOException {
  //    SoloRedGameTextView view = new SoloRedGameTextView(model, stringWriter);
  //    view.render();
  //    String expected = "Canvas: B[]\nP1: \nHand: \n";
  //    assertTrue(expected.contains(stringWriter.toString()));
  //  }
  //
  //  @Test
  //  public void testRenderWithIOException() {
  //    SoloRedGameTextView view = new SoloRedGameTextView(model, null);
  //    assertThrows(IOException.class, view::render);
  //  }

  @Test
  public void testRenderWithDeckDisplay() throws IOException {
    SoloRedGameTextView view = new SoloRedGameTextView(model, stringWriter);
    view.render("Deck: 10 cards\n");
    String expected = "Deck: 10 cards\n";
    assertEquals(expected, stringWriter.toString());
  }

  //  // Test Readable failure behavior
  //  @Test
  //  public void testReadableFailureBehavior() {
  //    Readable failingReadable = cb -> {
  //      throw new IOException("Simulated read failure");
  //    };
  //    Appendable appendable = new StringWriter();
  //    SoloRedTextController controller = new SoloRedTextController(failingReadable, appendable);
  //    RedGameModel<Card> model = new SoloRedGameModel();
  //
  //    Assert.assertThrows(IllegalStateException.class, () -> {
  //      controller.playGame(model, model.getAllCards(), false, 4, 5);
  //    });
  //  }

}
