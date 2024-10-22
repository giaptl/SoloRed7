package cs3500.solored.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a palette of cards in the game.
 */
public class Palette {

  private final List<Card> cards;

  /**
   * Constructs a palette of cards.
   */
  public Palette() {
    this.cards = new ArrayList<>();
  }

  /**
   * Adds a card to the palette.
   *
   * @param card the Card to add
   */
  public void addCard(Card card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    cards.add(card);
  }

  /**
   * Returns a copy of the cards in the palettes.
   *
   * @return list of Card
   */
  public List<Card> getCards() {
    return new ArrayList<>(cards); // Defensive copy
  }

  /**
   * Returns the size cards in the palettes.
   *
   * @return size of cards
   */
  public int getSizeOfPalette() {
    return cards.size(); // Defensive copy
  }

  /**
   * Finds the most occurring number in the palette.
   *
   * @return the number of occurrences
   */
  public int getMostOfOneNumber() {
    Map<Integer, Integer> countMap = new HashMap<>();
    // Count occurrences of each card
    for (Card card : cards) {
      countMap.put(CardNumberColor.getNumber(card),
              countMap.getOrDefault(CardNumberColor.getNumber(card), 0) + 1);
    }
    return Collections.max(countMap.values());
  }

  /**
   * Returns the number of different colors in the palette.
   *
   * @return the number of different colors
   */
  public int getMostOfDifferentColors() {
    Set<CardColors> uniqueColors = new HashSet<>();
    for (Card card : cards) {
      uniqueColors.add(CardNumberColor.getColor(card));
    }
    return uniqueColors.size();
  }

  /**
   * Returns the longest run of consecutive numbers in the palette.
   *
   * @return the length of the longest run
   */
  public int getLongestRun() {
    // Sort the cards by number
    List<Card> sortedCards = new ArrayList<>(cards);
    sortedCards.sort(Comparator.comparingInt(CardNumberColor::getNumber));
    // Find the longest run of consecutive numbers
    int longestRun = 1;
    int currentRun = 1;
    int lastNumber = CardNumberColor.getNumber(sortedCards.get(0));
    for (int card = 1; card < sortedCards.size(); card++) {
      // Check if the current number is one more than the last number
      int currentNumber = CardNumberColor.getNumber(sortedCards.get(card));
      if (currentNumber == lastNumber + 1) {
        currentRun++;
        if (currentRun > longestRun) {
          longestRun = currentRun;
        }
      } else if (currentNumber == lastNumber) {
        // Skip duplicate numbers
        continue;
      } else {
        currentRun = 1;
      }
      lastNumber = currentNumber;
    }

    return longestRun;
  }

  /**
   * Returns the number of cards with a number less than 4.
   *
   * @return occurrences of cards < 4
   */
  public int getMostBelowFour() {
    int count = 0;
    // Count how many cards have a number less than 4
    for (Card card : cards) {
      if (CardNumberColor.getNumber(card) < 4) {
        count++;
      }
    }
    return count;
  }
}
