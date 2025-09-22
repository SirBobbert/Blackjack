import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

enum SUIT {
    SPADES("♠"),
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣");

    private final String symbol;


    SUIT(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}


public class Card {

    HelperClass helper = new HelperClass();


    ArrayList<Card> deck = new ArrayList<>(52);
    String[] allRanks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    String rank;
    SUIT suit;
    int value;

    public Card() {
    }

    public Card(String rank, SUIT suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public ArrayList<Card> createDeck() {
        for (SUIT s : SUIT.values()) {
            for (int i = 0; i < 13; i++) {
                this.suit = s;
                this.rank = allRanks[i];
                deck.add(new Card(this.rank, this.suit));
            }
        }

        Collections.shuffle(deck);
        return deck;
    }

    public int getValue() {

        switch (this.rank.toUpperCase()) {
            case "2" -> this.value = 2;
            case "3" -> this.value = 3;
            case "4" -> this.value = 4;
            case "5" -> this.value = 5;
            case "6" -> this.value = 6;
            case "7" -> this.value = 7;
            case "8" -> this.value = 8;
            case "9" -> this.value = 9;
            case "10", "K", "J", "Q" -> this.value = 10;
            case "A" -> this.value = 11;
        }

        return value;
    }

    // TODO: handle if ace is drawn first
    // TODO: auto pick based on current score - both player and dealer
    private void chooseAceValue() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            helper.headline("You got an Ace! Choose its value (1 or 11): ");

            String input = scanner.nextLine().trim();

            // Check if it's "1" or "11"
            if (input.equals("1")) {
                this.value = 1;
                break;
            } else if (input.equals("11")) {
                this.value = 11;
                break;
            } else {
                helper.headline("Invalid choice. Please type exactly 1 or 11.");
            }
        }
    }

    @Override
    public String toString() {

        return suit.getSymbol() + rank;
    }
}
