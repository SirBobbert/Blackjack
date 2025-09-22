import java.util.ArrayList;

public class Blackjack {

    int playerScore;
    int dealerScore;

    ArrayList<Card> deck = new ArrayList<>(52);



    public void play() {

        Card deck = new Card();

        deck.createDeck();

        deck.printDeck();
    }

    public ArrayList<Card> shuffleDeck() {
        return deck;
    }

}
