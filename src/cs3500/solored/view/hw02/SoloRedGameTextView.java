package cs3500.solored.view.hw02;

import cs3500.solored.model.hw02.CardPiece;
import cs3500.solored.model.hw02.RedGameModel;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Represents the text view for a solo game of RedSeven.
 */
public class SoloRedGameTextView implements RedGameView {

  private final RedGameModel<?> model;
  private final Appendable appendable;

  /**
   * Constructs a text view for a solo game of RedSeven.
   */
  public SoloRedGameTextView(RedGameModel<?> model) {
    this.model = model;
    this.appendable = null;
  }

  /**
   * Constructs a text view for a solo game of RedSeven with an Appendable.
   */
  public SoloRedGameTextView(RedGameModel<?> model, Appendable appendable) {
    if (appendable == null || model == null) {
      throw new IllegalArgumentException("Appendable or model cannot be null.");
    }
    this.model = model;
    this.appendable = appendable;
  }

  /**
   * Returns a string representation of the game state.
   *
   * @return game state in string form
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();

    // Display the canvas or rule card
    CardPiece.setStartCard(true);
    sb.append("Canvas: ")
        .append(model.getCanvas().toString().replaceAll("\\d", "")).append("\n");
    CardPiece.setStartCard(false);

    // Display palettes
    for (int i = 0; i < model.numPalettes(); i++) {
      if (i == model.winningPaletteIndex()) {
        sb.append("> ");
      }
      sb.append("P").append(i + 1).append(": ");
      // Display cards in palette
      var paletteCards = model.getPalette(i);
      for (int j = 0; j < paletteCards.size(); j++) {
        if (j > 0) {
          sb.append(" ");
        }
        sb.append(paletteCards.get(j).toString());
      }
      sb.append("\n");
    }

    // Display the player's hand
    sb.append("Hand: ");
    var handCards = model.getHand();
    for (int i = 0; i < handCards.size(); i++) {
      sb.append(handCards.get(i).toString());
      if (i < handCards.size() - 1) {
        sb.append(" ");
      }
    }

    return sb.toString();
  }

  /**
   * Renders the current game state to the provided Appendable.
   *
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render() throws IOException {
    if (appendable != null) {
      appendable.append(this.toString());
      if (appendable instanceof BufferedWriter) {
        ((BufferedWriter) appendable).flush();
      }
    } else {
      throw new IOException("Rendering has failed.");
    }
  }

  /**
   * Renders the provided deck display to the Appendable.
   * @param deckDisplay the deck display to render
   * @throws IOException if the rendering fails for some reason
   */
  public void render(String deckDisplay) throws IOException {
    if (appendable != null) {
      appendable.append(deckDisplay);
      if (appendable instanceof BufferedWriter) {
        ((BufferedWriter) appendable).flush();
      }
    } else {
      throw new IOException("Rendering has failed for deck display.");
    }
  }

}

