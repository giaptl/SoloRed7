package cs3500.solored.model.hw04;

import org.junit.Test;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the RedGameCreator class.
 */
public class RedGameCreatorTest {

  @Test
  public void testCreateBasicGame() {
    RedGameModel<Card> game = RedGameCreator.createGame(RedGameCreator.GameType.BASIC);
    assertTrue("Failed to create BASIC game", game instanceof SoloRedGameModel);
  }

  @Test
  public void testCreateAdvancedGame() {
    RedGameModel<Card> game = RedGameCreator.createGame(RedGameCreator.GameType.ADVANCED);
    assertTrue("Failed to create ADVANCED game", game instanceof AdvancedSoloRedGameModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateGameWithNullType() {
    RedGameCreator.createGame(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateGameWithUnknownType() {
    RedGameCreator.createGame(RedGameCreator.GameType.valueOf("not valid"));
  }
}