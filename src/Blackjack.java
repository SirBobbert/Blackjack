import java.util.ArrayList;

public class Blackjack implements iBlackjackMethods{

    int playerScore;
    int dealerScore;

    ArrayList<Card> deck = new ArrayList<>(52);


    public void play() {

        this.deck = new Card().createDeck();


        for (Card x : deck) {
            System.out.println(x);
        }
    }

    @Override
    public void hit() {

    }

    @Override
    public void stand() {

    }

    @Override
    public void bust() {

    }

    @Override
    public void push() {

    }
}
