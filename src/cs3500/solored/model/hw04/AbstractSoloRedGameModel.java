package cs3500.solored.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cs3500.solored.model.hw02.CardColors;
import cs3500.solored.model.hw02.CardNumberColor;
import cs3500.solored.model.hw02.CardPiece;
import cs3500.solored.model.hw02.Palette;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.Card;

/**
 * Represents an abstract game of SoloRed, to be used by various versions.
 */
public abstract class AbstractSoloRedGameModel implements RedGameModel<Card> {
  protected Deque<Card> deck;
  protected List<Palette> palettes;
  protected Card canvas;
  protected List<Card> hand;
  protected boolean gameOver;
  protected boolean gameStarted;
  protected boolean playedToCanvasThisTurn;
  protected final Random random;
  protected int lastWinningPalette = -1;

  /**
   * Constructs a SoloRedGameModel with the given random number generator.
   *
   * @param random the random number generator
   */
  public AbstractSoloRedGameModel(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null");
    }
    this.random = random;
    gameOver = false;
    gameStarted = false;
    playedToCanvasThisTurn = false;
    canvas = null;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPalettes, int handSize) {
    checkStartGameConditions(deck, numPalettes, handSize);

    if (shuffle) {
      Collections.shuffle(deck, random);
    }

    this.deck = new LinkedList<>(deck);

    initializePalettes(numPalettes);
    initializeHand(handSize);

    this.canvas = new CardPiece(CardColors.R, CardNumbers.ONE);

    gameStarted = true;
  }

  // Check conditions for startGame()
  protected void checkStartGameConditions(List<Card> deck, int numPalettes, int handSize) {
    // Check if the game has started or is over
    if (gameOver || gameStarted) {
      throw new IllegalStateException("Game is over or has already started");
    }
    // Check for invalid number of palettes or hand size
    if (numPalettes < 2 || handSize <= 0) {
      throw new IllegalArgumentException("Invalid number of palettes or hand size");
    }

    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }
    // Check if the deck is large enough
    // Needs to be enough to fill hand to max size and place one card in each palette
    if (deck.size() < (handSize + numPalettes)) {
      throw new IllegalArgumentException("Deck is too small");
    }
    // Check for non-unique
    List<Card> seenCards = new ArrayList<>();
    for (Card card : deck) {
      if (seenCards.contains(card)) {
        throw new IllegalArgumentException("Deck has non-unique cards");
      }
      seenCards.add(card);
    }
    // Check for null cards
    if (deck.contains(null)) {
      throw new IllegalArgumentException("Deck has null cards");
    }
  }

  // Initialize the palettes
  protected void initializePalettes(int numPalettes) {
    this.palettes = new ArrayList<>();
    for (int paletteIdx = 0; paletteIdx < numPalettes; paletteIdx++) {
      Palette palette = new Palette();
      palette.addCard(this.deck.poll());
      this.palettes.add(palette);
    }
  }

  // Initialize the hand
  protected void initializeHand(int handSize) {
    this.hand = new LinkedList<>();
    for (int handIdx = 0; handIdx < handSize; handIdx++) {
      this.hand.add(this.deck.poll());
    }
  }

  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    // Check if game is over or has not started
    if (!gameStarted || gameOver) {
      throw new IllegalStateException("Game is over or has not started.");
    }
    // Check for invalid palette index: refers to palette # index + 1
    // ex. paletteIdx = 0 refers to P1
    if (paletteIdx < 0 || paletteIdx >= palettes.size()) {
      throw new IllegalArgumentException("Invalid palette index");
    }
    // Check for invalid card index
    if (cardIdxInHand < 0 || cardIdxInHand >= hand.size()) {
      throw new IllegalArgumentException("Invalid card index in hand");
    }

    // Check if the palette is winning
    if (isWinning() == paletteIdx) {
      throw new IllegalStateException("Cannot play to a winning palette");
    }

    // Add the card to the palette
    palettes.get(paletteIdx).addCard(hand.remove(cardIdxInHand));

    // Since played to palette, reset the playedToCanvasThisTurn
    playedToCanvasThisTurn = false;

    // Keep track of the last winning palette
    lastWinningPalette = paletteIdx;
  }

  @Override
  public void playToCanvas(int cardIdxInHand) {
    // Check if the game has started or is over
    if (!gameStarted || gameOver) {
      throw new IllegalStateException("Game has not started or is over");
    }
    // Check for invalid card index
    if (cardIdxInHand < 0 || cardIdxInHand >= hand.size()) {
      throw new IllegalArgumentException("Invalid card index in hand");
    }
    // Check if method was already called once in a given turn
    if (playedToCanvasThisTurn) {
      throw new IllegalStateException("Cannot play to canvas more than once per turn");
    }
    // Check if there is exactly one card in hand
    if (hand.size() == 1) {
      throw new IllegalStateException("Cannot play to canvas with exactly one card in hand");
    }
  }

  // Gets the number of cards in the current winning palette
  protected int getNumberOfCardsInWinningPalette() {
    int winningPaletteIndex = isWinning();
    Palette winningPalette = palettes.get(winningPaletteIndex);
    return winningPalette.getSizeOfPalette();
  }

  @Override
  public void drawForHand() {
    // Check if the game has started or is over
    if (!gameStarted || gameOver) {
      throw new IllegalStateException("Game has not started or is over");
    }
  }

  protected void hasGameStarted() {
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started.");
    }
  }


  @Override
  public int numOfCardsInDeck() {
    // Check if the game has started
    hasGameStarted();
    return deck.size();

  }

  @Override
  public int numPalettes() {
    // Check if game has not started
    hasGameStarted();
    return palettes.size();
  }

  @Override
  public int winningPaletteIndex() {
    // Check if game has not started
    hasGameStarted();
    Card ruleCard = canvas;
    if (ruleCard == null) {
      throw new IllegalStateException("No rule card on the canvas");
    }
    return isWinning();
  }


  @Override
  public boolean isGameOver() {
    // Check if game has not started
    hasGameStarted();

    // Check if hand is empty or deck is empty
    if ((hand.isEmpty() && deck.isEmpty())) {
      gameOver = true;
      return true;
    }

    // Check if winning palette has changed or card played to losing palette
    if ((lastWinningPalette != isWinning()) && (lastWinningPalette != -1)) {
      gameOver = true;
      return true;
    }

    // New check for red rule violation
    if (CardNumberColor.getColor(canvas) == CardColors.R) {
      int winningPaletteIndex = isWinning();
      for (int paletteIdx = 0; paletteIdx < palettes.size(); paletteIdx++) {
        if (paletteIdx != winningPaletteIndex) {
          Palette palette = palettes.get(paletteIdx);
          Card lastCard = palette.getCards().get(palette.getCards().size() - 1);
          Card highestCard = palette.getCards().get(0);
          for (Card card : palette.getCards()) {
            if (CardNumberColor.getNumber(card) > CardNumberColor.getNumber(highestCard)) {
              highestCard = card;
            }
          }
          if (CardNumberColor.getNumber(lastCard) < CardNumberColor.getNumber(highestCard)) {
            gameOver = true;
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public boolean isGameWon() {
    // Check if game has not started or is not over
    if (!gameStarted || !gameOver) {
      throw new IllegalStateException("Game has not started or is not over");
    }

    return hand.isEmpty() && deck.isEmpty() && lastWinningPalette == isWinning();
  }

  @Override
  public List<Card> getHand() {
    // Check if game has not started
    hasGameStarted();
    return new ArrayList<>(hand);
  }

  @Override
  public List<Card> getPalette(int paletteNum) {
    // Check if game has not started
    hasGameStarted();
    // Check valid palette index
    if (paletteNum < 0 || paletteNum >= palettes.size()) {
      throw new IllegalArgumentException("Invalid palette index: " + paletteNum);
    }
    // Return copy
    return palettes.get(paletteNum).getCards();
  }


  @Override
  public Card getCanvas() {
    // Check if game has not started
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }
    return canvas;
  }

  @Override
  public List<Card> getAllCards() {
    List<Card> allCards = new ArrayList<>();
    for (CardColors color : CardColors.values()) {
      for (CardNumbers number : CardNumbers.values()) {
        allCards.add(new CardPiece(color, number));
      }
    }
    return allCards;
  }


  /**
   * Determines which palette is winning based on the rule card.
   *
   * @return index of the winning palette
   */
  protected int isWinning() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    // Get the rule card and the selected palette
    Card ruleCard = canvas;
    // Check the rule card color
    switch (CardNumberColor.getColor(ruleCard)) {
      case R:
        return isWinningRed(palettes);
      case O:
        return isWinningOrange(palettes);
      case B:
        return isWinningBlue(palettes);
      case I:
        return isWinningIndigo(palettes);
      case V:
        return isWinningViolet(palettes);
      default:
        throw new IllegalArgumentException("Error. There will always be a winning palette.");
    }
  }

  /**
   * Determines which palette is winning based on the red rule card. Red rule is the highest card
   * wins.
   *
   * @param palettes list of palettes
   * @return index of winning palette
   */
  protected int isWinningRed(List<Palette> palettes) {
    // Initialize variables to track the highest card and its index
    int highestNumberOverall = -2;
    int winningIndex = -1;
    CardColors colorOfWinningCard = null;

    // Iterate through all palettes
    for (Palette palette : palettes) {
      // Iterate through all cards in the palette
      for (Card card : palette.getCards()) {
        int number = CardNumberColor.getNumber(card);
        CardColors color = CardNumberColor.getColor(card);
        if (number > highestNumberOverall || (number == highestNumberOverall
                && color.ordinal() < Objects.requireNonNull(
                colorOfWinningCard).ordinal())) {
          colorOfWinningCard = color;
          highestNumberOverall = number;
          winningIndex = palettes.indexOf(palette);
        }
      }
    }
    return winningIndex;
  }

  // Determines which palette is winning based on the orange rule card.
  // Orange rule is the most one number wins.
  protected int isWinningOrange(List<Palette> palettes) {
    // Step 1: Initialize
    List<Integer> mostOccurrencesInEachPalette = new ArrayList<>();
    List<Integer> tiedPalettes = new ArrayList<>();

    // Step 2: Iterate through all palettes
    for (Palette palette : palettes) {
      // Step 3: Get the most occurrences of a single number in the palette
      mostOccurrencesInEachPalette.add(palette.getMostOfOneNumber());
    }

    int mostOverall = 0;
    int winningIndex = -1;

    // Step 4: Find the most occurrences of a single number in all palettes
    for (Integer occurrence : mostOccurrencesInEachPalette) {
      if (occurrence > mostOverall) {
        mostOverall = occurrence;
        winningIndex = mostOccurrencesInEachPalette.indexOf(occurrence);
        tiedPalettes.clear();
        tiedPalettes.add(winningIndex);
      } else if (occurrence == mostOverall) {
        tiedPalettes.add(mostOccurrencesInEachPalette.indexOf(occurrence));
      }
    }

    return checkForTie(palettes, tiedPalettes, winningIndex);
  }

  // Determines which palette is winning based on the blue rule card.
  // Blue rule is the most different colors wins.
  protected int isWinningBlue(List<Palette> palettes) {
    // Step 1: Initialize
    int mostColorsOverall = 0;
    List<Integer> tiedPalettes = new ArrayList<>();
    int winningIndex = -1;

    for (int palette = 0; palette < palettes.size(); palette++) {
      int numOfDifferentColors = palettes.get(palette).getMostOfDifferentColors();
      if (numOfDifferentColors > mostColorsOverall) {
        mostColorsOverall = numOfDifferentColors;
        winningIndex = palette;
        tiedPalettes.clear();
        tiedPalettes.add(palette);
      } else if (numOfDifferentColors == mostColorsOverall) {
        tiedPalettes.add(palette);
      }
    }

    // Step 3: handle ties
    return checkForTie(palettes, tiedPalettes, winningIndex);
  }

  /**
   * Determines which palette is winning based on the indigo rule card. Indigo rule is the longest
   * run of numbers wins.
   *
   * @param palettes list of palettes
   * @return index of winning palette
   */
  protected int isWinningIndigo(List<Palette> palettes) {
    // Step 1: Initialize
    List<Integer> tiedPalettes = new ArrayList<>();
    int winningIndex = -1;
    int longestRunOverall = 0;

    for (int i = 0; i < palettes.size(); i++) {
      int currentLongestRun = palettes.get(i).getLongestRun();
      if (currentLongestRun > longestRunOverall) {
        longestRunOverall = currentLongestRun;
        winningIndex = i;
        tiedPalettes.clear();
        tiedPalettes.add(i);
      } else if (currentLongestRun == longestRunOverall) {
        tiedPalettes.add(i);
      }
    }

    //Step 4: handle ties
    return checkForTie(palettes, tiedPalettes, winningIndex);
  }

  /**
   * Determines which palette is winning based on the violet rule card. Violet rule is the most
   * cards below 4 wins.
   *
   * @param palettes list of palettes
   * @return index of winning palette
   */
  protected int isWinningViolet(List<Palette> palettes) {
    // Step 1: Initialize
    List<Integer> tiedPalettes = new ArrayList<>();
    int winningIndex = -1;
    int maxLowNumberCount = 0;

    for (int palette = 0; palette < palettes.size(); palette++) {
      int lowNumberCount = palettes.get(palette).getMostBelowFour();
      if (lowNumberCount > maxLowNumberCount) {
        maxLowNumberCount = lowNumberCount;
        winningIndex = palette;
        tiedPalettes.clear();
        tiedPalettes.add(palette);
      } else if (lowNumberCount == maxLowNumberCount) {
        tiedPalettes.add(palette);
      }
    }

    // Step 4: handle ties
    return checkForTie(palettes, tiedPalettes, winningIndex);
  }

  // Helper method to check for ties
  // Used for all color methods except red
  protected int checkForTie(List<Palette> palettes, List<Integer> tiedPalettes, int winningIndex) {
    if (tiedPalettes.size() > 1) {
      List<Palette> tiedPalettesList = new ArrayList<>();
      for (int palette : tiedPalettes) {
        tiedPalettesList.add(palettes.get(palette));
      }
      winningIndex = tiedPalettes.get(isWinningRed(tiedPalettesList));
    }
    return winningIndex;
  }

}
