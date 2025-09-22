import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack implements iBlackjackMethods {

    int playerScore;
    int dealerScore;

    private ArrayList<Card> deck = new ArrayList<>(52);

    private final ArrayList<Card> playerHand = new ArrayList<>(52);
    private final ArrayList<Card> dealerHand = new ArrayList<>(52);

    private final Scanner scanner = new Scanner(System.in);


    public void play() {


        this.deck = new Card().createDeck();

        while (true) {

            dealersInitialAction();

            for (int i = 0; i < deck.size(); i++) {

                headline("your hand");
                System.out.println(playerHand);
                System.out.println("TOTAL SCORE: " + playerScore);

                headline("what would you like to do?");
                printOptions();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        hit(i);
                        if (bust()) {
                            System.out.println("BUST! Final total: " + playerScore);
                            return;
                        }
                        if (playerScore == 21) {
                            System.out.println("You win!");
                            return;
                        }
                    }
                    case 2 -> stand();
                }


            }
        }
    }

    public void dealersInitialAction() {
        playerDrawCard();
        dealerDrawCard();
        playerDrawCard();
        dealerDrawCard();
        dealerRevealFirstCard();
    }

    public void playerDrawCard() {

        Card card = deck.getFirst();

        playerHand.add(card);
        playerScore += card.getValue();
        deck.removeFirst();

        headline("you drew");
        System.out.println(playerHand);
    }

    public void dealerDrawCard() {

        Card card = deck.getFirst();

        dealerHand.add(card);
        dealerScore += card.getValue();
        deck.removeFirst();

        headline("dealer drew");
    }

    public void dealerRevealFirstCard() {
        headline("reveal first card");
        System.out.println(dealerHand.get(1));
        System.out.println("DEALER TOTAL SCORE: " + dealerHand.get(1).getValue());
    }

    public void headline(String str) {
        System.out.println("==================================== " + str.toUpperCase() + " ====================================");
    }

    // adds another card to hand
    @Override
    public void hit(int x) {
        System.out.println("You chose to hit!");

        Card currentCard = deck.get(x);

        playerHand.add(currentCard);
        playerScore += currentCard.getValue();
        deck.remove(currentCard);

        System.out.println("======== YOU DREW ========");
        System.out.println(currentCard);


    }

    // don't add another card to hand
    @Override
    public void stand() {
        System.out.println("You chose to stand");

    }

    // going above 21 in score
    @Override
    public boolean bust() {
        return playerScore > 21;
    }

    // both dealer and player(s) have the same scores
    @Override
    public void push() {

    }

    private void printOptions() {
        System.out.println("1: hit");
        System.out.println("2: stand");
    }
}
