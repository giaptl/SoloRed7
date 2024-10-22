package cs3500.solored.model.hw04;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.CardNumberColor;
import cs3500.solored.model.hw02.RedGameModel;

import java.util.List;
import java.util.Random;

/**
 * Represents an advanced version of the Solo Red game model.
 */
public class AdvancedSoloRedGameModel extends AbstractSoloRedGameModel implements
        RedGameModel<Card> {

  private int numCardsToDraw;

  /**
   * Constructs a SoloRedGameModel with the default number of palettes (4) and hand size (5).
   */
  public AdvancedSoloRedGameModel() {
    super(new Random());
  }

  /**
   * Constructs a SoloRedGameModel with the given random number generator.
   */
  public AdvancedSoloRedGameModel(Random random) {
    super(random);
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPalettes, int handSize) {
    super.startGame(deck, shuffle, numPalettes, handSize);
  }


  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    super.playToPalette(paletteIdx, cardIdxInHand);
  }


  @Override
  public void playToCanvas(int cardIdxInHand) {
    super.playToCanvas(cardIdxInHand);

    // Retrieves the number of cards in the current winning palette
    int numberOfCardsInWinningPalette = getNumberOfCardsInWinningPalette();

    canvas = hand.remove(cardIdxInHand);
    playedToCanvasThisTurn = true;

    // Check if number on card played to canvas is greater than num cards in winning palette
    if (CardNumberColor.getNumber(canvas) > numberOfCardsInWinningPalette) {
      numCardsToDraw = 2;
    } else {
      numCardsToDraw = 1;
    }

  }

  @Override
  public void drawForHand() {
    super.drawForHand();

    // Check if the canvas was played last turn
    if (numCardsToDraw == 2 && playedToCanvasThisTurn) {
      numCardsToDraw = 1; // Reset to 1 if the canvas was not played last turn
    }

    for (int i = 0; i < numCardsToDraw && !deck.isEmpty(); i++) {
      hand.add(deck.poll());
    }

    numCardsToDraw = 1;
    playedToCanvasThisTurn = false;
  }

}
