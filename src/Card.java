import java.util.ArrayList;

enum SUIT {
    spades, hearts, diamonds, clubs
}

public class Card {

    ArrayList<Card> deck = new ArrayList<>(52);
    String[] allRanks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    String rank;
    SUIT suit;

    public Card() {
    }

    public Card(String rank, SUIT suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public void createDeck() {
        for (SUIT s : SUIT.values()) {
            for (int i = 0; i < 13; i++) {
                this.suit = s;
                this.rank = allRanks[i];
                deck.add(new Card(this.rank, this.suit));
            }
        }
    }

    public void printDeck() {
        for (Card card : deck) {
            System.out.println(card.toString());
        }
    }

    @Override
    public String toString() {
        return "rank='" + rank + '\'' +
                ", suit=" + suit;
    }
}
