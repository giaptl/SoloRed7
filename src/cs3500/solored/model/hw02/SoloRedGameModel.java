package cs3500.solored.model.hw02;

import java.util.List;
import java.util.Random;

import cs3500.solored.model.hw04.AbstractSoloRedGameModel;

/**
 * Represents a game of SoloRed.
 */
public class SoloRedGameModel extends AbstractSoloRedGameModel implements RedGameModel<Card> {

  private int assignedMaxHandSize;

  /**
   * Constructs a SoloRedGameModel with the default number of palettes (4) and hand size (5).
   */
  public SoloRedGameModel() {
    super(new Random());
  }

  /**
   * Constructs a SoloRedGameModel with the given random number generator.
   */
  public SoloRedGameModel(Random random) {
    super(random);
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPalettes, int handSize) {
    super.startGame(deck, shuffle, numPalettes, handSize);
    this.assignedMaxHandSize = handSize;
  }


  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    super.playToPalette(paletteIdx, cardIdxInHand);
  }


  @Override
  public void playToCanvas(int cardIdxInHand) {
    super.playToCanvas(cardIdxInHand);
    canvas = hand.remove(cardIdxInHand);
    playedToCanvasThisTurn = true;
  }

  @Override
  public void drawForHand() {
    super.drawForHand();
    while (hand.size() < assignedMaxHandSize && !deck.isEmpty()) {
      Card drawnCard = deck.poll();
      hand.add(drawnCard);
    }
    playedToCanvasThisTurn = false;
  }

}